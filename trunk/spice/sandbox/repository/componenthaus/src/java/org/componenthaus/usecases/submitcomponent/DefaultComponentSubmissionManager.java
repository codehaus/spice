package org.componenthaus.usecases.submitcomponent;

import org.componenthaus.ant.metadata.ComponentMetadata;
import org.componenthaus.repository.services.CommandRegistry;
import org.componenthaus.repository.api.Component;
import org.prevayler.Prevayler;

import java.util.Collection;
import java.util.Iterator;

public class DefaultComponentSubmissionManager implements ComponentSubmissionManager {
    private final MetadataConverter metadataConverter;
    private final Prevayler prevayler;
    private CommandRegistry commandRegistry;

    public DefaultComponentSubmissionManager(MetadataConverter metadataConverter, Prevayler prevayler, CommandRegistry commandRegistry) {
        this.metadataConverter = metadataConverter;
        this.prevayler = prevayler;
        this.commandRegistry = commandRegistry;
    }

    public void submit(ComponentMetadata componentMetadata) throws ComponentSubmissionException {
        if ( componentMetadata == null ) {
            throw new IllegalArgumentException("componentMetadata cannot be null");
        }
        if ( componentMetadata.numInterfaces() <= 0 ) {
            throw new IllegalArgumentException("componentMetadata must contain at least one service interface");
        }
        Collection components = metadataConverter.convert(componentMetadata);
        for(Iterator i=components.iterator();i.hasNext();) {
            try {
                prevayler.executeCommand(commandRegistry.createSubmitComponentCommand((Component)i.next()));
            } catch (Exception e) {
                throw new ComponentSubmissionException("Exception submitting component",e);
            }
        }
    }
}
