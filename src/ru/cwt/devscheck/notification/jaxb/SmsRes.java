package ru.cwt.devscheck.notification.jaxb;

import java.util.List;

/**
 * @author andreynovikov
 * @date 29.12.16
 * <p>
 * Copyright (c) 2017 CrestWave technologies LLC. All right reserved.
 */
public class SmsRes {
    private List<Message> messages;

    public SmsRes() {
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    @Override
    public String toString() {
        return "SmsRes{" +
                "messages=" + messages +
                '}';
    }
}
