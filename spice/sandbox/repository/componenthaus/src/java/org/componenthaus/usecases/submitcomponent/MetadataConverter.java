package org.componenthaus.usecases.submitcomponent;

import org.componenthaus.ant.metadata.ComponentMetadata;

import java.util.Collection;

public interface MetadataConverter {
    Collection convert(ComponentMetadata componentMetadata);
}
