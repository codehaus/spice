package org.componenthaus.tests;

import org.componenthaus.repository.api.ComponentRepository;
import org.componenthaus.repository.api.Component;
import junit.framework.Assert;

public class MockComponentRepositoryMonitor implements ComponentRepository.Monitor {
    private Component expectedComponent;
    private Component actualComponent;

    public void componentAdded(Component component) {
        actualComponent = component;
    }

    public void setupExpectedComponent(Component component) {
        this.expectedComponent = component;
    }

    public void verify() {
        Assert.assertSame(expectedComponent,actualComponent);
    }
}
