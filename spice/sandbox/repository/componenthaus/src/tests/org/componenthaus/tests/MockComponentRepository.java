package org.componenthaus.tests;

import junit.framework.Assert;
import org.componenthaus.repository.api.Component;
import org.componenthaus.repository.api.ComponentRepository;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MockComponentRepository implements ComponentRepository {
    private Map downloadables = new HashMap();
    private Map components = new HashMap();
    private Collection expectedAddComponents = new ArrayList();
    private Collection actualAddComponents = new ArrayList();

    public String add(Component component) {
        return null;
    }

    public Collection listComponents() {
        return null;
    }

    public Component getComponent(String id) {
        return (Component) components.get(id);
    }

    public void registerDownloadable(String componentId, File downloadable) {
    }

    public File getDownloadable(String componentId) {
        return (File) downloadables.get(componentId);
    }

    public void setupDownloadable(String id,File file) {
        downloadables.put(id,file);
    }

    public void setupComponent(Component component) {
        components.put(component.getId(),component);
    }

    public void setupExpectedAddComponent(Component component) {
        this.expectedAddComponents.add(component);
    }

    public void verify() {
        Assert.assertEquals(expectedAddComponents.size(),actualAddComponents.size());
        for(Iterator i=expectedAddComponents.iterator();i.hasNext();) {
            Assert.assertTrue(actualAddComponents.contains(i.next()));
        }
    }
}
