/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.io;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import junit.framework.TestCase;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.3 $ $Date: 2003-10-31 23:58:34 $
 */
public class DOMMetaClassDeserializerTestCase
    extends TestCase
{
    public void testGetPathDescriptionOneLevelDeep()
        throws Exception
    {
        final DOMMetaClassDeserializer deserializer = new DOMMetaClassDeserializer();
        final Document document = createDocument();
        final Element element = document.createElement( "foo" );
        final String description = deserializer.getPathDescription( element );
        assertEquals( "description", "foo", description );
    }

    public void testGetPathDescriptionMultipleLevelsDeep()
        throws Exception
    {
        final DOMMetaClassDeserializer deserializer = new DOMMetaClassDeserializer();
        final Document document = createDocument();
        final Element element = document.createElement( "foo" );
        final Element child = document.createElement( "baz" );
        element.appendChild( child );
        final String description = deserializer.getPathDescription( child );
        assertEquals( "description", "foo/baz", description );
    }

    public void testExpectElementThatIsPresent()
        throws Exception
    {
        final DOMMetaClassDeserializer deserializer = new DOMMetaClassDeserializer();
        final Document document = createDocument();
        final Element element = document.createElement( "foo" );
        deserializer.expectElement( element, "foo" );
    }

    public void testExpectElementThatIsNotPresent()
        throws Exception
    {
        final DOMMetaClassDeserializer deserializer = new DOMMetaClassDeserializer();
        final Document document = createDocument();
        final Element element = document.createElement( "foo" );

        try
        {
            deserializer.expectElement( element, "baz" );
        }
        catch( Exception e )
        {
            final String message =
                "Unexpected element. Expected: baz. Actual: foo @ foo.";
            assertEquals( "e.getMessage()", message, e.getMessage() );
            return;
        }
        fail( "Expected exception as missing element" );
    }

    public void testExpectAttributeThatIsPresent()
        throws Exception
    {
        final DOMMetaClassDeserializer deserializer = new DOMMetaClassDeserializer();
        final Document document = createDocument();
        final Element element = document.createElement( "foo" );
        element.setAttribute( "baz", "bar" );
        final String result = deserializer.expectAttribute( element, "baz" );
        assertEquals( "result", "bar", result );
    }

    public void testExpectAttributeThatIsNotPresent()
        throws Exception
    {
        final DOMMetaClassDeserializer deserializer = new DOMMetaClassDeserializer();
        final Document document = createDocument();
        final Element element = document.createElement( "foo" );
        try
        {
            deserializer.expectAttribute( element, "baz" );
        }
        catch( Exception e )
        {
            final String message =
                "Element named foo missing attribute named baz @ foo.";
            assertEquals( "e.getMessage()", message, e.getMessage() );
            return;
        }
        fail( "Expected exception as missing attribute" );
    }

    private Document createDocument()
        throws Exception
    {
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        final DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.newDocument();
    }
}
