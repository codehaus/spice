package org.componenthaus.repository.api;

import java.io.File;
import java.util.Collection;

public interface ComponentRepository {
    public String add(Component component);
    Collection listComponents();
    Component getComponent(String id);
    void registerDownloadable(String componentId, File downloadable);
    File getDownloadable(String componentId);
    ServiceImplementation getImplementation(String componentId, String implId);

    public static interface Monitor {
        public void componentAdded(final Component component);
    }
}
