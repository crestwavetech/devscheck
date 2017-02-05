package ru.cwt.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author e.chertikhin
 * @date 26/01/2017
 * <p>
 * Copyright (c) 2017 CrestWave technologies LLC. All right reserved.
 */
public class Config {
    private static final Logger log = LoggerFactory.getLogger(Config.class);

    private static Properties props = new Properties();

    public Properties getProperties() {
        try {
            InputStream is = new FileInputStream("conf/ptconfig.properties");
            props.load(is);
        } catch (FileNotFoundException e) {
            log.error("No properties file found", e);
        } catch (IOException e) {
            log.error("CheckStatus during reading properties", e);
        }
        return props;
    }
}
