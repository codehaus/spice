package org.componenthaus.ant;

import org.apache.tools.ant.BuildException;

public class NoJavadocBuildException extends BuildException {
    private String interfaceName;

    public NoJavadocBuildException(String interfaceName) {
        super("There is no javadoc for the source file " + interfaceName);
        this.interfaceName = interfaceName;
    }


    public String getInterfaceName() {
        return interfaceName;
    }
}
