package org.componenthaus.usecases.submitcomponent;

import junit.framework.TestCase;
import org.componenthaus.ant.metadata.ComponentMetadata;
import org.componenthaus.ant.metadata.InterfaceMetadata;
import org.componenthaus.ant.ClassAbbreviator;
import org.componenthaus.ant.ClassAbbreviatorImpl;
import org.componenthaus.ant.JavadocFormatterImpl;
import org.componenthaus.ant.LineIndenterImpl;
import org.componenthaus.repository.api.Component;
import org.componenthaus.repository.impl.ComponentFactory;
import org.componenthaus.tests.MockClassAbbreviator;

import java.util.Collection;
import java.util.Iterator;

public class MetadataConverterImplTestCase extends TestCase {
    private MetadataConverter converter = null;
    private ClassAbbreviator classAbbreviator = null;

    protected void setUp() throws Exception {
        classAbbreviator = new ClassAbbreviatorImpl(new JavadocFormatterImpl(80),new LineIndenterImpl());
        converter = new MetadataConverterImpl(new ComponentFactory(),classAbbreviator);
    }

    public void testThrowsIllegalArgumentExceptionWhenGivenNull() {
        try {
            converter.convert(null);
            fail("Did not get expected exception");
        } catch (IllegalArgumentException e) {
        }
    }

    public void testThrowsExceptionIfComponentContainsNoInterfaces() {
        ComponentMetadata componentMetadata = new ComponentMetadata();
        try {
            converter.convert(componentMetadata);
            fail("Did not get expected exception");
        } catch (IllegalArgumentException e) {
        }
    }

    public void testConvertsMetadataToComponentCollection() {
        String aClass =
                "package com.acme;\n" +
                "public class AClass {\n" +
                "}";

        ComponentMetadata componentMetadata = new ComponentMetadata();
        componentMetadata.addInterface(new InterfaceMetadata("a","b","c","d",false));
        componentMetadata.addInterface(new InterfaceMetadata("com.acme","AClass","",aClass,true));
        Collection components = converter.convert(componentMetadata);
        assertEquals(2, components.size());
        Iterator i = components.iterator();
        Component component = (Component) i.next();
        assertEquals("AClass",component.getName());
        assertEquals("com.acme.AClass",component.getFullyQualifiedName());
        assertEquals("",component.getFullDescription());

        component = (Component) i.next();
        assertEquals("b",component.getName());
        assertEquals("a.b",component.getFullyQualifiedName());
        assertEquals("c",component.getFullDescription());
        assertEquals("d",component.getServiceInterface());
    }

}
