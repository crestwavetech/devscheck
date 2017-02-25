package ru.cwt.devscheck.dao.impl;

import org.springframework.stereotype.Repository;
import ru.cwt.devscheck.dao.CommonDao;
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
@Repository
public class CommonDaoImpl implements CommonDao {

    @Override
    public void addHost(Host host) {

    }

    @Override
    public boolean updateHost(Host host) {
        return false;
    }

    @Override
    public void addHostGroup(HostGroup group) {

    }

    @Override
    public void updateHostGroup(HostGroup group) {

    }

    @Override
    public void addServiceGroup(ServiceGroup group) {

    }

    @Override
    public void updateServiceGroup(ServiceGroup group) {

    }

    @Override
    public void addServiceCheck(String host, ServiceCheck check) {

    }

    @Override
    public void updateServiceCheck(ServiceCheck check) {

    }

    @Override
    public void addTreshold(Treshold treshold) {

    }
}
