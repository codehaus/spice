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
 * @version $Revision: 1.1 $ $Date: 2003-10-31 23:41:47 $
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

    private Document createDocument()
        throws Exception
    {
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        final DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.newDocument();
    }
}
