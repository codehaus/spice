/*
 * Copyright  The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 */
package org.realityforge.configkit;

import junit.framework.TestCase;
import java.util.HashMap;
import java.util.Properties;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.w3c.dom.Comment;

/**
 * Basic unit tests for the PropertyExpander.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 */
public final class PropertyExpanderTestCase
    extends TestCase
{
    private final PropertyExpander m_emptyExpander = new PropertyExpander( PropertyExpander.EMPTY_ON_UNDEFINED );
    private final PropertyExpander m_exceptExpander = new PropertyExpander();
    private final PropertyExpander m_leaveExpander = new PropertyExpander( PropertyExpander.LEAVE_UNDEFINED );

    public PropertyExpanderTestCase( final String name )
    {
        super( name );
    }

    public void testStringExpansion()
        throws Exception
    {
        final HashMap data = new HashMap();
        data.put( "app.name", "MyApp" );
        assertEquals( "Expand with no vars",
                      "foo",
                      m_emptyExpander.expandValues( "foo", data ) );
        assertEquals( "Expand with one var",
                      "MyApp",
                      m_emptyExpander.expandValues( "${app.name}", data ) );
        assertEquals( "Expand with one var n start text",
                      "xMyApp",
                      m_emptyExpander.expandValues( "x${app.name}", data ) );
        assertEquals( "Expand with one var n end text",
                      "MyAppx",
                      m_emptyExpander.expandValues( "${app.name}x", data ) );
        assertEquals( "Expand with two var",
                      "MyAppMyApp",
                      m_emptyExpander.expandValues( "${app.name}${app.name}", data ) );
        assertEquals( "Expand with two var inner text",
                      "MyAppxMyApp",
                      m_emptyExpander.expandValues( "${app.name}x${app.name}", data ) );
        assertEquals( "Expand with two var inner n outer text",
                      "xMyAppxMyAppx",
                      m_emptyExpander.expandValues( "x${app.name}x${app.name}x", data ) );
        assertEquals( "No exist and empty policy",
                      "",
                      m_emptyExpander.expandValues( "${noexist}", data ) );
        assertEquals( "No exist and leave policy",
                      "${noexist}",
                      m_leaveExpander.expandValues( "${noexist}", data ) );
        try
        {
            m_exceptExpander.expandValues( "${noexist}", data );
            fail( "Expected exception on non existent property" );
        }
        catch( Exception e )
        {
        }

        try
        {
            m_emptyExpander.expandValues( "${noexist", data );
            fail( "Expected exception on badly formed property" );
        }
        catch( Exception e )
        {
        }
    }

    public void testElementExpansion()
        throws Exception
    {
        final HashMap data = new HashMap();
        data.put( "app.name", "MyApp" );

        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        final DocumentBuilder builder = factory.newDocumentBuilder();
        final Document document = builder.newDocument();
        final Element root = document.createElement( "root" );
        root.setAttribute( "attr", "${app.name}" );

        final Text text1 = document.createTextNode( "${app.name}" );
        root.appendChild( text1 );

        final Element child = document.createElement( "child" );
        child.setAttribute( "attr2", "${app.name}" );

        final Text text2 = document.createTextNode( "${app.name}" );
        child.appendChild( text2 );

        final Comment comment = document.createComment( "some random comment" );
        child.appendChild( comment );

        root.appendChild( child );

        m_emptyExpander.expandValues( root, data );

        assertEquals( "root/@attr", "MyApp", root.getAttribute( "attr" ) );
        assertEquals( "root/#content", "MyApp", text1.getData() );
        assertEquals( "root/child/@attr2", "MyApp", child.getAttribute( "attr2" ) );
        assertEquals( "root/child/#content", "MyApp", text2.getData() );
    }

    public void testPropertiesExpansion()
        throws Exception
    {
        final HashMap data = new HashMap();
        data.put( "app.name", "MyApp" );

        final Properties input = new Properties();
        final InputStream inputStream = getClass().getResourceAsStream( "test/test.properties" );
        assertNotNull( "Input data", inputStream );
        input.load(inputStream );

        final Properties output =
            m_emptyExpander.expandValues( input, data );

        assertEquals( "${app.name}.description=Foo", "Foo", output.getProperty( "MyApp.description" ) );
        assertEquals( "location=${app.name}", "MyApp", output.getProperty( "location" ) );
    }
}

