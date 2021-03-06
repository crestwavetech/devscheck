package ru.cwt.devscheck.dao;

import ru.cwt.devscheck.probe.model.Host;
import ru.cwt.devscheck.probe.model.HostGroup;
import ru.cwt.devscheck.probe.model.ServiceCheck;
import ru.cwt.devscheck.probe.model.ServiceGroup;
import ru.cwt.devscheck.probe.model.ServiceStatus;
import ru.cwt.devscheck.probe.model.Treshold;

/**
 * @author e.chertikhin
 * @date 12/01/2017
 * <p>
 * Copyright (c) 2017 CrestWave technologies LLC. All right reserved.
 */
public interface CommonDao {

    void addHost(Host host);
    boolean updateHost(Host host);

    void addHostGroup(HostGroup group);
    void updateHostGroup(HostGroup group);

    void addServiceGroup(ServiceGroup group);
    void updateServiceGroup(ServiceGroup group);

    void addServiceCheck(String host, ServiceCheck check);
    void updateServiceCheck(ServiceCheck check);

    void addTreshold(Treshold treshold);

}
