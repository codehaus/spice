package org.componenthaus.repository.api;

import org.prevayler.implementation.AbstractPrevalentSystem;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
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
        giveIdsToImplementations(component);
        monitor.componentAdded(component);
        System.out.println("Added component " + component);
        return component.getId();
    }

    private void giveIdsToImplementations(Component component) {
        int index = 0;
        for(Iterator i=component.getImplementations().iterator();i.hasNext();) {
            final ServiceImplementation impl = (ServiceImplementation) i.next();
            assert impl.getId() == null; //I'm in charge of ids
            impl.setId("" + component.getId() + ":" + index++);
            handlePlugs(impl);
        }
    }

    /**
     * Plugs themselves are interfaces, so we get recursive.  This will require some more
     * thought, because some of the plugs might already exist.
     */
    private void handlePlugs(ServiceImplementation impl) {
        for(Iterator i=impl.getPlugs().iterator();i.hasNext();) {
            final Component plug = (Component) i.next();
            add(plug);
        }
    }

    public Collection listComponents() {
        return Collections.unmodifiableCollection(components.values());
    }

    public Component getComponent(String id) {
        return (Component) components.get(id);
    }

    public void registerDownloadable(String componentId, File downloadable) {
        System.out.println("Registering downloadable " + downloadable.getAbsolutePath() + " against component id " + componentId);
        downloadables.put(componentId,downloadable);
    }

    public File getDownloadable(String componentId) {
        return (File) downloadables.get(componentId);
    }

    public ServiceImplementation getImplementation(String componentId, String implId) {
        final Component component = getComponent(componentId);
        return getImpl(component,implId);
    }

    private ServiceImplementation getImpl(Component component, String implId) {
        ServiceImplementation result = null;
        final Iterator i = component.getImplementations().iterator();
        while(result == null && i.hasNext()) {
            final ServiceImplementation current = (ServiceImplementation) i.next();
            if ( implId.equals(current.getId())) {
                result = current;
            }
        }
        return result;
    }
}

