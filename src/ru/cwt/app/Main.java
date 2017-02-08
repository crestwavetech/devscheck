package ru.cwt.app;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by d.romanovsky on 18/01/16.
 */
public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

        public static void main(String[] args) {
            Properties p = new Properties();

            try {
                InputStream is = new FileInputStream("conf/ptconfig.properties");
                p.load(is);

                if (StringUtils.isNotEmpty(p.getProperty("spring.context"))) {
                    ApplicationContext context = new ClassPathXmlApplicationContext(p.getProperty("spring.context"));
                }
                else {
                    log.error("No spring.context found in config");
                }

            } catch (FileNotFoundException e) {
                log.error("No properties file found");
            } catch (IOException e) {
                log.error("Error during reading properties");
            }
        }
}
