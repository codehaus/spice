package org.componenthaus.ant.metadata;

import junit.framework.TestCase;

public class ComponentMetadataTestCase extends TestCase {
    private ComponentMetadata componentMetadata = null;

    protected void setUp() throws Exception {
        componentMetadata = new ComponentMetadata();
    }

    public void testEquals() {
        assertFalse(componentMetadata.equals(null));
        assertFalse(componentMetadata.equals(""));

        ComponentMetadata secondOne = new ComponentMetadata();
        assertTrue(componentMetadata.equals(secondOne));

        final InterfaceMetadata emptyInterfaceMetadata = new InterfaceMetadata("","","","", false);
        componentMetadata.addInterface(emptyInterfaceMetadata);
        assertFalse(componentMetadata.equals(secondOne));

        secondOne.addInterface(emptyInterfaceMetadata);
        assertTrue(componentMetadata.equals(secondOne));

        final InterfaceMetadata interfaceMetadata_a = new InterfaceMetadata("a","","","", false);
        componentMetadata.addInterface(interfaceMetadata_a);
        assertFalse(componentMetadata.equals(secondOne));

        InterfaceMetadata interfaceMetadata_b = new InterfaceMetadata("b","","","", false);
        secondOne.addInterface(interfaceMetadata_b);
        assertFalse(componentMetadata.equals(secondOne));

        ComponentMetadata thirdOne = new ComponentMetadata();
        thirdOne.addInterface(emptyInterfaceMetadata);
        thirdOne.addInterface(interfaceMetadata_a);
        assertEquals(componentMetadata,thirdOne);
    }

    public void testCanSerializeToAndFromXmlWhenEmpty() {
        final String xml = componentMetadata.toXml();
        final ComponentMetadata reconstituted = ComponentMetadata.fromXml(xml);
        assertEquals(componentMetadata,reconstituted);
    }

    public void testCanSerializeToAndFromXmlWhenContainingOneInterface() {
        final InterfaceMetadata emptyInterfaceMetadata = new InterfaceMetadata("","","","", false);
        componentMetadata.addInterface(emptyInterfaceMetadata);
        assertEquals(componentMetadata,ComponentMetadata.fromXml(componentMetadata.toXml()));
    }

    public void testCanSerializeToAndFromXmlWhenContainingTwoInterfaces() {
        final InterfaceMetadata emptyInterfaceMetadata = new InterfaceMetadata("","","","", false);
        final InterfaceMetadata simpleInterfaceMetadata = new InterfaceMetadata("a","b","c","d", false);
        componentMetadata.addInterface(emptyInterfaceMetadata);
        componentMetadata.addInterface(simpleInterfaceMetadata);
        assertEquals(componentMetadata,ComponentMetadata.fromXml(componentMetadata.toXml()));
    }
}
