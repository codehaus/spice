package org.componenthaus.util.text;

import junit.framework.TestCase;

public class TextUtilsTestCase extends TestCase {

    public void testFmtDoesNotModShortLines() {
        final String shortLine = "shortLine";
        assertEquals(shortLine, TextUtils.fmt(shortLine));
    }

    public void testFmtSplitsLongLine() {
        final String longLine = "This string is more than eighty characters in length.  We'll see if fmt can split it, and split it intelligently i.e. at a natural word boundary";
        final String expected =  "This string is more than eighty characters in length.  We'll see if fmt can\n"+
                                    "split it, and split it intelligently i.e. at a natural word boundary";
        assertEquals(expected,TextUtils.fmt(longLine));
    }

    public void testFmtSplitsLongLineThatEndsOn80Chars() {
        final String longLine = "1234567890123456789012345678901234567890123456789012345678901234567890123456789. New Line";
        final String expected = "1234567890123456789012345678901234567890123456789012345678901234567890123456789.\nNew Line";
        assertEquals(expected,TextUtils.fmt(longLine));
    }

    public void testFmtSplitsLongLineThatEndsOn80CharsWithSpecifiedNewLine() {
        final String longLine = "1234567890123456789012345678901234567890123456789012345678901234567890123456789. New Line";
        final String expected = "1234567890123456789012345678901234567890123456789012345678901234567890123456789.</br>New Line";
        assertEquals(expected,TextUtils.fmt(longLine,"</br>"));
    }

    public void testReplaceHandlesIndenticalMapping() {
        final String line = "line";
        assertEquals(line, TextUtils.replace("li","li",line));
    }

    public void testReplaceSimpleRegexp() {
        final String line = "  *  Hello";
        assertEquals("Hello",TextUtils.replace("\\s*\\*\\s*","",line));
    }

    public void testReplaceSimpleRegexpAcrossLineBoundaries() {
        final String line = "  *  Hello\n" +
                            "  *  Hello";
        assertEquals("Hello\nHello",TextUtils.replace("\\s*\\*\\s*","",line));
    }
}
