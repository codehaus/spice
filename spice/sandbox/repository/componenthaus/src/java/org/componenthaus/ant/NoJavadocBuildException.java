package org.componenthaus.ant;

import org.apache.tools.ant.BuildException;

public class NoJavadocBuildException extends BuildException {
    private String interfaceName;

    public NoJavadocBuildException(String interfaceName) {
        this.interfaceName = interfaceName;
    }


    public String getInterfaceName() {
        return interfaceName;
    }
}
