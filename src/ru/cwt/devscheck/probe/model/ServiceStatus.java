package ru.cwt.devscheck.probe.model;

import ru.cwt.devscheck.probe.model.dict.CheckStatus;

import java.util.Date;
import java.util.List;

/**
 * @author e.chertikhin
 * @date 13/01/2017
 *
 * Service checkName result
 *
 * Copyright (c) 2017 CrestWave technologies LLC. All right reserved.
 */
public class ServiceStatus {

    String hostName;
    String checkName;

    /**
     * Numberic result value
     */
    Double result;

    /**
     * String result value
     */
    String resultString;

    /**
     * How many seconds was taken
     */
    Long timeout;

    /**
     * Check status (ok, notOk, misConfigured)
     */
    CheckStatus status;
    CheckStatus prevStatus;

    boolean progress;

    Date date;

    public ServiceStatus() {
    }

    public CheckStatus getPrevStatus() {
        return prevStatus;
    }

    public void setPrevStatus(CheckStatus prevStatus) {
        this.prevStatus = prevStatus;
    }

    public boolean isProgress() {
        return progress;
    }

    public void setProgress(boolean progress) {
        this.progress = progress;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getCheckName() {
        return checkName;
    }

    public void setCheckName(String checkName) {
        this.checkName = checkName;
    }

    public Double getResult() {
        return result;
    }

    public void setResult(Double result) {
        this.result = result;
    }

    public String getResultString() {
        return resultString;
    }

    public void setResultString(String resultString) {
        this.resultString = resultString;
    }

    public Long getTimeout() {
        return timeout;
    }

    public void setTimeout(Long timeout) {
        this.timeout = timeout;
    }

    public CheckStatus getStatus() {
        return status;
    }

    public void setStatus(CheckStatus status) {
        this.status = status;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("{");
        sb.append("\"hostName\"=\"").append(hostName).append("\"");
        sb.append(", \"checkName\"=\"").append(checkName).append("\"");
        sb.append(", \"result\"=\"").append(result).append("\"");
        sb.append(", \"resultString\"=\"").append(resultString).append("\"");
        sb.append(", \"timeout\"=\"").append(timeout).append("\"");
        sb.append(", \"status\"=\"").append(status).append("\"");
        sb.append(", \"prevStatus\"=\"").append(prevStatus).append("\"");
        sb.append(", \"date\"=\"").append(date).append("\"");
        sb.append(", \"progress\"=\"").append(progress).append("\"");
        sb.append('}');
        return sb.toString();
    }
}
