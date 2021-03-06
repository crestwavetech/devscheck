package ru.cwt.devscheck.probe.model;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.Map;
import java.util.Random;

/**
 * @author e.chertikhin
 * @date 12/01/2017
 *
 * ServiceCheck - checkName definition. checkName will be run for some (defined) hostName
 *
 * Copyright (c) 2017 CrestWave technologies LLC. All right reserved.
 */
public class ServiceCheck {

    String name;

    /**
     * Address to checkName (may be ip, url or for example JdbcString). Format (and type) of this
     * depend on ServiceBean
     */
    String url;

    /**
     * Timeout for checkName, can be null (standard timeout)
     */
    Integer timeout;

    /**
     * Override period to checkName in seconds
     */
    Integer period;

    /**
     * Additional data
     */
    Map<ServiceParam, String> params;

    /**
     * Tresholds
     */
    Treshold low;
    Treshold def;

    /**
     * Link to corresponding probe bean
     */
    String serviceBeanName;

    /**
     * dates...
     */
    Date createDate;
    Date updateDate;

    Date scheduledDate;
    Date startDate;
    Date lastCheckDate;

    public ServiceCheck() {
    }

    public ServiceCheck(String name, String url, Map<ServiceParam, String> params, String serviceBeanName) {
        this.name = name;
        this.url = url;
        this.params = params;
        this.serviceBeanName = serviceBeanName;
//        this.createDate = new Date();
    }

    @Override
    public int hashCode() {
        int i = 0;

        if (StringUtils.isNotEmpty(name))
            i =+ name.hashCode();

        if (StringUtils.isNotEmpty(url))
            i =+ url.hashCode();

        if (StringUtils.isNotEmpty(serviceBeanName))
            i =+ serviceBeanName.hashCode();

        if (params != null)
            i =+ params.hashCode();

        if (createDate != null)
            i =+ createDate.hashCode();

        return i;
    }

    public Map<ServiceParam, String> getParams() {
        return params;
    }

    public void setParams(Map<ServiceParam, String> params) {
        this.params = params;
    }

    public Date getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(Date scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public Treshold getLow() {
        return low;
    }

    public void setLow(Treshold low) {
        this.low = low;
    }

    public Treshold getDef() {
        return def;
    }

    public void setDef(Treshold def) {
        this.def = def;
    }

    public String getServiceBeanName() {
        return serviceBeanName;
    }

    public void setServiceBeanName(String serviceBeanName) {
        this.serviceBeanName = serviceBeanName;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Date getLastCheckDate() {
        return lastCheckDate;
    }

    public void setLastCheckDate(Date lastCheckDate) {
        this.lastCheckDate = lastCheckDate;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("{");
        sb.append("\"name\":\"").append(name).append("\"");
        sb.append(", \"url\":\"").append(url).append("\"");
        sb.append(", \"timeout\":\"").append(timeout).append("\"");
        if (params != null)
            sb.append(", \"params\":").append(params);
        if (low != null)
            sb.append(", \"low\":").append(low);
        if (def != null)
            sb.append(", \"def\":").append(def);
        sb.append(", \"serviceBeanName\"=\"").append(serviceBeanName).append("\"");
        sb.append(", \"createDate\"=\"").append(createDate).append("\"");
        sb.append(", \"updateDate\"=\"").append(updateDate).append("\"");
        sb.append(", \"scheduledDate\"=\"").append(scheduledDate).append("\"");
        sb.append(", \"startDate\"=\"").append(startDate).append("\"");
        sb.append(", \"lastCheckDate\"=\"").append(lastCheckDate).append("\"");
        sb.append('}');
        return sb.toString();
    }
}
