package org.componenthaus.ant;

import junit.framework.TestCase;

public class LineIndenterImplTestCase extends TestCase {
    private LineIndenter indenter = null;

    protected void setUp() throws Exception {
        indenter = new LineIndenterImpl();
    }

    public void testCanIndentEmptyString() {
        assertEquals("",indenter.indent("",0));
    }

    public void testCanHandleNullInput() {
        assertNull(indenter.indent(null,0));
    }

    public void testCanIndentSingleLine() {
        final String line = "This is a single line";
        assertEquals(line, indenter.indent(line,0));
        assertEquals("\t" + line, indenter.indent(line,1));
        assertEquals("\t\t" + line, indenter.indent(line,2));
    }

    public void testCanIndentTwoLines() {
        final String twoLines =
                "This is line one.\n" +
                "This is line two.";
        assertEquals(twoLines,indenter.indent(twoLines,0));

        assertEquals(
                "\tThis is line one.\n" +
                "\tThis is line two.",indenter.indent(twoLines,1));

        assertEquals(
                "\t\tThis is line one.\n" +
                "\t\tThis is line two.",indenter.indent(twoLines,2));

    }

}
