package org.componenthaus.ant;

import org.apache.tools.ant.BuildException;

import java.io.IOException;

public class IOBuildException extends BuildException {
    public IOBuildException(IOException e) {
        super(e);
    }
}
