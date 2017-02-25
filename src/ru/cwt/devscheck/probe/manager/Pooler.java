package ru.cwt.devscheck.probe.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.cwt.devscheck.probe.model.PoolerTask;
import ru.cwt.devscheck.probe.model.ServiceStatus;
import ru.cwt.devscheck.probe.model.dict.CheckStatus;
import ru.cwt.devscheck.probe.CheckProto;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author e.chertikhin
 * @date 27/01/2017
 * <p>
 * Copyright (c) 2017 CrestWave technologies LLC. All right reserved.
 */
@Component
@Scope("prototype")
public class Pooler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(Pooler.class);

    @Autowired
    ApplicationContext context;

    @Autowired
    ProbeService manager;

    Set<PoolerTask> tasks = new HashSet<>();

    // rescan pooler stack every one second
    @Scheduled(cron="* * * * * *")
    public void scheduler() {

        if(tasks.size() > 0) {
            for (PoolerTask task: tasks) {
                log.info("Pooler {} check {} in {}", this, task.getCheck().getName(), task.getHost().getName());

                ServiceStatus status;
                try {
                    Class clazz = Class.forName(task.getCheck().getServiceBeanName());
                    CheckProto checkProto = (CheckProto) context.getBean(clazz);

                    status = checkProto.check(task.getHost(), task.getCheck());
                } catch (Exception e) {
                    status = new ServiceStatus();
                    status.setCheckName(task.getCheck().getName());
                    status.setStatus(CheckStatus.MISCONFIGURED);
                    status.setDate(new Date());
                }

                manager.storeResult(task.getCheck(), status);
            }

            tasks = new HashSet<>();
        }
    }

    public int count() {
        return tasks.size();
    }

    public void put(PoolerTask task) {
        tasks.add(task);
    }
}
