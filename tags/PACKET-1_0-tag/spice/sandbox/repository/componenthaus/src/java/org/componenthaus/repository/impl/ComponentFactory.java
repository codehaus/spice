package org.componenthaus.repository.impl;

import org.componenthaus.repository.api.Component;

import java.util.List;

public class ComponentFactory {
    public Component createComponent(String fullyQualifiedName, String version, List authors, String oneLineDescription, String fullDescription, String serviceInterface) {
        return new ComponentImpl(fullyQualifiedName,version,authors,oneLineDescription,fullDescription,serviceInterface);
    }
}
