package org.componenthaus.ant.metadata;

import junit.framework.TestCase;

public class InterfaceMetadataTestCase extends TestCase {
    private static final String firstLineOfJavadoc = "This is the first line of javadoc.";
    private static final String someJavadoc = firstLineOfJavadoc + " This is the second.  This the third.";

    public void testEquals() {
        final InterfaceMetadata intf = new InterfaceMetadata("d","","","", false);
        assertFalse(intf.equals(null));

        InterfaceMetadata secondOne = new InterfaceMetadata("d","","","", false);
        assertTrue(intf.equals(secondOne));

        assertFalse(intf.equals(new InterfaceMetadata("a","","","", false)));
    }

    public void testCanGetShortDescriptionFromFullJavadoc() {
        final InterfaceMetadata interfaceMetadata = new InterfaceMetadata(null,null,someJavadoc,null, false);
        assertEquals(firstLineOfJavadoc,interfaceMetadata.getShortDescription());
    }

    public void testCanGetShortDescriptionFromSingleLineJavadoc() {
        final InterfaceMetadata interfaceMetadata = new InterfaceMetadata(null,null,firstLineOfJavadoc,null, false);
        assertEquals(firstLineOfJavadoc,interfaceMetadata.getShortDescription());
    }
}
