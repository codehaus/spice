package jcontainer.impl;

import jcontainer.api.Component;
import jcontainer.api.Category;
import jcontainer.api.ComponentFactory;
import jcontainer.api.CategoryFactory;

public class JContainerFactory implements CategoryFactory, ComponentFactory {
    public Component createComponent(String componentName, String version, String theInterface) {
        return new ComponentImpl(componentName, version, theInterface);
    }

    public Category createCategory(String categoryName) {
        return new CategoryImpl(categoryName);
    }
}
