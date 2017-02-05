package ru.cwt.devscheck.probe.model;

/**
 * @author e.chertikhin
 * @date 12/01/2017
 *
 * ServiceGroup - логическое объединение хостов в группу. Скорее всего это будет объединение по типам сервисов,
 * например Databases, WebServers и т.п.
 *
 * Copyright (c) 2017 CrestWave technologies LLC. All right reserved.
 */
public class ServiceGroup {

    String id;
    String name;

    public ServiceGroup() {
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
}
