package ru.cwt.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by d.romanovsky on 18/01/16.
 */
public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

        public static void main(String[] args) {
            ApplicationContext context = new ClassPathXmlApplicationContext("spring/beans.xml");
        }
}
