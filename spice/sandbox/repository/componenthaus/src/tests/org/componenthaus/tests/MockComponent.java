package org.componenthaus.tests;

import org.componenthaus.repository.api.Component;

import java.util.List;

public class MockComponent implements Component {
    private String componentId = null;
    private String fullDescription;

    public MockComponent(String componentId) {
        this.componentId = componentId;
    }

    public String getId() {
        return componentId;
    }

    public void setId(String id) {
        componentId = id;
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

    public List getAuthors() {
        return null;
    }

    public String getOneLineDescription() {
        return null;
    }

    public String getFullDescription() {
        return fullDescription;
    }

    public void setFullDescription(String fullDescription) {
        this.fullDescription = fullDescription;
    }
}