package org.componenthaus.tests;

import org.componenthaus.repository.api.Component;
import org.componenthaus.repository.api.ComponentRepository;
import org.componenthaus.repository.api.ServiceImplementation;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MockComponentRepository implements ComponentRepository {
    private Map downloadables = new HashMap();

    public String add(Component component) {
        return null;
    }

    public Collection listComponents() {
        return null;
    }

    public Component getComponent(String id) {
        return null;
    }

    public void registerDownloadable(String componentId, File downloadable) {
    }

    public File getDownloadable(String componentId) {
        return (File) downloadables.get(componentId);
    }

    public ServiceImplementation getImplementation(String componentId, String implId) {
        return null;
    }

    public void setupDownloadable(String id,File file) {
        downloadables.put(id,file);
    }
}
