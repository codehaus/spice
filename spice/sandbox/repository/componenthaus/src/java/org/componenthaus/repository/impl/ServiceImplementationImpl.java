package org.componenthaus.repository.impl;

import org.componenthaus.repository.api.ServiceImplementation;
import org.componenthaus.repository.api.Service;

import java.util.List;
import java.util.Collection;
import java.util.ArrayList;
import java.io.Serializable;

public class ServiceImplementationImpl extends ServiceImpl implements ServiceImplementation, Serializable {
    private String id = null;
    private final Collection plugs;

    public ServiceImplementationImpl(String fullyQualifiedName, String version, List authors, String oneLineDescription, String fullDescription) {
        super(fullyQualifiedName, version, authors, oneLineDescription, fullDescription);
        plugs = new ArrayList();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Collection getPlugs() {
        return plugs;
    }

    public void addPlug(Service service) {
        plugs.add(service);
    }
}
