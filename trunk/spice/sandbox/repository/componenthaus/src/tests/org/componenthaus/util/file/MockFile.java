package org.componenthaus.util.file;

import java.io.File;

public class MockFile extends File {
    private boolean preparedExists = false;

    public MockFile() {
        super("");
    }

    public void setupExists(boolean b) {
        preparedExists = b;
    }

    public boolean exists() {
        return preparedExists;
    }
}
