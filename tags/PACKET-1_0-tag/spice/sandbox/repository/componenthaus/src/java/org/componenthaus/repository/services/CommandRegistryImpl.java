package org.componenthaus.repository.services;

import org.prevayler.Command;
import org.componenthaus.repository.api.Component;

import java.io.File;

public class CommandRegistryImpl implements CommandRegistry {
    public Command createSubmitComponentCommand(Component component) {
        return new SubmitComponentCommand(component);
    }

    public Command createRegisterDownloadableCommand(String componentId, File downloadable) {
        return new RegisterDownloadableComponentCommand(componentId,downloadable);
    }
}
