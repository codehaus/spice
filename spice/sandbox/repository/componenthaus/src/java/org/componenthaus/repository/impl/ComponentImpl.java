package org.componenthaus.repository.impl;

import org.componenthaus.repository.api.Component;
import org.componenthaus.repository.api.ServiceImplementation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Collections;

public class ComponentImpl extends ServiceImpl implements Component, Serializable {
    private String id = null;
    private String serviceInterface = null;
    private final Collection implementations;

    public ComponentImpl() {
        this(null,null,Collections.EMPTY_LIST,null,null,null);
    }

    public ComponentImpl(String fullyQualifiedName, String version, List authors, String oneLineDescription, String fullDescription,String serviceInterface) {
        super(fullyQualifiedName,version,authors,oneLineDescription,fullDescription);
        this.serviceInterface = serviceInterface;
        implementations = new ArrayList();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Collection getImplementations() {
        return implementations;
    }

    public void addImplementation(ServiceImplementation impl) {
        implementations.add(impl);
    }

    public String getServiceInterface() {
        return serviceInterface;
    }
}
