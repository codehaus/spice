package org.componenthaus.ant;

import org.apache.tools.ant.BuildException;

public class NoInterfacesSpecifiedBuildException extends BuildException {
    public NoInterfacesSpecifiedBuildException() {
        super("No interfaces specified to ant task");
    }
}
