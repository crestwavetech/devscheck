package ru.cwt.devscheck.probe.discovery;

import com.github.jgonian.ipmath.Ipv4;
import com.github.jgonian.ipmath.Ipv4Range;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.cwt.devscheck.probe.CheckProto;
import ru.cwt.devscheck.probe.model.Host;
import ru.cwt.devscheck.probe.model.ServiceCheck;
import ru.cwt.devscheck.probe.model.ServiceStatus;
import ru.cwt.devscheck.probe.model.dict.AddressType;
import ru.cwt.devscheck.probe.model.dict.CheckStatus;
import ru.cwt.devscheck.probe.model.dict.HostType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author e.chertikhin
 * @date 12/01/2017
 *
 * Сервис бин для (авто) поиска хостов в сети
 *
 * Copyright (c) 2017 CrestWave technologies LLC. All right reserved.
 */
@Service
public class DiscoveryService {
    private static final Logger log = LoggerFactory.getLogger(DiscoveryService.class);

    @Autowired
    ApplicationContext context;

    List<DiscoveryTask> range = new ArrayList<>();

    @Scheduled(cron="* * * * * *")
    private void processScan() {
        for (DiscoveryTask task : range) {
            if (!task.isRemove()) {
                task.setRemove(true);
                for (Ipv4 ip : task.getRange()) {
                    task.getCheck().setUrl(ip.toString());

                    log.info("Discovery {} check {} in {}", this, task.getCheck().getName(), ip.toString());

                    ServiceStatus status;
                    try {
                        Class clazz = Class.forName(task.getCheck().getServiceBeanName());
                        CheckProto checkProto = (CheckProto) context.getBean(clazz);

                        Host host = new Host("Autodiscovered host " + ip, HostType.AUTO, ip.toString(), AddressType.IPV4);
                        status = checkProto.check(host, task.getCheck());

                    } catch (Exception e) {
                        status = new ServiceStatus();
                        status.setCheckName(task.getCheck().getName());
                        status.setStatus(CheckStatus.MISCONFIGURED);
                        status.setDate(new Date());
                    }

                    log.info("discovery result {} status {}", task.getCheck(), status);
                }
            }
        }
        range = new ArrayList<>();
    }

    public boolean scan(String start, String end, ServiceCheck check) {
        Ipv4 ipFrom = null;
        Ipv4 ipTo = null;

        if ((check == null) || StringUtils.isEmpty(check.getName()) ||
                StringUtils.isEmpty(check.getServiceBeanName())) {
            log.error("Check parameter can't be null");
            return false;
        }

        try {
            ipFrom = Ipv4.parse(start);
            ipTo = Ipv4.parse(end);
        } catch (Exception e) {
            log.error("Wrong parameters {} - {} check {}", start, end, check);
            return false;
        }

        range.add(new DiscoveryTask(Ipv4Range.from(ipFrom).to(ipTo), check));

        return true;
    }
}

class DiscoveryTask {
    Ipv4Range range;
    ServiceCheck check;
    boolean remove;

    public DiscoveryTask(Ipv4Range range, ServiceCheck check) {
        this.range = range;
        this.check = check;
        remove = false;
    }

    public Ipv4Range getRange() {
        return range;
    }

    public void setRange(Ipv4Range range) {
        this.range = range;
    }

    public ServiceCheck getCheck() {
        return check;
    }

    public void setCheck(ServiceCheck check) {
        this.check = check;
    }

    public boolean isRemove() {
        return remove;
    }

    public void setRemove(boolean remove) {
        this.remove = remove;
    }
}
