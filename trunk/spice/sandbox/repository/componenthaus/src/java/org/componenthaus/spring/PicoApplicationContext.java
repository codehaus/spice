package org.componenthaus.spring;

import org.componenthaus.usecases.welcomeuser.WelcomeUserController;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;

import java.util.Properties;

public class PicoApplicationContext extends AbstractPicoApplicationContext {
    public PicoApplicationContext(ApplicationContext parent, String nameSpace) {
        super(parent, nameSpace);
    }

    protected void registerBeans() throws BeansException {
        SimpleUrlHandlerMapping mapping = new UrlHandlerMapping();

        final Properties mappings = new Properties();
        mappings.put("/welcome.htm","welcomeController");
        mappings.put("/componenthaus/welcome.htm","welcomeController");
        mapping.setUrlMap(mappings);

        pico.registerComponentInstance("urlMapping",mapping);
        pico.registerComponentInstance("welcomeController",new WelcomeUserController());
        mapping.setApplicationContext(this);
    }

    public static final class UrlHandlerMapping extends SimpleUrlHandlerMapping {
        public void initApplicationContext() {
            System.out.println("INIT APP");
            super.initApplicationContext();
        }
    }
}
