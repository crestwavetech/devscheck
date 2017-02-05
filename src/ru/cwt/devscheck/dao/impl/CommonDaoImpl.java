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
    public boolean storeHost(Host host) {
        return false;
    }

    @Override
    public boolean storeHostGroup(HostGroup group) {
        return false;
    }

    @Override
    public boolean storeServiceGroup(ServiceGroup group) {
        return false;
    }

    @Override
    public boolean storeGlobalTreshold(Treshold treshold) {
        return false;
    }

    @Override
    public boolean storeGlobalCheck(ServiceCheck check) {
        return false;
    }

    @Override
    public boolean storeResult(ServiceStatus status) {
        return false;
    }
}
