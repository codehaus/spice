package org.componenthaus.ant;

import org.apache.tools.ant.BuildException;

public class NoSuchJavaSourceFileBuildException extends BuildException {
    private String interfaceName;

    public NoSuchJavaSourceFileBuildException(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getInterfaceName() {
        return interfaceName;
    }
}
