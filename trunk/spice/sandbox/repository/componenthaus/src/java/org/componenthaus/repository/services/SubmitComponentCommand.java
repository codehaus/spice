package org.componenthaus.repository.services;

import org.prevayler.Command;
import org.prevayler.PrevalentSystem;
import org.componenthaus.repository.api.Component;
import org.componenthaus.repository.api.ComponentRepository;

import java.io.Serializable;

class SubmitComponentCommand implements Command {
    private final Component component;

    public SubmitComponentCommand(final Component component) {
        this.component = component;
    }

    public Serializable execute(PrevalentSystem prevalentSystem) throws Exception {
        final ComponentRepository repository = (ComponentRepository) prevalentSystem;
        System.out.println("repository = " + repository);
        int numComponents = repository.listComponents().size();
        final String result = repository.add(component);
        assert numComponents < repository.listComponents().size();
        return result;
    }
}
