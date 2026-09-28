package com.charlie.serve;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class ServeApplication {

    private static final Logger log = LoggerFactory.getLogger(ServeApplication.class);

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ServeApplication.class, args);
        String port = context.getEnvironment().getProperty("server.port", "8080");
        log.info("Serve 服务启动成功，监听端口 {}，接口地址 http://localhost:{}", port, port);
    }
}
