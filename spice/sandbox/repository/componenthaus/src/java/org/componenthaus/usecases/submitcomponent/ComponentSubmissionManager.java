package org.componenthaus.usecases.submitcomponent;

import org.componenthaus.ant.metadata.ComponentMetadata;

import java.util.Collection;

public interface ComponentSubmissionManager {
    Collection submit(ComponentMetadata componentMetadata) throws ComponentSubmissionException;
}
