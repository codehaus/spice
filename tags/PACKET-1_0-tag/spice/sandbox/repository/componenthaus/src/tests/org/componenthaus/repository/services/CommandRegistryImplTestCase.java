package org.componenthaus.repository.services;

import junit.framework.TestCase;
import org.componenthaus.tests.MockComponent;

import java.io.File;

public class CommandRegistryImplTestCase extends TestCase {
    private CommandRegistry commandRegistry = null;

    protected void setUp() throws Exception {
        commandRegistry = new CommandRegistryImpl();
    }

    public void testCanCreateSubmitComponentCommand() {
        assertNotNull(commandRegistry.createSubmitComponentCommand(new MockComponent("1")));
    }

    public void testCanCreateRegisterDownloadableCommand() {
        assertNotNull(commandRegistry.createRegisterDownloadableCommand("1",new File("")));
    }
}
