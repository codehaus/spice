package org.componenthaus.tests;

import org.componenthaus.usecases.submitcomponent.ComponentSubmissionManager;
import org.componenthaus.usecases.submitcomponent.ComponentSubmissionException;
import org.componenthaus.ant.metadata.ComponentMetadata;

import java.util.Collection;

public class MockComponentSubmissionManager implements ComponentSubmissionManager{
    private Collection setupResult;

    public Collection submit(ComponentMetadata componentMetadata) throws ComponentSubmissionException {
        return setupResult;
    }



    public void setSetupResult(Collection setupResult) {
        this.setupResult = setupResult;
    }
}
