package org.componenthaus.repository.api;

import org.prevayler.implementation.AbstractPrevalentSystem;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RepositoryImpl extends AbstractPrevalentSystem implements ComponentRepository {
    private final Map components;
    private final Map downloadables;
    private final ComponentRepository.Monitor monitor;

    public RepositoryImpl(final ComponentRepository.Monitor monitor) {
        components = new HashMap();
        downloadables = new HashMap();
        this.monitor = monitor;
    }

    public String add(Component component) {
        assert component != null;
        assert component.getId() == null; //I'm in charge of ids
        component.setId(""+components.size());
        components.put(component.getId(),component);
        monitor.componentAdded(component);
        return component.getId();
    }

    public Collection listComponents() {
        return Collections.unmodifiableCollection(components.values());
    }

    public Component getComponent(String id) {
        return (Component) components.get(id);
    }

    public void registerDownloadable(String componentId, File downloadable) {
        downloadables.put(componentId,downloadable);
    }

    public File getDownloadable(String componentId) {
        return (File) downloadables.get(componentId);
    }
}

