package org.componenthaus.repository.api;

import java.util.Collection;

/**
 * Represents a category of Components.  A Category has a name and can refer to many
 * Components.  Similarly, a component can be referred to by many Categories.
 *
 * @version 1.0
 * @author Mike Hogan
 */
public interface Category {
    void addComponent(Component component);

    Collection getMatchingComponents(String componentNameRegexp);

    String getName();
}
