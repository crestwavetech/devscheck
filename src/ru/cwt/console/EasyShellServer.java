package ru.cwt.console;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.RegexPatternTypeFilter;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import ru.cwt.core.utils.Config;
import ru.cwt.core.utils.TelnetUtils;
import ru.cwt.devscheck.probe.discovery.DiscoveryManager;
import ru.cwt.devscheck.probe.manager.ProbeManager;
import ru.cwt.devscheck.probe.model.Host;
import ru.cwt.devscheck.probe.model.ServiceCheck;
import ru.cwt.devscheck.probe.model.Treshold;
import ru.cwt.devscheck.probe.model.dict.AddressType;
import ru.cwt.devscheck.probe.model.dict.HostType;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * EasyShellServer
 *
 * @author hexprobe <hexprobe@nbug.net>
 *
 * @license
 * This code is hereby placed in the public domain.
 *
 */
@Service
public class EasyShellServer {
    private static final Logger log = LoggerFactory.getLogger(EasyShellServer.class);

    private final Map<String, Command> commands = new HashMap<String, Command>();
    private EasyTelnetServer telnetd = null;

    @Autowired
    CommandProcessor commandProcessor;

    @Autowired
    ProbeManager probeManager;

    @Autowired
    DiscoveryManager discoveryManager;

