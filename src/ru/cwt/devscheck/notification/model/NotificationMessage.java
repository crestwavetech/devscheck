package ru.cwt.devscheck.notification.model;

/**
 * @author e.chertikhin
 * @date 03/02/2017
 * <p>
 * Copyright (c) 2017 CrestWave technologies LLC. All right reserved.
 */
public class NotificationMessage {

    // ok/warning/alert
    NotificationType type;
    String to;
    String subject;
    String body;
    // template !!! depend on type

    public NotificationMessage() {
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("{");
        sb.append("\"to\"=\"").append(to).append("\"");
        sb.append(", \"subject\"=\"").append(subject).append("\"");
        sb.append(", \"body\"=\"").append(body).append("\"");
        sb.append(", \"type\"=\"").append(type).append("\"");
        sb.append('}');
        return sb.toString();
    }
}
