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

    boolean storeHost(Host host);
    boolean storeHostGroup(HostGroup group);
    boolean storeServiceGroup(ServiceGroup group);

    boolean storeGlobalTreshold(Treshold treshold);
    boolean storeGlobalCheck(ServiceCheck check);

    boolean storeResult(ServiceStatus status);
}
