package org.componenthaus.repository.api;

import junit.framework.TestCase;
import org.componenthaus.tests.MockComponentRepositoryMonitor;
import org.componenthaus.tests.MockComponent;

import java.io.File;

public class RepositoryImplTestCase extends TestCase {
    private RepositoryImpl repository = null;
    private MockComponentRepositoryMonitor monitor = null;

    protected void setUp() throws Exception {
        monitor = new MockComponentRepositoryMonitor();
        repository = new RepositoryImpl(monitor);
    }

    public void testCanAddComponent() {
        MockComponent component = new MockComponent(null);
        monitor.setupExpectedComponent(component);
        String newId = repository.add(component);
        assertEquals("0", newId);
        assertEquals(newId,component.getId());
        assertSame(component,repository.getComponent(newId));
        monitor.verify();
    }

    public void testCanListComponents() {
        assertEquals(0,repository.listComponents().size());
        final MockComponent component = new MockComponent(null);
        repository.add(component);
        assertEquals(1,repository.listComponents().size());
        assertTrue(repository.listComponents().contains(component));
    }

    public void testCanGetComponent() {
        String componentId = "0";
        assertNull(repository.getComponent(componentId));
        final MockComponent component = new MockComponent(null);
        repository.add(component);
        assertSame(component,repository.getComponent(componentId));
    }

    public void testCanRegisterAndRetrieveDownloadables() {
        String componentId = "0";
        assertNull(repository.getDownloadable(componentId));
        File file = new File("");
        repository.registerDownloadable(componentId,file);
        assertSame(file,repository.getDownloadable(componentId));
    }


}
