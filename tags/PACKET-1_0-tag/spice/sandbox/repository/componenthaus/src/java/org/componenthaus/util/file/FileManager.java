package org.componenthaus.util.file;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface FileManager {
    void copy(File from, OutputStream to) throws IOException;
    void copy(InputStream inputStream, File target) throws IOException;
}
