package org.componenthaus.prevayler;

import org.prevayler.PrevalentSystem;
import org.prevayler.Prevayler;
import org.prevayler.Command;
import org.prevayler.implementation.SnapshotPrevayler;
import org.componenthaus.repository.api.ComponentRepository;

import java.io.IOException;
import java.io.Serializable;

public class PrevaylerBean implements Prevayler {
    private Prevayler delegate = null;

    public PrevaylerBean(final ComponentRepository repo) throws ClassNotFoundException, IOException {
        delegate = new SnapshotPrevayler((PrevalentSystem) repo);
    }

    public PrevalentSystem system() {
        return delegate.system();
    }

    public Serializable executeCommand(Command command) throws Exception {
        return delegate.executeCommand(command);
    }
}