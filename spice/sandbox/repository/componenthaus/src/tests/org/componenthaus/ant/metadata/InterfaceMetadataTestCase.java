package org.componenthaus.ant.metadata;

import junit.framework.TestCase;

public class InterfaceMetadataTestCase extends TestCase {
    private static final String firstLineOfJavadoc = "This is the first line of javadoc.";
    private static final String someJavadoc = firstLineOfJavadoc + " This is the second.  This the third.";

    public void testEquals() {
        final InterfaceMetadata intf = new InterfaceMetadata("d","","","");
        assertFalse(intf.equals(null));

        InterfaceMetadata secondOne = new InterfaceMetadata("d","","","");
        assertTrue(intf.equals(secondOne));

        assertFalse(intf.equals(new InterfaceMetadata("a","","","")));
    }

    public void testCanGetShortDescriptionFromFullJavadoc() {
        final InterfaceMetadata interfaceMetadata = new InterfaceMetadata(null,null,someJavadoc,null);
        assertEquals(firstLineOfJavadoc,interfaceMetadata.getShortDescription());
    }

    public void testCanGetShortDescriptionFromSingleLineJavadoc() {
        final InterfaceMetadata interfaceMetadata = new InterfaceMetadata(null,null,firstLineOfJavadoc,null);
        assertEquals(firstLineOfJavadoc,interfaceMetadata.getShortDescription());
    }
}
