package org.componenthaus.repository.services;

import org.componenthaus.repository.api.Component;
import org.prevayler.Command;

public interface CommandRegistry {
    Command createSubmitComponentCommand(Component component);
}
