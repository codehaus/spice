package org.componenthaus.ant;

import org.apache.tools.ant.BuildException;

public class NotADirectoryBuildException extends BuildException {
    private String directoryName;

    public NotADirectoryBuildException(String directoryName) {
        super(directoryName + " is not a directory");
        this.directoryName = directoryName;
    }

    public String getDirectoryName() {
        return directoryName;
    }
}
