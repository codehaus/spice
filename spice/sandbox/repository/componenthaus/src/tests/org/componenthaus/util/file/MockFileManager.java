package org.componenthaus.util.file;

import java.io.File;
import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MockFileManager implements FileManager {
    private File preparedNewFile = null;

    public void setupNewFile(File mockFile) {
        preparedNewFile = mockFile;
    }

    public void copy(File from, OutputStream to) throws IOException {
    }

    public void copy(InputStream inputStream, File target) {
    }

}
