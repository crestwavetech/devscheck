package ru.cwt.devscheck.status.model;

import java.util.Date;

/**
 * @author e.chertikhin
 * @date 26/01/2017
 * <p>
 * Copyright (c) 2017 CrestWave technologies LLC. All right reserved.
 */
public class Status {

    int hosts;
    int services;
    int queue;
    int res;
    Date started;

    public int getRes() {
        return res;
    }

    public void setRes(int res) {
        this.res = res;
    }

    public int getHosts() {
        return hosts;
    }

    public void setHosts(int hosts) {
        this.hosts = hosts;
    }

    public int getServices() {
        return services;
    }

    public void setServices(int services) {
        this.services = services;
    }

    public int getQueue() {
        return queue;
    }

    public void setQueue(int queue) {
        this.queue = queue;
    }

    public Date getStarted() {
        return started;
    }

    public void setStarted(Date started) {
        this.started = started;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("{");
        sb.append("\"hosts\"=").append(hosts);
        sb.append(", \"services\"=").append(services);
        sb.append(", \"queue\"=").append(queue);
        sb.append(", \"res\"=").append(res);
        sb.append(", \"started\"=\"").append(started).append("\"");
        sb.append('}');
        return sb.toString();
    }
}
