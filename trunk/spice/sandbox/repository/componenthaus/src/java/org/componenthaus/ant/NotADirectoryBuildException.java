package org.componenthaus.ant;

import org.apache.tools.ant.BuildException;

public class NotADirectoryBuildException extends BuildException {
    private String directoryName;

    public NotADirectoryBuildException(String directoryName) {
        this.directoryName = directoryName;
    }

    public String getDirectoryName() {
        return directoryName;
    }
}
