package org.componenthaus.ant;

import java.io.BufferedReader;
import java.io.StringReader;
import java.io.IOException;

public class LineIndenterImpl implements LineIndenter {
    private static final String END_LINE = "\n";
    private static final String TAB = "\t";

    public String indent(String lines, int numIndents) {
        String result = lines;
        if ( lines != null && lines.length() > 0 && numIndents > 0 ) {
            try {
                result = indentLines(lines, numIndents);
            } catch (IOException e) {
                throw new RuntimeException("Got an IOException reading strings", e);
            }
        }
        return result;
    }

    private String indentLines(String lines, int numIndents) throws IOException {
        StringBuffer result = new StringBuffer();
        final String indent = createIndentText(numIndents);
        BufferedReader reader = new BufferedReader(new StringReader(lines));
        String line = reader.readLine();
        while ( line != null ) {
            if ( result.length() > 0 ) {
                result.append(END_LINE);
            }
            result.append(indent).append(line);
            line = reader.readLine();
        }
        reader.close();
        return result.toString();
    }

    private String createIndentText(int numIndents) {
        String result = "";
        for(int i=0;i<numIndents;i++) {
            result += TAB;
        }
        return result;
    }
}
