package ru.cwt.devscheck.notification.jaxb;

/**
 * Created by andreynovikov on 29.12.16.
 */
public class Message {

    private String to;
    private Status status;
    private int smsCount;
    private String messageId;

    public Message() {
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getSmsCount() {
        return smsCount;
    }

    public void setSmsCount(int smsCount) {
        this.smsCount = smsCount;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    @Override
    public String toString() {
        return "Message{" +
                "to='" + to + '\'' +
                ", status=" + status +
                ", smsCount=" + smsCount +
                ", messageId='" + messageId + '\'' +
                '}';
    }
}
