package org.componenthaus.repository.impl;

import org.componenthaus.repository.api.ServiceImplementation;

import java.util.List;

public class ServiceImplementationFactory {
    public ServiceImplementation createServiceImplementation(String fullyQualifiedName, String version, List authors, String oneLineDescription, String fullDescription) {
        return new ServiceImplementationImpl(fullyQualifiedName,version,authors,oneLineDescription,fullDescription);
    }
}
