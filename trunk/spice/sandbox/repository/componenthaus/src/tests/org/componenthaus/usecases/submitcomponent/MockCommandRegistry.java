package org.componenthaus.usecases.submitcomponent;

import org.componenthaus.repository.services.CommandRegistry;
import org.componenthaus.repository.api.Component;
import org.prevayler.Command;
import org.prevayler.PrevalentSystem;
import junit.framework.Assert;

import java.io.Serializable;
import java.io.File;
import java.util.Iterator;
import java.util.Collection;
import java.util.ArrayList;

public class MockCommandRegistry implements CommandRegistry {
    private int expectedSubmitComponentCalls;
    private int actualSubmitComponentCalls;
    private int actualRegisterDownloadableCalls;
    private int expectedRegisterDownloadableCalls;
    private Collection expectedAddComponents = new ArrayList();
    private Collection actualAddComponents = new ArrayList();
    private boolean addComponentsExpected = false;

    public void setupExpectedSubmitComponentCalls(int i) {
        this.expectedSubmitComponentCalls = i;
    }

    public Command createSubmitComponentCommand(Component component) {
        actualSubmitComponentCalls++;
        component.setId("1");
        actualAddComponents.add(component);
        return new Command() {
            public Serializable execute(PrevalentSystem prevalentSystem) throws Exception {
                return null;
            }
        };
    }

    public void setupExpectedAddComponent(Component component) {
        this.expectedAddComponents.add(component);
        addComponentsExpected = true;
    }

    public Command createRegisterDownloadableCommand(String componentId, File downloadable) {
        actualRegisterDownloadableCalls++;
        return new Command() {
            public Serializable execute(PrevalentSystem prevalentSystem) throws Exception {
                return null;
            }
        };
    }

    public void verify() {
        Assert.assertEquals(expectedSubmitComponentCalls, actualSubmitComponentCalls);
        Assert.assertEquals(expectedRegisterDownloadableCalls, actualRegisterDownloadableCalls);
        if (addComponentsExpected) {
            Assert.assertEquals(expectedAddComponents.size(), actualAddComponents.size());
            for (Iterator i = expectedAddComponents.iterator(); i.hasNext();) {
                Assert.assertTrue(actualAddComponents.contains(i.next()));
            }
        }
    }

    public void setupExpectedRegisterDownloadableCalls(int i) {
        expectedRegisterDownloadableCalls = i;
    }
}
