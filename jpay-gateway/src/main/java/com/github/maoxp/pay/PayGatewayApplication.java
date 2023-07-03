package com.github.maoxp.pay;

//import com.github.maoxp.pay.config.printer.StartupRunner;

import com.github.maoxp.core.utils.SpringBeansUtil;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootVersion;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

@SpringBootApplication
@MapperScan("com.github.maoxp.service.mapper")    //Mybatis mapper接口路径
@ComponentScan(basePackages = "com.github.maoxp.**")   // 从路径中找出标识了需要装配的类自动装配到spring的bean容器中
@ConfigurationPropertiesScan
@Slf4j
public class PayGatewayApplication {

    public static void main(String[] args) throws UnknownHostException {

        final ConfigurableApplicationContext application = SpringApplication.run(PayGatewayApplication.class, args);
        log.info("Spring-Boot version: {}", SpringBootVersion.getVersion());
        log.info("项目启动完成 ✅.....");

        Environment env = application.getEnvironment();
        log.info("\n----------------------------------------------------------\n\t" +
                        "应用 '{}' 运行成功! 访问连接:\n\t" +
                        "Swagger文档: \t\thttp://{}:{}{}{}/doc.html\n\t" +
                        "----------------------------------------------------------",
                env.getProperty("spring.application.name"),
                InetAddress.getLocalHost().getHostAddress(),
                env.getProperty("server.port"),
                env.getProperty("server.servlet.context-path", ""),
                env.getProperty("spring.mvc.servlet.path", "")
        );


        final ApplicationContext applicationContext = SpringBeansUtil.getApplicationContext();
        String[] beanNames = applicationContext.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        log.info("项目的容器中 已成功注入的Bean, 如下👇所示!");
        for (String name : beanNames) {
            if (applicationContext.getType(name).getPackage().getName().contains("com.github.maoxp")) {
                log.info(name);
            }
        }
    }

}

