package ru.cwt.devscheck.notification.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.cwt.devscheck.common.ConfigurationServiceBean;
import ru.cwt.devscheck.notification.model.NotificationMessage;
import ru.cwt.devscheck.notification.NotificationService;

import javax.annotation.PostConstruct;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * @author e.chertikhin
 * @date 28/01/2017
 * <p>
 * Copyright (c) 2017 CrestWave technologies LLC. All right reserved.
 */
@Service
public class EmailNotification implements NotificationService {
    private static final Logger log = LoggerFactory.getLogger(EmailNotification.class);

    @Autowired
    ConfigurationServiceBean conf;

    Session session;

    @PostConstruct
    private void init() {
        Properties props = new Properties();
        props.put("mail.smtp.starttls.enable","true");
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.host", conf.getProps("mail.server"));
        props.put("mail.smtp.port", conf.getProps("mail.server.port"));

        if ((conf.getProps("mail.auth.login") != null) && (conf.getProps("mail.auth.password") != null)) {
            props.put("mail.smtp.auth", "true");

            session = Session.getDefaultInstance(props,
                    new javax.mail.Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(conf.getProps("mail.auth.login"),
                                    conf.getProps("mail.auth.password"));
                        }
                    });
        }
        else {
            props.put("mail.smtp.auth", "false");
            session = Session.getDefaultInstance(props);
        }
    }

    @Override
    public boolean send(NotificationMessage m) {
        try {
            Message message = new MimeMessage(session);

            message.setFrom(new InternetAddress(conf.getProps("mail.from")));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(m.getTo()));
            message.setSubject(m.getSubject());
            message.setText(m.getBody());
            Transport.send(message);

            return true;

        } catch (Exception e) {
            log.error("error during sending email", e);
            return false;
        }
    }
}
