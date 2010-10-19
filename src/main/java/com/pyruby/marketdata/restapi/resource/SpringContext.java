package com.pyruby.marketdata.restapi.resource;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringContext {
    private static SpringContext ctx;
    private ClassPathXmlApplicationContext springCtx;

    private SpringContext() {
        springCtx = new ClassPathXmlApplicationContext("database.xml", "service.xml");
    }

    public static synchronized SpringContext getInstance() {
        if (ctx == null) {
            ctx = new SpringContext();
        }
        return ctx;
    }

    public Object get(String component) {
        return springCtx.getBean(component);
    }
}
