package org.componenthaus.util.file;

import java.io.File;
import java.io.OutputStream;
import java.io.IOException;

public class MockFileManager implements FileManager {
    private File preparedNewFile = null;

    public void setupNewFile(File mockFile) {
        preparedNewFile = mockFile;
    }

    public String basename(String path) {
        return null;
    }

    public void copy(File from, OutputStream to) throws IOException {
    }

    public String asString(File f) throws IOException {
        return null;
    }

    public File newFile(String fileName) {
        return preparedNewFile;
    }
}
