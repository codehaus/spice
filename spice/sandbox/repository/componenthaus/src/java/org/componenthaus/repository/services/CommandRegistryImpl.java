package org.componenthaus.repository.services;

import org.prevayler.Command;
import org.componenthaus.repository.api.Component;

public class CommandRegistryImpl implements CommandRegistry {
    public Command createSubmitComponentCommand(Component component) {
        return new SubmitComponentCommand(component);
    }
}
