package org.componenthaus.prevayler;

import junit.framework.TestCase;
import org.componenthaus.tests.MockComponentRepository;
import org.prevayler.Command;
import org.prevayler.PrevalentSystem;

import java.io.Serializable;

public class PrevaylerBeanTestCase extends TestCase {
    private PrevaylerBean bean = null;
    private MockComponentRepository repo = null;

    protected void setUp() throws Exception {
        repo = new MockComponentRepository();
        bean = new PrevaylerBean(repo);
    }

    public void testSystem() {
        assertSame(repo, bean.system());
    }

    public void testCanExecuteCommand() throws Exception {
        String preparedReturn = "jdladj";
        final MockCommand command = new MockCommand(preparedReturn);
        bean.executeCommand(command);
        command.verify();
    }

    private static final class MockCommand implements Command {
        private String preparedReturn = null;
        private boolean called = false;

        public MockCommand(String preparedReturn) {
            this.preparedReturn = preparedReturn;
        }

        public Serializable execute(PrevalentSystem prevalentSystem) throws Exception {
            called = true;
            return preparedReturn;
        }

        public void verify() {
            assertTrue(called);
        }
    }
}
