package jcontainer;

import java.io.Serializable;

public class ComponentImpl implements Component, Serializable {
    private String name = null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
