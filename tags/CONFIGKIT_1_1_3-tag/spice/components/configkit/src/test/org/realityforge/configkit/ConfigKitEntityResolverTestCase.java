/*
 * Copyright  The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 */
package org.realityforge.configkit;

import junit.framework.TestCase;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import java.io.IOException;

/**
 * Basic unit tests for the multiplexing streams.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 */
public final class ConfigKitEntityResolverTestCase
    extends TestCase
{
    private ClassLoader m_contextClassLoader;

    public ConfigKitEntityResolverTestCase( final String name )
    {
        super( name );
    }

    protected void setUp() throws Exception
    {
        m_contextClassLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader( null );
    }

    protected void tearDown() throws Exception
    {
        Thread.currentThread().setContextClassLoader( m_contextClassLoader );
    }

    public void testNullInfos()
    {
        try
        {
            new ConfigKitEntityResolver( null, getClassLoader() );
        }
        catch( NullPointerException npe )
        {
            assertEquals( "NPE for null infos", "infos", npe.getMessage() );
            return;
        }

        fail( "Expected NPE due to null infos" );
    }

    public void testBadResource()
    {
        final EntityInfo info =
            new EntityInfo( TestData.PUBLIC_ID, TestData.SYSTEM_ID, "noexist.txt" );
        final ConfigKitEntityResolver resolver =
            new ConfigKitEntityResolver( new EntityInfo[]{info}, getClassLoader() );
        try
        {
            resolver.resolveEntity( TestData.PUBLIC_ID, TestData.SYSTEM_ID );
            fail( "Expected IOException as resource no exist" );
        }
        catch( final SAXException se )
        {
            fail( "Unexpected SAXException: " + se );
        }
        catch( final IOException ioe )
        {
            return;
        }
    }

    public void testNullClassLoader()
    {
        new ConfigKitEntityResolver( new EntityInfo[]{TestData.INFO}, null );
    }

    public void testEmptyResolver()
    {
        new ConfigKitEntityResolver( new EntityInfo[ 0 ], getClassLoader() );
    }

    public void testRetrieveBySystemId()
    {
        doRetrievalTest( null, TestData.SYSTEM_ID, true, getClassLoader() );
    }

    public void testRetrieveByPublicId()
    {
        doRetrievalTest( TestData.PUBLIC_ID, null, true, getClassLoader() );
    }

    public void testNonRetrieveByPublicId()
    {
        doRetrievalTest( "no exist", null, false, getClassLoader() );
    }

    public void testNonRetrieveBySystemId()
    {
        doRetrievalTest( null, "no exist", false, getClassLoader() );
    }

    public void testNoClassLoaderRetrieveBySystemId()
    {
        doRetrievalTest( null, TestData.SYSTEM_ID, true, null );
    }

    public void testNoClassLoaderRetrieveByPublicId()
    {
        doRetrievalTest( TestData.PUBLIC_ID, null, true, null );
    }

    public void testNoClassLoaderNonRetrieveByPublicId()
    {
        doRetrievalTest( "no exist", null, false, null );
    }

    public void testNoClassLoaderNonRetrieveBySystemId()
    {
        doRetrievalTest( null, "no exist", false, null );
    }

    public void testCtxClassLoaderRetrieveBySystemId()
    {
        Thread.currentThread().setContextClassLoader( getClassLoader() );
        doRetrievalTest( null, TestData.SYSTEM_ID, true, null );
    }

    public void testCtxClassLoaderRetrieveByPublicId()
    {
        Thread.currentThread().setContextClassLoader( getClassLoader() );
        doRetrievalTest( TestData.PUBLIC_ID, null, true, null );
    }

    public void testCtxClassLoaderNonRetrieveByPublicId()
    {
        Thread.currentThread().setContextClassLoader( getClassLoader() );
        doRetrievalTest( "no exist", null, false, null );
    }

    public void testCtxClassLoaderNonRetrieveBySystemId()
    {
        Thread.currentThread().setContextClassLoader( getClassLoader() );
        doRetrievalTest( null, "no exist", false, null );
    }

    private void doRetrievalTest( final String publicID,
                                  final String systemID,
                                  final boolean shouldExist,
                                  final ClassLoader classLoader )
    {
        final ConfigKitEntityResolver resolver =
            new ConfigKitEntityResolver( new EntityInfo[]{TestData.INFO}, classLoader );
        try
        {
            final InputSource inputSource = resolver.resolveEntity( publicID, systemID );
            if( shouldExist )
            {
                assertNotNull( "Expected to find resource for " + getIdString( publicID, systemID ),
                               inputSource );
            }
            else
            {
                assertNull( "Not expected to find resource for " + getIdString( publicID, systemID ),
                            inputSource );
            }
        }
        catch( final IOException ioe )
        {
            fail( "Error loading resource for " + getIdString( publicID, systemID ) +
                  " due to " + ioe );
        }
        catch( final SAXException se )
        {
            fail( "Error finding resolver resource for " + getIdString( publicID, systemID ) +
                  " due to " + se );
        }
    }

    private String getIdString( final String publicId, final String systemId )
    {
        if( null == publicId )
        {
            return "systemID " + systemId;
        }
        else if( null == systemId )
        {
            return "publicID " + publicId;
        }
        else
        {
            return "id " + publicId + "/" + systemId;
        }
    }

    private ClassLoader getClassLoader()
    {
        return getClass().getClassLoader();
    }
}