    @PostConstruct
    private void init() {
        registerCommand("exit", new Command() {
            @Override
            public void execute(String name, String argument, EasyTerminal terminal) throws IOException {
                terminal.close();
            }
        });

        registerCommand("help", new Command() {
            @Override
            public void execute(String name, String argument, EasyTerminal terminal) throws IOException {
                terminal.write(TelnetUtils.join(" ", commands.keySet()) + "\r\n");
                terminal.flush();
            }
        });

        registerCommand("status", new Command() {
            @Override
            public void execute(String name, String argument, EasyTerminal terminal) throws IOException {
                Map<String, Integer> status = probeManager.getPoolersStatus();

                int c = 0;
                for (String p : status.keySet()) {
                    terminal.write("Pooler " + p + " task count " + status.get(p) + "\r\n");
                    c += status.get(p);
                }

                terminal.write("Total tasks in queue " + c + "\r\n");

                terminal.flush();
            }
        });

        registerCommand("list", new Command() {
            @Override
            public void execute(String name, String argument, EasyTerminal terminal) throws IOException {
                switch (argument) {
                    case "hosts":
                        if(probeManager.getHosts().size() > 0) {
                            terminal.write("available hosts: \r\n");

                            for (String hostId : probeManager.getHosts().keySet()) {
                                Host h = probeManager.getHosts().get(hostId);
                                terminal.write(" " + h.getName() + " (" + h.getAddressType() + ":" + h.getAddress() + ")\r\n");
                            }
                        } else {
                            terminal.write("no hosts \r\n");
                        }

                        break;

                    case "checks":
                        if(probeManager.getHosts().size() > 0) {
                            terminal.write("available probe checks: \r\n");

                            for (String hostId : probeManager.getHosts().keySet()) {
                                Host h = probeManager.getHosts().get(hostId);

                                if (CollectionUtils.isEmpty(h.getChecks())) {
                                    terminal.write(" Host: " + h.getName() + " has no probe check\r\n");

                                } else {
                                    terminal.write(" Host: " + h.getName() + " (" + h.getAddressType() + ":" + h.getAddress() + ")\r\n");

                                    for (ServiceCheck sc : h.getChecks()) {
                                        terminal.write("  * " + sc.getName() + " (" + sc.getServiceBeanName() + ")\r\n");
                                    }
                                }
                            }
                        } else {
                            terminal.write("no checks \r\n");
                        }

                        break;

                    case "services":
                        terminal.write("available probe beans: \r\n");
                        final ClassPathScanningCandidateComponentProvider provider =
                                new ClassPathScanningCandidateComponentProvider(false);
                        provider.addIncludeFilter(new RegexPatternTypeFilter(Pattern.compile(".*")));

                        final Set<BeanDefinition> classes =
                                provider.findCandidateComponents("ru.cwt.devscheck.probe.impl");

                        for (BeanDefinition bean: classes) {
                            terminal.write(" " + bean.getBeanClassName() + "\r\n");
                        }

                        break;

                    default:
                        terminal.write("unknown argument '" + argument + "'. allowed values: host, check\r\n");
                }

                terminal.flush();
            }
        });

        registerCommand("add", new Command() {
            @Override
            public void execute(String name, String argument, EasyTerminal terminal) throws IOException {

                String[] params = StringUtils.split(argument, " ");
                if (params.length == 0) {
                    terminal.write("unknown argument '" + argument + "'. allowed values: host, check\r\n");
                }
                else {

                    switch (params[0]) {
                        case "host":
                            if (params.length < 4) {
                                terminal.write("wrong parameters count. usage: add host name ip type\r\n");
                                terminal.write(" type can be one of: WMWARE,WINDOWS,LINUX,MACOSX,CISCO,IBM_BLADE\r\n");
                            }
                            else {
                                Host h = new Host();
                                h.setName(params[1]);
                                if (StringUtils.contains(params[2], "."))
                                    h.setAddressType(AddressType.IPV4);
                                else
                                    h.setAddressType(AddressType.IPV6);
                                h.setAddress(params[2]);
                                h.setHostType(HostType.valueOf(params[3]));
                                h.setCreateDate(new Date());

                                if (!probeManager.addHost(h)) {
                                    terminal.write("Error adding Host\r\n");
                                }
                            }

                            break;

                        case "check":
                            if (params.length < 3) {
                                terminal.write("wrong parameters count. usage: add check host check-name clazz\r\n");
                            }
                            else {
                                ServiceCheck sc = new ServiceCheck();
                                sc.setName(params[2]);
                                sc.setServiceBeanName(params[3]);
                                sc.setCreateDate(new Date());
                                if (!probeManager.addServiceCheck(params[1], sc)) {
                                    terminal.write("Error adding ServiceCheck\r\n");
                                }
                            }

                            break;

                        case "treshold":
                            if (params.length < 4) {
                                terminal.write("wrong parameters count. usage: add treshold name warning alert\r\n");
                            }
                            else {
                                Treshold t = new Treshold();
                                t.setName(params[2]);
                                t.setWarning(NumberUtils.createDouble(params[3]));
                                t.setAlert(NumberUtils.createDouble(params[4]));
                                if (!probeManager.addTreshold(t)) {
                                    terminal.write("Error adding Treshold\r\n");
                                }
                            }
                            break;

                        default:
                            terminal.write("unknown argument '" + argument + "'. allowed values: host, check\r\n");
                    }
                }

                terminal.flush();
            }
        });

        registerCommand("discovery", new Command() {
            @Override
            public void execute(String name, String argument, EasyTerminal terminal) throws IOException {
                String[] params = StringUtils.split(argument, " ");
                if (params.length < 3) {
                    terminal.write("wrong parameters count. usage: discovery from-ip to-ip ServiceCherk\r\n");
                }
                else {
                    ServiceCheck check = new ServiceCheck("ping", null, null, new HashMap<>(),
                            "ru.cwt.devscheck.probe.impl.PingServiceBean");

                    discoveryManager.scan(params[0], params[1], check);
                }

                terminal.flush();
            }
        });

        try {
            String port = new Config().getProperties().getProperty("shell.port");
            start(Integer.parseInt(port));

            log.info("Start shell server at port {} ", port);
        } catch (Exception e) {
            log.error("Cant init shell server", e);
        }
    }

    public Map<String, Command> getCommands() {
        return commands;
    }

    public void registerCommand(String name, Command command) {
        commands.put(name.toLowerCase(Locale.getDefault()), command);
    }

    public void start(int port) throws IOException {
        if (telnetd == null) {
            EasyTelnetServer srv = new EasyTelnetServer();
            srv.setOnCommandLineListener(commandProcessor);
            srv.start(port);
            telnetd = srv;
        } else {
            throw new IllegalStateException();
        }
    }

    public void stop() throws InterruptedException {
        if (telnetd != null) {
            telnetd.stop();
            telnetd = null;
        } else {
            throw new IllegalStateException();
        }
    }
}
