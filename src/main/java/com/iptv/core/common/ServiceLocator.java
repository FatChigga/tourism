package com.iptv.core.common;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


public class ServiceLocator implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {

        return applicationContext;

    }

    public static Object getService(String servName) {

        servName = String.format("%s%s", new Object[]{servName.substring(0, 1).toLowerCase(), servName.substring(1)});
        System.out.print(applicationContext);

        try {

            return applicationContext.getBean(servName);

        } catch (Exception e) {
        }

        return applicationContext.getBean(servName + "Impl");

    }

    public static Object getService(String servName, Class clazz) {

        servName = String.format("%s%s", new Object[]{servName.substring(0, 1).toLowerCase(), servName.substring(1)});

        try {

            return applicationContext.getBean(servName, clazz);

        } catch (Exception e) {
        }

        return applicationContext.getBean(servName + "Impl", clazz);

    }

}
