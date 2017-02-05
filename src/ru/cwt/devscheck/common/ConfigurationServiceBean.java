package ru.cwt.devscheck.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author e.chertikhin
 * @date 05/02/2017
 * <p>
 * Copyright (c) 2017 CrestWave technologies LLC. All right reserved.
 */
@Configuration
public class ConfigurationServiceBean {
    private static final Logger log = LoggerFactory.getLogger(ConfigurationServiceBean.class);

    Properties props;

    @PostConstruct
    private void init() {
        props = new Properties();

        try {
            InputStream is = new FileInputStream("conf/ptconfig.properties");
            props.load(is);

        } catch (FileNotFoundException e) {
            log.error("No properties file found");
        } catch (IOException e) {
            log.error("Error during reading properties");
        }
    }

    public String getProps(String key) {
        return props.getProperty(key);
    }

    public String getProps(String key, String defaultValue) {
        return props.getProperty(key, defaultValue);
    }
}
