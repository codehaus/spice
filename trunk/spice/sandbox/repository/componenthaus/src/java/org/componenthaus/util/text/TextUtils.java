package org.componenthaus.util.text;

import java.util.Collection;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class TextUtils {
    private static final int columnCount = 80;
    private static final String newLine = "\n";
    private static Collection breakChars = new ArrayList();

    static {
        breakChars.add(new Character(' '));
    }

    public static String fmt(String text) {
        return fmt(text, newLine);
    }

    public static String fmt(String text, final String lineBreak) {
        System.out.println("text in is " + text);
        final StringBuffer result = new StringBuffer();
        while ( text != null ) {
            if ( text.length() >= columnCount ) {
                final String chunk = getChunk(text);
                System.out.println("Chunk is " + chunk);
                result.append(chunk).append(lineBreak);
                text = text.substring(chunk.length()).trim();
            } else {
                result.append(text);
                text = null;
            }
        }
        return result.toString();
    }

    private static String getChunk(String text) {
        int lastBreakPoint = columnCount;
        char current = text.charAt(lastBreakPoint);
        while ( ! breakChar(current)) {
            lastBreakPoint--;
            current = text.charAt(lastBreakPoint);
        }
        return text.substring(0,lastBreakPoint);
    }

    private static boolean breakChar(char c) {
        return breakChars.contains(new Character(c));
    }

    public static String replace(final String oldString, final String newString,final String text) {
        final StringBuffer result = new StringBuffer();
        final StringTokenizer lines = new StringTokenizer(text,"\n",true);
        String token = "who cares";
        while ( token != null && lines.hasMoreTokens()) {
            token = lines.nextToken();
            if ( !"\n".equals(token)) {
                result.append(token.replaceAll(oldString,newString));
            } else {
                result.append(token);
            }
        }
        return result.toString();
    }
}
