package jcontainer.impl;

import jcontainer.api.Category;
import jcontainer.api.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

class CategoryImpl implements Category {
    private final String name;
    private final Collection components;

    public CategoryImpl(final String name) {
        components = new ArrayList();
        this.name = name;
    }

    public void addComponent(Component component) {
        components.add(component);
    }

    public String getName() {
        return name;
    }

    public Collection getMatchingComponents(String componentNameRegexp) {
        final Collection result = new ArrayList();
        for(Iterator i=components.iterator();i.hasNext();) {
            final Component component = (Component) i.next();
            if ( component.getName().matches(componentNameRegexp)) {
                result.add(component);
            }

        }
        return result;
    }
}
