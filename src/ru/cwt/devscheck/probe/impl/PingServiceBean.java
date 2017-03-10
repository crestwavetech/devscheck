package ru.cwt.devscheck.probe.impl;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.cwt.devscheck.probe.CheckProto;
import ru.cwt.devscheck.probe.model.Host;
import ru.cwt.devscheck.probe.model.ServiceCheck;
import ru.cwt.devscheck.probe.model.ServiceParam;
import ru.cwt.devscheck.probe.model.ServiceStatus;
import ru.cwt.devscheck.probe.model.dict.CheckStatus;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author e.chertikhin
 * @date 12/01/2017
 *
 * Сервис бин для простого (ping) опроса хостов
 *
 * Copyright (c) 2017 CrestWave technologies LLC. All right reserved.
 */
@Component
@Scope("prototype")
public class PingServiceBean implements CheckProto {
    private static final Logger log = LoggerFactory.getLogger(PingServiceBean.class);

    @Override
    public ServiceStatus check(Host host, ServiceCheck check) {
        log.info("Pooler {} check {} in {}", this, check.getName(), host.getName());

        check.setStartDate(new Date());

        ServiceStatus res = new ServiceStatus();
        res.setHostName(host.getName());
        res.setCheckName(check.getName());

        if (check.getTimeout() == null) {
            check.setTimeout(1000);
        }

        if (StringUtils.isEmpty(host.getAddress())) {
            res.setStatus(CheckStatus.MISCONFIGURED);
        } else {
            try {
                InetAddress address = InetAddress.getByName(host.getAddress());
                if (address.isReachable(check.getTimeout())) {
                    res.setStatus(CheckStatus.AVAIL);
                    res.setResult(new Double(1));
                } else {
                    res.setStatus(CheckStatus.NOTAVAIL);
                    res.setResult(new Double(0));
                }
            } catch (Exception e) {
                res.setStatus(CheckStatus.ERROR);
            }
        }

        check.setLastCheckDate(new Date());
        res.setDate(new Date());
        res.setTimeout(check.getLastCheckDate().getTime() - check.getStartDate().getTime());

        return res;
    }

    /**
     * Ping do not require additional fields...
     *
     * @return
     */
    @Override
    public Map<ServiceParam, String> getServiceParams() {
        return new HashMap<>();
    }
}
