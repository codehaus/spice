package org.componenthaus.tests;

import org.prevayler.Prevayler;
import org.prevayler.PrevalentSystem;
import org.prevayler.Command;

import java.io.Serializable;

public class MockPrevayler implements Prevayler {
    public PrevalentSystem system() {
        return null;
    }

    public Serializable executeCommand(Command command) throws Exception {
        return null;
    }
}
