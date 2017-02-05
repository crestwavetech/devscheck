package ru.cwt.devscheck.probe.model;

/**
 * @author e.chertikhin
 * @date 04/02/2017
 * <p>
 * Copyright (c) 2017 CrestWave technologies LLC. All right reserved.
 */
public class PoolerTask {
    Host host;
    ServiceCheck check;

    public PoolerTask() {
    }

    public PoolerTask(Host host, ServiceCheck check) {
        this.host = host;
        this.check = check;
    }

    public Host getHost() {
        return host;
    }

    public void setHost(Host host) {
        this.host = host;
    }

    public ServiceCheck getCheck() {
        return check;
    }

    public void setCheck(ServiceCheck check) {
        this.check = check;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("{");
        sb.append("\"host\":\"").append(host).append("\"");
        sb.append(", \"check\":\"").append(check).append("\"");
        sb.append('}');
        return sb.toString();
    }
}
