package ru.cwt.devscheck.probe.model;

/**
 * @author e.chertikhin
 * @date 12/01/2017
 *
 * HostGroup - физическое объединение хостов в группу. Скорее всего это будет объединение vmware hostName
 *
 * Copyright (c) 2017 CrestWave technologies LLC. All right reserved.
 */
public class HostGroup {

    String id;
    String name;
    Host host;

    public HostGroup() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Host getHost() {
        return host;
    }

    public void setHost(Host host) {
        this.host = host;
    }
}
