package jcontainer.api;

import jcontainer.api.Component;

public interface ComponentFactory {
    public Component createComponent(String componentName, String version, String theInterface);
}
