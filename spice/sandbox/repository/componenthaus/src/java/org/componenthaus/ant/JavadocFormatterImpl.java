package org.componenthaus.ant;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;

public class JavadocFormatterImpl implements JavadocFormatter {
    private static final int LENGTH_OF_A_SPACE_CHARACTER = 1;
    private static final Collection wordsRequiringNewline = new ArrayList();
    private final int maxLineLength;

    static {
        wordsRequiringNewline.add("@param");
        wordsRequiringNewline.add("@return");
        wordsRequiringNewline.add("@throws");
        wordsRequiringNewline.add("@version");
        wordsRequiringNewline.add("@author");
    }


    public JavadocFormatterImpl(int maxLineLength) {
        this.maxLineLength = maxLineLength;
    }

    public String format(String javadoc) {
        String result = javadoc;
        if ( javadoc != null && javadoc.length() != 0) {
            result = formatJavadoc(javadoc);
        }
        return result;
    }

    private String formatJavadoc(String javadoc) {
        final StringBuffer result = new StringBuffer("/**\n");
        final List words = new ArrayList(Arrays.asList(javadoc.split("\\s")));
        final String commentPrefix = " * ";
        final int commentTextLength = maxLineLength - commentPrefix.length();
        boolean firstNewLineWord = true;
        while ( words.size() > 0 ) {
            if ( isNewlineWord((String) words.get(0)) && firstNewLineWord) {
                result.append(commentPrefix).append("\n");
                firstNewLineWord = false;
            }
            String line = commentPrefix + createLineText(words, commentTextLength) + "\n";
            result.append(line);
        }
        result.append(" **/");
        return result.toString();
    }

    private String createLineText(List words, int commentTextLength) {
        final StringBuffer result = new StringBuffer();
        while(canAddAnotherWord(result,words, commentTextLength)) {
            addAnotherWord(result,words);
        }
        if ( result.length() == 0 ) {
            addAnotherWord(result,words);
        }
        return result.toString();
    }

    private void addAnotherWord(StringBuffer result, List words) {
        if ( result.length() > 0 ) {
            result.append(" ");
        }
        result.append(words.remove(0));
    }

    private boolean canAddAnotherWord(StringBuffer current, List words, int maxLength) {
        boolean result = false;
        if ( words.size() > 0 ) {
            String nextWord = (String) words.get(0);
            if ( isNewlineWord(nextWord) ) {
                result = current.length() == 0;
            } else {
                int nextLength = current.length() + nextWord.length() + LENGTH_OF_A_SPACE_CHARACTER;
                result = nextLength < maxLength;
            }
        }
        return result;
    }

    private boolean isNewlineWord(String word) {
        return wordsRequiringNewline.contains(word);
    }
}
