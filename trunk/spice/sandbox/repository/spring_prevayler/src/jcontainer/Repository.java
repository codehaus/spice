package jcontainer;

import org.prevayler.implementation.AbstractPrevalentSystem;

import java.util.Map;
import java.util.HashMap;
import java.util.Collection;
import java.util.Collections;

public class Repository extends AbstractPrevalentSystem {
    private final Map components;

    public Repository() {
        components = new HashMap();
    }

    public boolean contains(Component component) {
        return components.get(component.getName()) != null;
    }

    public void add(Component component) {
        components.put(component.getName(),component);
    }

    public Collection getComponents() {
        return Collections.unmodifiableCollection(components.values());
    }
}
