package org.componenthaus.ant;

import org.apache.tools.ant.BuildException;

public class ParameterNotSpecifiedBuildException extends BuildException {
    private final String directoryName;

    public ParameterNotSpecifiedBuildException(String directoryName) {
        this.directoryName = directoryName;
    }

    public String getParameterName() {
        return directoryName;
    }
}
