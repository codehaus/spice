package org.componenthaus.tests;

import org.componenthaus.repository.api.Component;
import org.componenthaus.repository.api.ServiceImplementation;

import java.util.Collection;
import java.util.List;

public class MockComponent implements Component {
    private String componentId = null;

    public MockComponent(String componentId) {
        this.componentId = componentId;
    }

    public String getId() {
        return null;
    }

    public void setId(String id) {
    }

    public Collection getImplementations() {
        return null;
    }

    public void addImplementation(ServiceImplementation impl) {
    }

    public String getServiceInterface() {
        return null;
    }

    public String getName() {
        return null;
    }

    public String getVersion() {
        return null;
    }

    public String getFullyQualifiedName() {
        return null;
    }

    public String getAuthorsCSV() {
        return null;
    }

    public List getAuthors() {
        return null;
    }

    public String getOneLineDescription() {
        return null;
    }

    public String getFullDescription() {
        return null;
    }
}
