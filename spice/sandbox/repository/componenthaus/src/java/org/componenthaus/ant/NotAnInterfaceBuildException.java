package org.componenthaus.ant;

import org.apache.tools.ant.BuildException;

public class NotAnInterfaceBuildException extends BuildException {
    private final String interfaceName;

    public NotAnInterfaceBuildException(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getInterfaceName() {
        return interfaceName;
    }
}
