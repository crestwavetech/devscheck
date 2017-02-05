package ru.cwt.devscheck.probe;

import ru.cwt.devscheck.probe.model.Host;
import ru.cwt.devscheck.probe.model.ServiceStatus;

/**
 * @author e.chertikhin
 * @date 13/01/2017
 * <p>
 * Copyright (c) 2017 CrestWave technologies LLC. All right reserved.
 */
public interface TrapService {

    ServiceStatus trap(Host host);
}
