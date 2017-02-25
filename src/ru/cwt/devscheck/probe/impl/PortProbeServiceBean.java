package ru.cwt.devscheck.probe.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
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

import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author e.chertikhin
 * @date 12/01/2017
 *
 * Сервис бин для работы с хостами по протоколу telnet
 *
 * Copyright (c) 2017 CrestWave technologies LLC. All right reserved.
 */
@Component
@Scope("prototype")
public class PortProbeServiceBean implements CheckProto {

    @Override
    public ServiceStatus check(Host host, ServiceCheck check) {
        check.setStartDate(new Date());

        ServiceStatus res = new ServiceStatus();
        res.setHostName(host.getName());
        res.setCheckName(check.getName());

        if (StringUtils.isNotEmpty(check.getParams().get(ServiceParam.port)) &&
                StringUtils.isNotEmpty(check.getUrl())) {

            if (check.getTimeout() == null) {
                check.setTimeout(1000);
            }

            try {
                try (Socket soc = new Socket()) {
                    soc.connect(new InetSocketAddress(check.getUrl(),
                            NumberUtils.toInt(check.getParams().get(ServiceParam.port))), check.getTimeout());
                }

                if (StringUtils.isNotEmpty(check.getParams().get(ServiceParam.data))) {
                    String[] param = StringUtils.split(check.getParams().get(ServiceParam.data), "\r\n");

                    // todo: write into socket
                }

            } catch (Exception ex) {
                res.setStatus(CheckStatus.ERROR);
            }
        } else {
            res.setStatus(CheckStatus.MISCONFIGURED);
        }

        check.setLastCheckDate(new Date());
        res.setDate(new Date());
        res.setTimeout(check.getLastCheckDate().getTime() - check.getStartDate().getTime());

        return res;
    }

    @Override
    public Map<ServiceParam, String> getServiceParams() {
        Map<ServiceParam, String> params = new HashMap<>();

        params.put(ServiceParam.port, "Port to check. Required.");
        params.put(ServiceParam.data, "Data to write into socket. Format IN\\r\\nOUT. Optional.");

        return params;
    }
}
