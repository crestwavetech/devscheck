package ru.cwt.devscheck.probe.manager;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import ru.cwt.devscheck.notification.manager.NotificationService;
import ru.cwt.devscheck.probe.model.Host;
import ru.cwt.devscheck.probe.model.PoolerTask;
import ru.cwt.devscheck.probe.model.ServiceCheck;
import ru.cwt.devscheck.probe.model.ServiceParam;
import ru.cwt.devscheck.probe.model.ServiceStatus;
import ru.cwt.devscheck.probe.model.Treshold;
import ru.cwt.devscheck.probe.model.dict.AddressType;
import ru.cwt.devscheck.probe.model.dict.CheckStatus;
import ru.cwt.devscheck.probe.model.dict.HostType;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author e.chertikhin
 * @date 12/01/2017
 *
 * Базовые методы для работы с объектами системы
 *
 * Copyright (c) 2017 CrestWave technologies LLC. All right reserved.
 */
@Service
public class ProbeService {
    private static final Logger log = LoggerFactory.getLogger(ProbeService.class);

    @Autowired
    ApplicationContext context;

    @Autowired
    NotificationService notificationService;

    @Value("${pooler.count.init?:5}")
    String poolerCountInit;

    /**
     * Map parameter: HostName, Host itself
     */
    Map<String, Host> hosts;

    /**
     * Map of created, pre-defined pooler to run checks
     */
    Set<Pooler> poolers;

    Map<Integer, ServiceStatus> services;

    // globalChecks

    // globalTreshold

    @PostConstruct
    private void init() {
        hosts = new HashMap();
        poolers = new HashSet<>();
        services = new HashMap<>();

        int poolersCount;

        if (!NumberUtils.isDigits(poolerCountInit))
            poolerCountInit = "5";

        poolersCount = NumberUtils.toInt(poolerCountInit);

        for (int i = 0; i < poolersCount; i++) {
            Pooler p = (Pooler) context.getBean(Pooler.class);
            poolers.add(p);
        }

        // store/fetch hosts from db
        sync();

        // finally
        log.info("Init " + this.getClass().getName() + " complete");
    }

    @Scheduled(cron="*/10 * * * * *")
    public void scheduler() {
        for (String host : hosts.keySet()) {
            Host h = hosts.get(host);

            // get pooler for host
            Pooler p = findPooler();

            if(!CollectionUtils.isEmpty(h.getChecks())) {

                for (ServiceCheck check: h.getChecks()) {
                    ServiceStatus status = services.get(check.hashCode());
                    if (status != null && status.isProgress()) {
                        log.error("Check {} for {} is still in progress", check.getName(), host);
                    }
                    else {
                        if (status == null) {
                            status = new ServiceStatus();
                        }

                        status.setProgress(true);
                        services.put(check.hashCode(), status);

                        check.setScheduledDate(new Date());
                        p.put(new PoolerTask(h, check));
                    }
                }

            } else {
                log.error("Host {} has no probe check", host);
            }
        }

        sync();
    }

    /**
     * Found mostly free pooler
     */
    private Pooler findPooler() {
        Pooler pooler = null;
        int count = 999999999;

        for (Pooler p : poolers) {
            if(p.count() == 0)
                return p;

            if(p.count() < count) {
                pooler = p;
                count = p.count();
            }
        }

        return pooler;
    }

    public void storeResult(ServiceCheck check, ServiceStatus status) {
        ServiceStatus prev = services.get(check.hashCode());

        // check for flap
        if (CheckStatus.AVAIL.equals(prev.getPrevStatus()) && CheckStatus.NOTAVAIL.equals(prev.getStatus()) &&
                CheckStatus.AVAIL.equals(status.getStatus()))
            status.setStatus(CheckStatus.FLAP);
        else if (CheckStatus.NOTAVAIL.equals(prev.getPrevStatus()) && CheckStatus.AVAIL.equals(prev.getStatus()) &&
                CheckStatus.NOTAVAIL.equals(status.getStatus()))
            status.setStatus(CheckStatus.FLAP);

        status.setPrevStatus(prev.getStatus());

        // process tresholds
        // todo status.setStatus(CheckStatus.NOTAVAIL);

        // notify admin
        if (status.getStatus().equals(CheckStatus.FLAP)) {
            // service is in flap
            log.info("notify service " + status.getCheckName() + " at " + status.getHostName() + " is on FLAP");
        }
        else if(!status.getStatus().equals(status.getPrevStatus())) {
            // status changed
            if (status.getStatus().equals(CheckStatus.AVAIL)) {
                // service is UP
                log.info("notify service " + status.getCheckName() + " at " + status.getHostName() + " is UP");
            }
            else if (status.getStatus().equals(CheckStatus.NOTAVAIL)) {
                // service goes DOWN
                log.info("notify service " + status.getCheckName() + " at " + status.getHostName() + " is DOWN");
            }
            else {
                // ERROR || MISCONFIG
                log.info("notify service " + status.getCheckName()+ " at " + status.getHostName() + " is in ERROR state");
            }
        }

        status.setProgress(false);

        log.info("check result: " + status);
        services.put(check.hashCode(), status);

        // TODO store in DB
    }

    /**
     * Fetch data from db
     *
     */
    public void sync() {
        hosts.put("local", new Host("local", HostType.MACOSX, "127.0.0.1", AddressType.IPV4));
        hosts.get("local").getChecks().add(new ServiceCheck("ping", null, null,
                "ru.cwt.devscheck.probe.impl.PingServiceBean"));

//        Map<ServiceParam, String> httpParams2 = new HashMap<>();
//        httpParams2.put(ServiceParam.method,"GET");
//        hosts.get("local").getChecks().add(new ServiceCheck("http", "http://172.16.10.10/",
//                httpParams2, "ru.cwt.devscheck.probe.impl.HttpServiceBean"));
    }

    public Map<String, Integer> getPoolersStatus() {
        Map<String, Integer> res = new HashMap<>();

        for (Pooler p : poolers) {
            res.put(p.getName(), p.count());
        }

        return res;
    }

    /**
     * Add new host.
     *
     * @param host
     * @return status of operation (true/false)
     */
    public boolean addHost(Host host) {
        Host h = hosts.get(host.getName());
        if (h != null) {
            log.error("try to add host already present in database");
            return false;
        }

        hosts.put(host.getName(), host);

        return true;
    }

    /**
     * get all hosts
     *
     * @return map with hosts
     */
    public Map<String, Host> getHosts() {
        return hosts;
    }

    /**
     * Add new probe-check
     *
     * @param check ServiceCheck object
     * @return status of operation (true/false)
     */
    public boolean addServiceCheck(String hostName, ServiceCheck check) {
        Host host = hosts.get(hostName);
        if (host == null) {
            log.error("host '{}' is not exists", hostName);
            return false;
        }

        // hash creating for new (empty) host
        if (host.getChecks() == null) {
            host.setChecks(new HashSet<>());
        } else {
            // check for dublicates
            for (ServiceCheck s : host.getChecks()) {
                if (s.getName().equals(check.getName()))
                    return false;       // dublicate !
            }
        }

        host.getChecks().add(check);

        return true;
    }

    public boolean addTreshold(Treshold t) {
        // TODO

//        Host h = hosts.get(host.getName());
//        if (h != null) {
//            log.error("try to add host already present in database");
//            return false;
//        }
//
//      TODO: add/save treshold

        return true;
    }
}
