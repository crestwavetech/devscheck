package ru.cwt.devscheck.notification.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.cwt.devscheck.common.ConfigurationServiceBean;
import ru.cwt.devscheck.notification.jaxb.SmsReq;
import ru.cwt.devscheck.notification.jaxb.SmsRes;
import ru.cwt.devscheck.notification.model.NotificationMessage;
import ru.cwt.devscheck.notification.NotificationService;

import javax.annotation.PostConstruct;

/**
 * @author e.chertikhin
 * @date 28/01/2017
 * <p>
 * Copyright (c) 2017 CrestWave technologies LLC. All right reserved.
 */
public class SmsNotification implements NotificationService {
    private static final Logger log = LoggerFactory.getLogger(SmsNotification.class);

    @Autowired
    ConfigurationServiceBean conf;

    HttpPost httpPost;
    HttpClient httpClient;
    ObjectMapper mapper;

    @PostConstruct
    private void init() {
        mapper = new ObjectMapper();
        httpClient = HttpClients.createDefault();
        httpPost = new HttpPost(conf.getProps("sms.url"));
    }

    @Override
    public boolean send(NotificationMessage message) {
        try {
            SmsReq request = new SmsReq();
            request.setFrom(conf.getProps("sms.from"));
            request.setTo(message.getTo());
            request.setText(message.getBody());

            StringEntity params = new StringEntity(mapper.writeValueAsString(request));
            params.setContentType("application/json");
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Authorization", "Basic " + conf.getProps("sms.auth"));
            httpPost.setEntity(params);

            HttpResponse httpResp = httpClient.execute(httpPost);

            HttpEntity entity = httpResp.getEntity();
            if (entity != null) {
                String responseJson = EntityUtils.toString(entity, "UTF-8");
                EntityUtils.consume(entity);

                log.info("Sms send result " + mapper.readValue(responseJson, SmsRes.class));

                return true;
            }
            throw new Exception("empty response");
        } catch (Exception e) {
            log.error("error sending sms");
        }

        return true;
    }
}
