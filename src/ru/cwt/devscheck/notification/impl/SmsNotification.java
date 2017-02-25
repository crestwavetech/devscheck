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
import org.springframework.beans.factory.annotation.Value;
import ru.cwt.devscheck.notification.NotificationProto;
import ru.cwt.devscheck.notification.jaxb.SmsReq;
import ru.cwt.devscheck.notification.jaxb.SmsRes;
import ru.cwt.devscheck.notification.model.NotificationMessage;

import javax.annotation.PostConstruct;

/**
 * @author e.chertikhin
 * @date 28/01/2017
 * <p>
 * Copyright (c) 2017 CrestWave technologies LLC. All right reserved.
 */
public class SmsNotification implements NotificationProto {
    private static final Logger log = LoggerFactory.getLogger(SmsNotification.class);

    @Value("${sms.url}")
    String url;

    @Value("${sms.from}")
    String smsFrom;

    @Value("${sms.auth}")
    String smsAuth;

    HttpPost httpPost;
    HttpClient httpClient;
    ObjectMapper mapper;

    @PostConstruct
    private void init() {
        mapper = new ObjectMapper();
        httpClient = HttpClients.createDefault();
        httpPost = new HttpPost(url);
    }

    @Override
    public boolean send(NotificationMessage message) {
        try {
            SmsReq request = new SmsReq();
            request.setFrom(smsFrom);
            request.setTo(message.getTo());
            request.setText(message.getBody());

            StringEntity params = new StringEntity(mapper.writeValueAsString(request));
            params.setContentType("application/json");
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Authorization", "Basic " + smsAuth);
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
