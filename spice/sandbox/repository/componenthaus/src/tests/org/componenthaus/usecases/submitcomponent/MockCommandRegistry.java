package org.componenthaus.usecases.submitcomponent;

import org.componenthaus.repository.services.CommandRegistry;
import org.componenthaus.repository.api.Component;
import org.prevayler.Command;
import org.prevayler.PrevalentSystem;
import junit.framework.Assert;

import java.io.Serializable;
import java.io.File;

public class MockCommandRegistry implements CommandRegistry {
    private int expectedSubmitComponentCalls;
    private int actualSubmitComponentCalls;
    private int actualRegisterDownloadableCalls;
    private int expectedRegisterDownloadableCalls;

    public void setupExpectedSubmitComponentCalls(int i) {
        this.expectedSubmitComponentCalls = i;
    }

    public Command createSubmitComponentCommand(Component component) {
        actualSubmitComponentCalls++;
        component.setId("1");
        return new Command() {
            public Serializable execute(PrevalentSystem prevalentSystem) throws Exception {
                return null;
            }
        };
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
        Assert.assertEquals(expectedSubmitComponentCalls,actualSubmitComponentCalls);
        Assert.assertEquals(expectedRegisterDownloadableCalls,actualRegisterDownloadableCalls);
    }

    public void setupExpectedRegisterDownloadableCalls(int i) {
        expectedRegisterDownloadableCalls = i;
    }
}
