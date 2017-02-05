package ru.cwt.jetty;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.jetty.jmx.MBeanContainer;
import org.eclipse.jetty.server.NCSARequestLog;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.RequestLogHandler;
import org.eclipse.jetty.webapp.Configuration.ClassList;
import org.eclipse.jetty.webapp.WebAppContext;

import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.util.Properties;

public class JettyBoot {
	private static Logger log = Logger.getLogger(JettyBoot.class);
	private static final String PROPERTY_NAME = "conf/ptconfig.properties";

	public static void main(String[] args) throws Exception {
		Properties props = new Properties();
		props.load(new FileInputStream(PROPERTY_NAME));

		if (StringUtils.isNotBlank(props.getProperty("app.profile"))) {
			System.setProperty("spring.profiles.active", props.getProperty("app.profile"));
		} else {
			System.setProperty("spring.profiles.active", "jetty");
		}
		log.info("spring.profiles.active : " + System.getProperty("spring.profiles.active"));

		String host = props.getProperty("http.host", "0.0.0.0");
		int port = Integer.parseInt(props.getProperty("http.port", "8080"));

		// Server setup
		Server server = new Server();

		ServerConnector connector = new ServerConnector(server);
		connector.setPort(port);
		connector.setHost(host);

		server.addConnector(connector);
		server.addBean(new JettyErrorHandler());

		ClassList classlist = ClassList.setServerDefault(server);

		classlist.addAfter("org.eclipse.jetty.webapp.FragmentConfiguration", "org.eclipse.jetty.plus.webapp.EnvConfiguration", "org.eclipse.jetty.plus.webapp.PlusConfiguration");

		classlist.addBefore("org.eclipse.jetty.webapp.JettyWebXmlConfiguration", "org.eclipse.jetty.annotations.AnnotationConfiguration");

		MBeanContainer mbContainer = new MBeanContainer(ManagementFactory.getPlatformMBeanServer());
		server.addBean(mbContainer);

		File temp = new File("tmp");
		temp.mkdirs();

		WebAppContext webapp = new WebAppContext();
		webapp.setErrorHandler(new JettyErrorPageHandler());
		webapp.setContextPath(props.getProperty("app.context"));
		webapp.setWar(props.getProperty("app.package"));
		webapp.setTempDirectory(temp);

		// autocommit - move to property?
		Properties connProps = new Properties();
		connProps.put("autoCommit", "false");

		// DB pool
//		int poolMaxSize = Integer.parseInt(props.getProperty("db.pool.max", "15"));
//		int poolMinSize = Integer.parseInt(props.getProperty("db.pool.min", "5"));
//
//		// Connection Pool
//		PoolDataSource pds = PoolDataSourceFactory.getPoolDataSource();
//		pds.setUser(props.getProperty("db.user"));
//		pds.setPassword(props.getProperty("db.pass"));
//		pds.setURL(props.getProperty("db.url"));
//		pds.setConnectionFactoryClassName("oracle.jdbc.pool.OracleDataSource");
//		pds.setInitialPoolSize(poolMinSize);
//		pds.setMinPoolSize(poolMinSize);
//		pds.setMaxPoolSize(poolMaxSize);
//		pds.setValidateConnectionOnBorrow(true);
//		pds.setSQLForValidateConnection("select 1 from dual");
//		pds.setConnectionProperties(connProps);
//
//		//
//
//		while (true) {
//			Connection c = null;
//			try {
//				c = pds.getConnection();
//			} catch (Exception e) {
//				log.error("DATABASE VALIDATE ERROR", e);
//				Thread.sleep(5000);
//				continue;
//			} finally {
//				if (c != null) {
//					c.close();
//				}
//			}
//
//			break;
//		}
//
//		Resource mydatasource = new Resource(webapp, "jdbc/PaymentGateDSnoJTA", pds);

		// Logging in application
		NCSARequestLog ncsaLog = new NCSARequestLog();
		ncsaLog.setExtended(false);
		ncsaLog.setFilename("logs/access-log-yyyy_mm_dd.log");

		RequestLogHandler logHandler = new RequestLogHandler();
		logHandler.setRequestLog(ncsaLog);

		HandlerCollection hc = new HandlerCollection();
		hc.addHandler(webapp);
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
