package org.componenthaus.repository.impl;

import org.componenthaus.repository.api.Category;
import org.componenthaus.repository.api.Component;

import java.util.Collection;

/**
 * Represents a category of Components.  A Category has a name and can refer to many
 * Components.  Similarly, a component can be referred to by many Categories.
 *
 * @version 1.1
 * @author Mike Hogan
 */
class CategoryImpl implements Category {
    public CategoryImpl(Component component, Category category) {
        // just for test purposes
    }

    public void addComponent(Component component) {
    }

    public Collection getMatchingComponents(String componentNameRegexp) {
        return null;
    }

    public String getName() {
        return null;
    }
}
