package ru.cwt.jetty;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.GenericWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 * @author e.chertikhin
 * @date 26/01/2017
 * <p>
 * Copyright (c) 2017 CrestWave technologies LLC. All right reserved.
 */
public class JettyContextStarter {
    private static final Logger log = LoggerFactory.getLogger(JettyContextStarter.class);

    private static final String CONTEXT_PATH = "/";

    private String classPathResource;
    private String jettyHost;
    private String jettyPort;

    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    private void startJetty() throws Exception {
        Server server = new Server();
        ServerConnector connetor = new ServerConnector(server);

        connetor.setHost(jettyHost);
        connetor.setPort(Integer.parseInt(jettyPort));

        server.addConnector(connetor);

        server.setHandler(getServletContextHandler(getContext()));
        log.info("Starting context from : " + classPathResource);

        server.start();
    }

    private ServletContextHandler getServletContextHandler(WebApplicationContext context) throws IOException {
        ServletContextHandler contextHandler = new ServletContextHandler();
        contextHandler.setErrorHandler(null);
        contextHandler.setContextPath(CONTEXT_PATH);

        contextHandler.addServlet(new ServletHolder(new DispatcherServlet(context)), "/*");
        contextHandler.addEventListener(new ContextLoaderListener(context));
        return contextHandler;
    }

    private WebApplicationContext getContext() {
        GenericWebApplicationContext context = new GenericWebApplicationContext();
        context.setParent(applicationContext);
        XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader(context);
        xmlReader.loadBeanDefinitions(new ClassPathResource(classPathResource));

        return context;
    }

    public String getClassPathResource() {
        return classPathResource;
    }

    public void setClassPathResource(String classPathResource) {
        this.classPathResource = classPathResource;
    }

    public String getJettyHost() {
        return jettyHost;
    }

    public void setJettyHost(String jettyHost) {
        this.jettyHost = jettyHost;
    }

    public String getJettyPort() {
        return jettyPort;
    }

    public void setJettyPort(String jettyPort) {
        this.jettyPort = jettyPort;
    }
}
