package ru.cwt.devscheck.probe;

import ru.cwt.devscheck.probe.model.Host;
import ru.cwt.devscheck.probe.model.ServiceCheck;
import ru.cwt.devscheck.probe.model.ServiceParam;
import ru.cwt.devscheck.probe.model.ServiceStatus;

import java.util.Map;

/**
 * @author e.chertikhin
 * @date 13/01/2017
 * <p>
 * Copyright (c) 2017 CrestWave technologies LLC. All right reserved.
 */
public interface CheckProto {

    ServiceStatus check(Host host, ServiceCheck check);
    Map<ServiceParam, String> getServiceParams();

}
