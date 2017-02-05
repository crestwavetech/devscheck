package ru.cwt.devscheck.notification.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.cwt.devscheck.probe.model.ServiceCheck;
import ru.cwt.devscheck.probe.model.ServiceStatus;
import ru.cwt.devscheck.notification.model.NotificationMessage;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * @author e.chertikhin
 * @date 03/02/2017
 * <p>
 * Copyright (c) 2017 CrestWave technologies LLC. All right reserved.
 */
@Service
public class NotificationManager {
    private static final Logger log = LoggerFactory.getLogger(NotificationManager.class);

    List<NotificationMessage> notifications;

    @PostConstruct
    public void init() {
        notifications = new ArrayList<>();
    }

    @Scheduled(cron="0 * * * * *")
    public void sendNotifications() {
        for (NotificationMessage n : notifications) {
            log.info("Sending notification to {}: {}", n.getTo(), n.getSubject());
            // depend on type call different service

            // if sent correcntly - remove from queue
            notifications.remove(n);
        }
    }

    public void put(ServiceCheck check, ServiceStatus status) {
        NotificationMessage n = new NotificationMessage();

        // TODO

        notifications.add(n);
    }

}
