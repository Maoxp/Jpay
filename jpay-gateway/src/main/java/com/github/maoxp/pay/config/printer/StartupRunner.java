package com.github.maoxp.pay.config.printer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;

import java.util.Arrays;


/**
 * 运行容器时打印已定义的bean
 */
@Order(1)
public class StartupRunner implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(StartupRunner.class);

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void run(String... args) {
        logger.info("startup runner");
        String[] beanNames = applicationContext.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        for (String name : beanNames) {
            logger.info(name);
        }
    }

}
