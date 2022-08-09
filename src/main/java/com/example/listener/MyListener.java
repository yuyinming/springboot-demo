package com.example.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class MyListener implements ServletContextListener {

    private static final Logger log = LoggerFactory.getLogger(MyListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        log.info("MyListener Context容器已初始化!");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        log.info("MyListener Context容器已销毁!");
    }
}
