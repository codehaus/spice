package org.componenthaus.ant;

import junit.framework.TestCase;

public class JavadocFormatterImplTestCase extends TestCase {
    private static final int MAX_LINE_LENGTH = 80;
    private JavadocFormatter formatter = null;

    protected void setUp() throws Exception {
        formatter = new JavadocFormatterImpl(MAX_LINE_LENGTH);
    }

    public void testCanFormatEmptyString() {
        assertEquals("",formatter.format(""));
    }

    public void testWillIgnoreNull() {
        assertNull(formatter.format(null));
    }

    public void testCanHandleSingleShortLine() {
        String input = "This is a short javadoc comment";
        String expected =
                "/**\n" +
                " * " + input + "\n" +
                " **/";
        final String actual = formatter.format(input);
        assertEquals(expected,actual);
    }

    public void testWillConcatenateTwoShortLines() {
        String input = "This is a short javadoc comment.";
        String input2 = "So is this.";
        String expected =
                "/**\n" +
                " * " + input + " " + input2 + "\n" +
                " **/";
        final String actual = formatter.format(input + "\n" + input2);
        assertEquals(expected,actual);
    }

    public void testCanHandleAWordLongerThanTheLineLimit() {
        formatter = new JavadocFormatterImpl(10);
        String input = "This is a short javadoc comment.";
        String input2 = "So is this.";
        String longInput = "AndThisWordIsLongerThanTheMaxLineLength.";
        String expected =
                "/**\n" +
                " * This\n" +
                " * is a\n" +
                " * short\n" +
                " * javadoc\n" +
                " * comment.\n" +
                " * So is\n" +
                " * this.\n" +
                " * AndThisWordIsLongerThanTheMaxLineLength.\n" +
                " **/";
        final String actual = formatter.format(input + "\n" + input2 + "\n" + longInput);
        assertEquals(expected,actual);
    }

    public void testAlwaysPutsParamTagOnNewLine() {
        String input = "This is a short line. @param this is a param. @return this is return";
        final String actual = formatter.format(input);
        assertEquals(
                "/**\n" +
                " * This is a short line.\n" +
                " * \n" +
                " * @param this is a param.\n" +
                " * @return this is return\n" +
                " **/", actual);
    }
}
