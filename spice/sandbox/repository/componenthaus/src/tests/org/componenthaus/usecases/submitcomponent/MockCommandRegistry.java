package org.componenthaus.usecases.submitcomponent;

import org.componenthaus.repository.services.CommandRegistry;
import org.componenthaus.repository.api.Component;
import org.prevayler.Command;
import org.prevayler.PrevalentSystem;
import junit.framework.Assert;

import java.io.Serializable;

public class MockCommandRegistry implements CommandRegistry {
    private int expectedSubmitComponentCalls;
    private int actualSubmitComponentCalls;

    public void setupExpectedSubmitComponentCalls(int i) {
        this.expectedSubmitComponentCalls = i;
    }

    public Command createSubmitComponentCommand(Component component) {
        actualSubmitComponentCalls++;
        return new Command() {
            public Serializable execute(PrevalentSystem prevalentSystem) throws Exception {
                return null;
            }
        };
    }

    public void verify() {
        Assert.assertEquals(expectedSubmitComponentCalls,actualSubmitComponentCalls);
    }
}
