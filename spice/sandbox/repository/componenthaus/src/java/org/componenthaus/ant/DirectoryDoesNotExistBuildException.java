package org.componenthaus.ant;

import org.apache.tools.ant.BuildException;

public class DirectoryDoesNotExistBuildException extends BuildException {
    private String directoryName;

    public DirectoryDoesNotExistBuildException(String directoryName) {
        super("The directory '" + directoryName + "' does not exist");
        this.directoryName = directoryName;
    }

    public String getDirectoryName() {
        return directoryName;
    }
}
