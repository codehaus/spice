package org.componenthaus.tests;

import junit.framework.Assert;
import org.componenthaus.repository.api.Component;
import org.componenthaus.repository.api.ComponentRepository;
import org.prevayler.AlarmClock;
import org.prevayler.implementation.AbstractPrevalentSystem;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Collections;

public class MockComponentRepository extends AbstractPrevalentSystem implements ComponentRepository  {
    private Map downloadables = new HashMap();
    private Map components = new HashMap();
    private Collection expectedAddComponents = new ArrayList();
    private Collection actualAddComponents = new ArrayList();
    private String expectedRegisterDownloadableComponentId = null;
    private File expectedRegisterDownloadableFile = null;
    private String actualRegisterDownloadableComponentId = null;
    private File actualRegisterDownloadableFile = null;
    private String preparedResult = null;
    private int expectedListComponentsCalls = -1;
    private int actualListComponentsCalls = 0;

    public String add(Component component) {
        actualAddComponents.add(component);
        return preparedResult;
    }

    public Collection listComponents() {
        actualListComponentsCalls++;
        return Collections.EMPTY_LIST;
    }

    public Component getComponent(String id) {
        return (Component) components.get(id);
    }

    public void registerDownloadable(String componentId, File downloadable) {
        this.actualRegisterDownloadableComponentId = componentId;
        this.actualRegisterDownloadableFile = downloadable;
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

    public void setupExpectedAddComponent(Component component, String preparedResult) {
        this.expectedAddComponents.add(component);
        this.preparedResult = preparedResult;
    }

    public void verify() {
        Assert.assertEquals(expectedAddComponents.size(),actualAddComponents.size());
        for(Iterator i=expectedAddComponents.iterator();i.hasNext();) {
            Assert.assertTrue(actualAddComponents.contains(i.next()));
        }
        if ( expectedRegisterDownloadableComponentId != null ) {
            Assert.assertEquals(expectedRegisterDownloadableComponentId, actualRegisterDownloadableComponentId);
            Assert.assertSame(expectedRegisterDownloadableFile, actualRegisterDownloadableFile);
        }
        if ( expectedListComponentsCalls >= 0) {
            Assert.assertEquals(expectedListComponentsCalls, actualListComponentsCalls);
        }
    }

    public void setupExpectedRegisterDownloadable(String componentId, File file) {
        this.expectedRegisterDownloadableComponentId = componentId;
        this.expectedRegisterDownloadableFile = file;
    }

    public void setupExpectedListComponentsCalls(int num) {
        expectedListComponentsCalls = num;
    }

}
