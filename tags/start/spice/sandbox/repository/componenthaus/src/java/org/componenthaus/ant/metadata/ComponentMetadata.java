package org.componenthaus.ant.metadata;

import java.util.Collection;
import java.util.ArrayList;

public class ComponentMetadata {
    private ComponentDefinition definition;
    private Collection implementations;

    public ComponentMetadata() {
        implementations = new ArrayList();
    }

    public ComponentMetadata(ComponentDefinition definition, Collection implementations) {
        this.definition = definition;
        this.implementations = implementations;
    }

    public ComponentDefinition getDefinition() {
        return definition;
    }

    public void setDefinition(ComponentDefinition definition) {
        this.definition = definition;
    }

    public Collection getImplementations() {
        return implementations;
    }

    public void addImplementation(ComponentImplementation impl) {
        implementations.add(impl);
    }
}
