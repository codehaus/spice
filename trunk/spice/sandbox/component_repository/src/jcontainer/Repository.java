package jcontainer;

import jcontainer.api.Category;
import jcontainer.api.Component;
import org.prevayler.implementation.AbstractPrevalentSystem;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Repository extends AbstractPrevalentSystem {
    private final Map categories;
    private final Map components;

    public Repository() {
        components = new HashMap();
        categories = new HashMap();
    }

    public boolean contains(Component component) {
        return components.get(component.getName()) != null;
    }

    public void add(final String categoryName,Component component) {
        components.put(component.getName(),component);
        final Category cat = getCategory(categoryName);
        cat.addComponent(component);
    }

    private Category getCategory(String categoryName) {
        return (Category) categories.get(categoryName);
    }

    public Collection getComponents() {
        return Collections.unmodifiableCollection(components.values());
    }

    public Collection findComponent(String categoryName, String componentNameRegexp) {
        final Category category = getCategory(categoryName);
        Collection result = null;
        if ( category != null ) {
            result = category.getMatchingComponents(componentNameRegexp);
        } else {
            result = Collections.EMPTY_LIST;
        }
            return result;
    }

    public boolean containsCategory(String categoryName) {
        return categories.containsKey(categoryName);
    }

    public void addCategory(Category category) {
        categories.put(category.getName(),category);
    }

    public Collection getCategories() {
        return Collections.unmodifiableCollection(categories.values());
    }
}
