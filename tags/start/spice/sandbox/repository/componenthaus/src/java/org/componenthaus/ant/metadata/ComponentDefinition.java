package org.componenthaus.ant.metadata;

import java.util.List;

public class ComponentDefinition extends AbstractService {
    private String serviceInterface = null;

    public ComponentDefinition() {
        super();
    }

    public ComponentDefinition(String fullyQualifiedName, String version, List authors, String oneLineDescription, String fullDescription, String serviceInterface) {
        super(fullyQualifiedName,version,authors,oneLineDescription,fullDescription);
        this.serviceInterface = serviceInterface;
    }

    public String getServiceInterface() {
        return serviceInterface;
    }

    public void setServiceInterface(String serviceInterface) {
        this.serviceInterface = serviceInterface;
    }
}
