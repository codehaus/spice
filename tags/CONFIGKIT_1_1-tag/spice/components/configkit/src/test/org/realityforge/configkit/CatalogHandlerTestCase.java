/*
 * Copyright  The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 */
package org.realityforge.configkit;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.SAXParserFactory;
import junit.framework.TestCase;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * Basic unit tests for the catalog handler class.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 */
public final class CatalogHandlerTestCase
    extends TestCase
{
    public CatalogHandlerTestCase( final String name )
    {
        super( name );
    }

    public void testNullEntityList()
    {
        try
        {
            new CatalogHandler( null );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( npe.getMessage(), "entitys" );
            return;
        }
        fail( "Expected Null pointer due to null entitys" );
    }

    public void testEmptyCatalog()
        throws Exception
    {
        final List entitys = new ArrayList();
        parseCatalog( 1, entitys );
        assertEquals( "entity count: " + entitys, 0, entitys.size() );
    }

    public void testSingleWithPublicID()
        throws Exception
    {
        final List entitys = new ArrayList();
        parseCatalog( 2, entitys );
        assertEquals( "entity count: " + entitys, 1, entitys.size() );
        final EntityInfo info = (EntityInfo)entitys.get( 0 );
        assertEquals( "info.getPublicId()",
                      "-//PHOENIX/Assembly DTD Version 1.0//EN",
                      info.getPublicId() );
        assertEquals( "info.getSystemId()", null, info.getSystemId() );
        assertEquals( "info.getResource()",
                      "org/apache/avalon/phoenix/tools/assembly.dtd",
                      info.getResource() );
    }

    public void testSingleWithSystemID()
        throws Exception
    {
        final List entitys = new ArrayList();
        parseCatalog( 3, entitys );
        assertEquals( "entity count: " + entitys, 1, entitys.size() );
        final EntityInfo info = (EntityInfo)entitys.get( 0 );
        assertEquals( "info.getPublicId()",
                      null,
                      info.getPublicId() );
        assertEquals( "info.getSystemId()",
                      "http://jakarta.apache.org/phoenix/assembly_1_0.dtd",
                      info.getSystemId() );
        assertEquals( "info.getResource()",
                      "org/apache/avalon/phoenix/tools/assembly.dtd",
                      info.getResource() );
    }

    public void testSingleWithBothID()
        throws Exception
    {
        final List entitys = new ArrayList();
        parseCatalog( 4, entitys );
        assertEquals( "entity count: " + entitys, 1, entitys.size() );
        final EntityInfo info = (EntityInfo)entitys.get( 0 );
        assertEquals( "info.getPublicId()",
                      "-//PHOENIX/Assembly DTD Version 1.0//EN",
                      info.getPublicId() );
        assertEquals( "info.getSystemId()",
                      "http://jakarta.apache.org/phoenix/assembly_1_0.dtd",
                      info.getSystemId() );
        assertEquals( "info.getResource()",
                      "org/apache/avalon/phoenix/tools/assembly.dtd",
                      info.getResource() );
    }

    public void testTriple()
        throws Exception
    {
        final List entitys = new ArrayList();
        parseCatalog( 5, entitys );
        assertEquals( "entity count: " + entitys, 3, entitys.size() );

        final EntityInfo info1 = (EntityInfo)entitys.get( 0 );
        assertEquals( "info1.getPublicId()",
                      "-//PHOENIX/Assembly DTD Version 1.0//EN",
                      info1.getPublicId() );
        assertEquals( "info1.getSystemId()", null, info1.getSystemId() );
        assertEquals( "info1.getResource()",
                      "org/apache/avalon/phoenix/tools/assembly.dtd",
                      info1.getResource() );

        final EntityInfo info2 = (EntityInfo)entitys.get( 1 );
        assertEquals( "info2.getPublicId()",
                      null,
                      info2.getPublicId() );
        assertEquals( "info2.getSystemId()",
                      "http://jakarta.apache.org/phoenix/assembly_1_0.dtd",
                      info2.getSystemId() );
        assertEquals( "info2.getResource()",
                      "org/apache/avalon/phoenix/tools/assembly.dtd",
                      info2.getResource() );

        final EntityInfo info3 = (EntityInfo)entitys.get( 2 );
        assertEquals( "info3.getPublicId()",
                      "-//PHOENIX/Assembly DTD Version 1.0//EN",
                      info3.getPublicId() );
        assertEquals( "info3.getSystemId()",
                      "http://jakarta.apache.org/phoenix/assembly_1_0.dtd",
                      info3.getSystemId() );
        assertEquals( "info3.getResource()",
                      "org/apache/avalon/phoenix/tools/assembly.dtd",
                      info3.getResource() );
    }

    public void testNullVersion()
    {
        try
        {
            final List entitys = new ArrayList();
            parseCatalog( 6, entitys );
            fail( "Expected exception due to null version" );
        }
        catch( final SAXException se )
        {
            return;
        }
        catch( final Exception e )
        {
            fail( "Unexpected exception " + e );
        }
    }

    public void testBadVersion()
    {
        try
        {
            final List entitys = new ArrayList();
            parseCatalog( 7, entitys );
            fail( "Expected exception due to bad version" );
        }
        catch( final SAXException se )
        {
            return;
        }
        catch( final Exception e )
        {
            fail( "Unexpected exception " + e );
        }
    }

    public void testNullResource()
    {
        try
        {
            final List entitys = new ArrayList();
            parseCatalog( 8, entitys );
            fail( "Expected exception due to null resource" );
        }
        catch( final SAXException se )
        {
            return;
        }
        catch( final Exception e )
        {
            fail( "Unexpected exception " + e );
        }
    }

    public void testNullIds()
    {
        try
        {
            final List entitys = new ArrayList();
            parseCatalog( 9, entitys );
            fail( "Expected exception due to null ids" );
        }
        catch( final SAXException se )
        {
            return;
        }
        catch( final Exception e )
        {
            fail( "Unexpected exception " + e );
        }
    }

    public void testBadElements()
    {
        try
        {
            final List entitys = new ArrayList();
            parseCatalog( 10, entitys );
            fail( "Expected exception due to bad elements" );
        }
        catch( final SAXException se )
        {
            return;
        }
        catch( final Exception e )
        {
            fail( "Unexpected exception " + e );
        }
    }

    private void parseCatalog( final int number, final List entitys )
        throws Exception
    {
        final XMLReader xmlReader = createXMLReader();
        final CatalogHandler handler = new CatalogHandler( entitys );
        xmlReader.setContentHandler( handler );
        xmlReader.setErrorHandler( handler );
        final InputStream inputStream =
            getClass().getResourceAsStream( "test/catalog" + number + ".xml" );

        xmlReader.parse( new InputSource( inputStream ) );
    }

    /**
     * Create an XMLReader.
     *
     * @return the created XMLReader
     */
    private static XMLReader createXMLReader()
    {
        final SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        saxParserFactory.setNamespaceAware( false );
        try
        {
            return saxParserFactory.newSAXParser().getXMLReader();
        }
        catch( final Exception e )
        {
            throw new IllegalStateException( e.toString() );
        }
    }
}

