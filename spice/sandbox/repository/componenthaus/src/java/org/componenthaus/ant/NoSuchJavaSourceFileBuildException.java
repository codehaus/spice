package org.componenthaus.ant;

import org.apache.tools.ant.BuildException;

public class NoSuchJavaSourceFileBuildException extends BuildException {
    private String interfaceName;

    public NoSuchJavaSourceFileBuildException(String interfaceName) {
        super("No java source file with name " + interfaceName);
        this.interfaceName = interfaceName;
    }

    public String getInterfaceName() {
        return interfaceName;
    }
}
