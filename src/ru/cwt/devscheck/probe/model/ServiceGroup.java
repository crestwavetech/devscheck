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

    String name;
    String description;

    public ServiceGroup() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
