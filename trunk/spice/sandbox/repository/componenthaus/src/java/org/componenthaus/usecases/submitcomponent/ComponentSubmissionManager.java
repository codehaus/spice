package org.componenthaus.usecases.submitcomponent;

import org.componenthaus.ant.metadata.ComponentMetadata;

public interface ComponentSubmissionManager {
    void submit(ComponentMetadata componentMetadata) throws ComponentSubmissionException;
}
