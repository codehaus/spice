package org.componenthaus.util.source;

import de.hunsicker.jalopy.Jalopy;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class JalopyCodeFormatter implements CodeFormatter {
    public String format(String in) throws CodeFormatter.Exception {
        Jalopy jalopy = new Jalopy();
        File tempFile = null;
        final StringBuffer output = new StringBuffer();
        try {
            tempFile = createTempFile(in);
            jalopy.setInput(tempFile);
            jalopy.setOutput(output);
            jalopy.format();
        } catch (IOException e) {
            throw new CodeFormatter.Exception("Exception formatting code via Jalopy",e);
        } finally {
            if ( tempFile != null ) {
                tempFile.delete();
            }
        }
        return output.toString();
    }

    private File createTempFile(String in) throws IOException {
        File temp = File.createTempFile("pattern", ".suffix");
        BufferedWriter out = new BufferedWriter(new FileWriter(temp));
        out.write(in);
        out.close();
        return temp;
    }
}
