/*
 * Copyright  The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 */
package org.realityforge.configkit;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Properties;
import javax.xml.parsers.FactoryConfigurationError;
import junit.framework.TestCase;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

/**
 * Basic unit tests for the resolver factory.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 */
public final class ResolverFactoryTestCase
    extends TestCase
{
    private static final String CATALOG_JAR = "aTestCatalog.jar";
    private static final String PARSER_KEY = "javax.xml.parsers.SAXParserFactory";

    public ResolverFactoryTestCase( final String name )
    {
        super( name );
    }

    public void testNullClassLoader()
    {
        try
        {
            final EntityResolver resolver = ResolverFactory.createResolver( null );
            fail( "Expected to get a npe creating resolver: " + resolver );
        }
        catch( NullPointerException npe )
        {
            assertEquals( "npe.getMessage()", npe.getMessage(), "classLoader" );
        }
        catch( Exception e )
        {
            fail( "Unexpected exception creating resolver " + e );
        }
    }

    public void testBadParser()
    {
        final ClassLoader classLoader = createClassLoader();
        final String oldValue = System.getProperty( PARSER_KEY );
        System.setProperty( PARSER_KEY, "Non-existent-parser" );
        try
        {
            ResolverFactory.createResolver( classLoader );
        }
        catch( FactoryConfigurationError se )
        {
        }
        catch( Exception e )
        {
            fail( "Unexpected exception creating resolver " + e );
        }
        finally
        {
            final Properties properties = System.getProperties();
            if( null == oldValue )
            {
                properties.remove( PARSER_KEY );
            }
            else
            {
                properties.setProperty( PARSER_KEY, oldValue );
            }
        }
    }

    public void testLoadPublicFromJar()
    {
        doLoadResource( TestData.PUBLIC_ID, null );
    }

    public void testLoadSystemFromJar()
    {
        doLoadResource( null, TestData.SYSTEM_ID );
    }

    private void doLoadResource( final String publicId, final String systemId )
    {
        final EntityResolver resolver = getResolver();
        try
        {
            final InputSource inputSource = resolver.resolveEntity( publicId, systemId );
            assertNotNull( "Expected a input source for id " + publicId + "/" + systemId,
                           inputSource );
        }
        catch( Exception e )
        {
            fail( "Unexpected exception resolving entity" + e );
        }
    }

    private EntityResolver getResolver()
    {
        final ClassLoader classLoader = createClassLoader();
        try
        {
            return ResolverFactory.createResolver( classLoader );
        }
        catch( Exception e )
        {
            fail( "Unexpected exception creating resolver " + e );
        }
        return null;
    }

    private ClassLoader createClassLoader()
    {
        final URL url = getClass().getClassLoader().getResource( CATALOG_JAR );
        assertNotNull( "ResourcePresent: " + CATALOG_JAR, url );
        return new URLClassLoader( new URL[]{url} );
    }
}
