package ru.cwt.devscheck.notification.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.cwt.devscheck.notification.model.NotificationMessage;
import ru.cwt.devscheck.notification.NotificationProto;

/**
 * @author e.chertikhin
 * @date 28/01/2017
 * <p>
 * Copyright (c) 2017 CrestWave technologies LLC. All right reserved.
 */
public class TelegramNotification implements NotificationProto {
    private static final Logger log = LoggerFactory.getLogger(TelegramNotification.class);

    @Override
    public boolean send(NotificationMessage message) {
        return false;
    }
}
