package org.componenthaus.ant;

import org.apache.tools.ant.BuildException;

public class DirectoryDoesNotExistBuildException extends BuildException {
    private String directoryName;

    public DirectoryDoesNotExistBuildException(String directoryName) {
        this.directoryName = directoryName;
    }

    public String getDirectoryName() {
        return directoryName;
    }
}
