package org.componenthaus.ant.metadata;

import org.componenthaus.ant.metadata.AbstractService;
import org.componenthaus.ant.metadata.Resource;

import java.util.List;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;

public class ComponentImplementation extends AbstractService {
    private final Collection serviceDependencies;
    private Resource []implementationDependencies = null;

    public ComponentImplementation() {
        serviceDependencies = new ArrayList();
    }

    public ComponentImplementation(String fullyQualifiedName, String version, List authors, String oneLineDescription, String fullDescription, ComponentDefinition[] serviceDependencies, Resource[] implementationDependencies) {
        super(fullyQualifiedName, version, authors, oneLineDescription, fullDescription);
        this.serviceDependencies = Arrays.asList(serviceDependencies);
        this.implementationDependencies = implementationDependencies;
    }

    public Collection getServiceDependencies() {
        return serviceDependencies;
    }

    public void addServiceDependency(ComponentDefinition serviceDependency) {
        System.out.println("Calling addServiceDependency");
        serviceDependencies.add(serviceDependency);
    }

    //Named so that betwixt can figure out the add method
    public void addServiceDependencie(ComponentDefinition serviceDependency) {
        System.out.println("Calling addServiceDependencie");
        serviceDependencies.add(serviceDependency);
    }

    public Resource[] getImplementationDependencies() {
        return implementationDependencies;
    }

    public void setImplementationDependencies(Resource[] implementationDependencies) {
        this.implementationDependencies = implementationDependencies;
    }
}
