package org.componenthaus.tests;

import org.componenthaus.ant.metadata.ComponentMetadata;
import org.componenthaus.usecases.submitcomponent.MetadataConverter;

import java.util.Collection;

public class MockMetadataConverterImpl implements MetadataConverter {
    private Collection setupResult;

    public void setSetupResult(Collection setupResult) {
        this.setupResult = setupResult;
    }

    public Collection convert(ComponentMetadata componentMetadata) {
        return setupResult;
    }
}
