package org.componenthaus.util.file;

import junit.framework.TestCase;

import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.io.ByteArrayOutputStream;

public class FileManagerImplTestCase extends TestCase {

    public void testFileCopy() throws IOException {
        FileManagerImpl fileManager = new FileManagerImpl();
        File from = File.createTempFile(this.getClass().getName(),".txt");
        String fileContents = "aalkdjlksdjlasdjlksdjlkasjdlaksjdlaksdsalkjd";
        FileWriter writer = new FileWriter(from);
        writer.write(fileContents);
        writer.close();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        fileManager.copy(from, output);
        output.close();
        assertEquals(fileContents, new String(output.toByteArray()));
    }
}
