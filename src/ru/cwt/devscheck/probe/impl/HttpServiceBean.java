package ru.cwt.devscheck.probe.impl;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.cwt.devscheck.probe.model.Host;
import ru.cwt.devscheck.probe.model.ServiceCheck;
import ru.cwt.devscheck.probe.model.ServiceParam;
import ru.cwt.devscheck.probe.model.ServiceStatus;
import ru.cwt.devscheck.probe.model.dict.CheckStatus;
import ru.cwt.devscheck.probe.CheckService;

import javax.annotation.PostConstruct;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author e.chertikhin
 * @date 12/01/2017
 *
 * Сервис бин для работы с хостами по протоколу http
 *
 * Copyright (c) 2017 CrestWave technologies LLC. All right reserved.
 */
@Component
@Scope("prototype")
public class HttpServiceBean implements CheckService {
    private static final Logger log = LoggerFactory.getLogger(HttpServiceBean.class);

    @Override
    public ServiceStatus check(Host host, ServiceCheck check) {
        check.setStartDate(new Date());

        ServiceStatus res = new ServiceStatus();
        res.setHostName(host.getName());
        res.setCheckName(check.getName());

        res.setResult(new ArrayList<>());
        res.setResultString(new ArrayList<>());

        /**
         * Check params.
         *
         * required: url, method
         * option: timeout, data
         */

        if (check.getTimeout() == null) {
            check.setTimeout(1000);
        }

        String method = check.getParams().get("method");
        if (StringUtils.isEmpty(check.getUrl()) || StringUtils.isEmpty(method)) {
            res.setStatus(CheckStatus.MISCONFIGURED);
        } else {
            try {
                URL url = new URL(check.getUrl());

                HttpURLConnection urlConnect = (HttpURLConnection)url.openConnection();
                urlConnect.setDoInput(true);
                urlConnect.setUseCaches(false);
                urlConnect.setRequestMethod(method);
                urlConnect.setConnectTimeout(check.getTimeout());

                if (method.equalsIgnoreCase("post") && StringUtils.isNotEmpty(check.getParams().get("post"))) {
                    urlConnect.setDoOutput(true);

                    OutputStream output = urlConnect.getOutputStream();
                    output.write(check.getParams().get("post").getBytes("UTF-8"));
                    output.close();
                }

                String out = IOUtils.toString(urlConnect.getInputStream(), "UTF-8");
                if (StringUtils.isNotEmpty(out)) {
                    res.setStatus(CheckStatus.AVAIL);
                    res.getResultString().add(out);
                    res.getResult().add(new Double(out.length()));
                } else {
                    res.setStatus(CheckStatus.NOTAVAIL);
                }

            } catch (Exception e) {
                res.getResultString().add(e.toString());
                res.setStatus(CheckStatus.ERROR);
            }
        }

        check.setLastCheckDate(new Date());
        res.setDate(new Date());
        res.setTimeout(check.getLastCheckDate().getTime() - check.getStartDate().getTime());

        log.info("res: " + res);

        return res;
    }

    @Override
    public List<ServiceParam> getFields() {
        return null;
    }
}
