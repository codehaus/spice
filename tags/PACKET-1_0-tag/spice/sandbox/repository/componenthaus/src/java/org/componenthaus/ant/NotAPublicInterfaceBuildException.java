package org.componenthaus.ant;

import org.apache.tools.ant.BuildException;

public class NotAPublicInterfaceBuildException extends BuildException {
    private final String interfaceName;

    public NotAPublicInterfaceBuildException(String interfaceName) {
        super("Class file " + interfaceName + " is not a service interface");
        this.interfaceName = interfaceName;
    }

    public String getInterfaceName() {
        return interfaceName;
    }
}
