package org.componenthaus.repository.services;

import org.prevayler.Command;
import org.prevayler.PrevalentSystem;
import org.componenthaus.repository.api.ComponentRepository;

import java.io.Serializable;
import java.io.File;

public class RegisterDownloadableComponentCommand implements Command {
    private final String componentId;
    private final File downloadable;

    public RegisterDownloadableComponentCommand(String componentId, File downloadable) {
        this.componentId = componentId;
        this.downloadable = downloadable;
    }

    public Serializable execute(PrevalentSystem prevalentSystem) throws Exception {
        final ComponentRepository repository = (ComponentRepository) prevalentSystem;
        repository.registerDownloadable(componentId,downloadable);
        return null;
    }
}
