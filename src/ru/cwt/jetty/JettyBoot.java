package ru.cwt.jetty;

import java.io.FileInputStream;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.eclipse.jetty.server.NCSARequestLog;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.RequestLogHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.springframework.web.context.ContextLoaderListener;

public class JettyBoot {
	private static Logger log = Logger.getLogger(JettyBoot.class);
	private static final String PROPERTY_NAME = "conf/ptconfig.properties";

	public static void main(String[] args) throws Exception {
		Properties props = new Properties();
		props.load(new FileInputStream(PROPERTY_NAME));

		String host = props.getProperty("http.host", "0.0.0.0");
		int port = Integer.parseInt(props.getProperty("http.port", "8080"));

		Server server = new Server();
		ServerConnector connector = new ServerConnector(server);
		connector.setPort(port);
		connector.setHost(host);

		server.addConnector(connector);

		HandlerCollection hc = new HandlerCollection();

		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.addEventListener(new ContextLoaderListener());
		context.setInitParameter("contextConfigLocation", "classpath:/spring/spring-context.xml");
		context.setContextPath("");
		hc.addHandler(context);

		NCSARequestLog ncsaLog = new NCSARequestLog();
		ncsaLog.setExtended(false);
		ncsaLog.setFilename("logs/access-log-yyyy_mm_dd.log");
		RequestLogHandler logHandler = new RequestLogHandler();
		logHandler.setRequestLog(ncsaLog);
		hc.addHandler(logHandler);

		// write a pid into file in current firectory
		PrintWriter writer = new PrintWriter("platform.pid", "UTF-8");
		writer.println(getProcessId());
		writer.close();

		server.setHandler(hc);
		server.start();
		server.join();
	}

	private static String getProcessId() {
		String fallback;
		// Note: may fail in some JVM implementations
		// therefore fallback has to be provided

		// something like '<pid>@<hostname>', at least in SUN / Oracle JVMs
		final String jvmName = ManagementFactory.getRuntimeMXBean().getName();
		final int index = jvmName.indexOf('@');

		try {
			return Long.toString(Long.parseLong(jvmName.substring(0, index)));
		} catch (NumberFormatException e) {
			// ignore
			return null;
		}
	}
}
