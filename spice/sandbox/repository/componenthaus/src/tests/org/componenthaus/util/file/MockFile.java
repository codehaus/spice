package org.componenthaus.util.file;

import java.io.File;

public class MockFile extends File {
    private boolean preparedExists = false;
    private boolean preparedIsDirectory = false;
    private int setupLength = 0;

    public MockFile() {
        super("");
    }

    public void setupExists(boolean b) {
        preparedExists = b;
    }

    public boolean exists() {
        return preparedExists;
    }

    public void setupLength(int length) {
        setupLength = length;
    }

    public long length() {
        return setupLength;
    }

    public void setupPreparedIsDirectory(boolean b) {
        this.preparedIsDirectory = b;
    }

    public boolean isDirectory() {
        return preparedIsDirectory;
    }
}
