package org.componenthaus.ant;

import org.apache.tools.ant.BuildException;

public class ParameterNotSpecifiedBuildException extends BuildException {
    private final String directoryName;

    public ParameterNotSpecifiedBuildException(String parameterName) {
        super("Required parameter '" + parameterName + "' is not specified");
        this.directoryName = parameterName;
    }

    public String getParameterName() {
        return directoryName;
    }
}
