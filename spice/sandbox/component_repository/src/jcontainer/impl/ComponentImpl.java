package jcontainer.impl;

import jcontainer.api.Component;
import jcontainer.api.Container;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

class ComponentImpl implements Component, Serializable {
    private String name = null;
    private String version = null;
    private String theInterface = null;
    private Container container = null;

    public ComponentImpl(String componentName, String version, String theInterface) {
        this.name = componentName;
        this.version = version;
        this.theInterface = theInterface;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Container getContainer() {
        return container;
    }

    public void setContainer(Container container) {
        this.container = container;
    }

    public String getTheInterface() {
        return theInterface;
    }

    public void setTheInterface(String theInterface) {
        this.theInterface = theInterface;
    }

    public String toString() {
        return new ToStringBuilder(this).
                append("name",name).
                append("version",version).
                append("theInterface",theInterface).toString();
    }
}
