package org.componenthaus.util.file;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * TODO make into a component and distribute separately.  With a monitor class for file creation etc.
 */
public interface FileManager {
    String basename(String path);
    void copy(File from, OutputStream to) throws IOException;
    String asString(File f) throws IOException;
}
