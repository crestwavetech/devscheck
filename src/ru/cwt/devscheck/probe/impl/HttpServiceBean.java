package ru.cwt.devscheck.probe.impl;

import org.apache.commons.io.IOUtils;
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

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
public class HttpServiceBean implements CheckProto {

    @Override
    public ServiceStatus check(Host host, ServiceCheck check) {
        check.setStartDate(new Date());

        ServiceStatus res = new ServiceStatus();
        res.setHostName(host.getName());
        res.setCheckName(check.getName());

        if (check.getTimeout() == null) {
            check.setTimeout(1000);
        }

        if (StringUtils.isEmpty(check.getUrl()) ||
                StringUtils.isEmpty(check.getParams().get(ServiceParam.method))) {
            res.setStatus(CheckStatus.MISCONFIGURED);
        } else {
            try {
                URL url = new URL(check.getUrl());

                HttpURLConnection urlConnect = (HttpURLConnection)url.openConnection();
                urlConnect.setDoInput(true);
                urlConnect.setUseCaches(false);
                urlConnect.setRequestMethod(check.getParams().get(ServiceParam.method));
                urlConnect.setConnectTimeout(check.getTimeout());

                if ("POST".equalsIgnoreCase(check.getParams().get(ServiceParam.method)) &&
                        StringUtils.isNotEmpty(check.getParams().get(ServiceParam.data))) {
                    urlConnect.setDoOutput(true);

                    OutputStream output = urlConnect.getOutputStream();
                    output.write(check.getParams().get(ServiceParam.data).getBytes("UTF-8"));
                    output.close();
                }

                if ("GET".equalsIgnoreCase(check.getParams().get(ServiceParam.method))) {
                    // TODO
                }

                if ("HEAD".equalsIgnoreCase(check.getParams().get(ServiceParam.method))) {
                    // TODO
                }

                String out = IOUtils.toString(urlConnect.getInputStream(), "UTF-8");
                if (StringUtils.isNotEmpty(out)) {
                    res.setStatus(CheckStatus.AVAIL);
                    res.setResult(new Double(out.length()));
                    res.setResultString(out);
                } else {
                    res.setStatus(CheckStatus.NOTAVAIL);
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
     * this is hint data for administrative interface
     *
     * @return Map with key-value data
     */
    @Override
    public Map<ServiceParam, String> getServiceParams() {
        Map<ServiceParam, String> params = new HashMap<>();

        params.put(ServiceParam.method, "Supported methods is GET, POST, HEAD");
        params.put(ServiceParam.data, "In case of POST method 'data' field can contain set of key=value lines");

        return params;
    }
}
