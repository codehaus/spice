package org.componenthaus.repository.services;

import org.componenthaus.repository.api.Component;
import org.prevayler.Command;

import java.io.File;

public interface CommandRegistry {
    Command createSubmitComponentCommand(Component component);
    Command createRegisterDownloadableCommand(String componentId, File downloadable);
}
