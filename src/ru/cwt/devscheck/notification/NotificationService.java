package ru.cwt.devscheck.notification;

import ru.cwt.devscheck.notification.model.NotificationMessage;

/**
 * @author e.chertikhin
 * @date 28/01/2017
 * <p>
 * Copyright (c) 2017 CrestWave technologies LLC. All right reserved.
 */
public interface NotificationService {

    public boolean send(NotificationMessage message);
}
