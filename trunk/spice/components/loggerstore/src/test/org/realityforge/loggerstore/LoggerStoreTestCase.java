/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.loggerstore;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import junit.framework.TestCase;
import org.apache.avalon.framework.configuration.DefaultConfigurationBuilder;
import org.apache.avalon.framework.logger.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

/**
 *  Test case for LoggerStore
 *
 * @author <a href="mailto:mauro.talevi at aquilonia.org">Mauro Talevi</a>
 * @author <a href="mailto:peter at apache.org">Peter Donald</a>
 */
public class LoggerStoreTestCase
    extends TestCase
{
    private File m_logsDir;
    private static final String MESSAGE = "Testing Logger";

    public LoggerStoreTestCase( final String name )
    {
        super( name );
    }

    protected void setUp() throws Exception
    {
        m_logsDir = new File( "logs/" );
        m_logsDir.mkdirs();
/*
        final File[] files = m_logsDir.listFiles();
        for( int i = 0; i < files.length; i++ )
        {
            files[ i ].delete();
        }
*/
    }

    public void testJDK14Configuration()
        throws Exception
    {
        final LoggerStore store =
            new Jdk14LoggerStore( getResource( "logging.properties" ) );
        runLoggerTest( "jdk14", store );
    }

    public void testLogKitConfiguration()
        throws Exception
    {
        final DefaultConfigurationBuilder builder = new DefaultConfigurationBuilder();
        final LoggerStore store =
            new LogKitLoggerStore( builder.build( getResource( "logkit.xml" ) ) );
        runLoggerTest( "logkit", store );
    }

    public void testLog4JElementConfiguration()
        throws Exception
    {
        final LoggerStore store =
            new Log4JLoggerStore( buildElement( getResource( "log4j.xml" ),
                                                new org.apache.log4j.xml.Log4jEntityResolver(), null ) );
        runLoggerTest( "log4j-xml", store );
    }

    public void testLog4JInputStreamConfiguration()
        throws Exception
    {
        final LoggerStore store =
            new Log4JLoggerStore( getResource( "log4j.xml" ) );
        runLoggerTest( "log4j-xml", store );
    }

    public void testLog4JPropertiesConfiguration()
        throws Exception
    {
        final Properties properties = new Properties();
        properties.load( getResource( "log4j.properties" ) );
        final LoggerStore store =
            new Log4JLoggerStore( properties );
        runLoggerTest( "log4j-properties", store );
    }

    protected final InputStream getResource( final String name )
    {
        return getClass().getResourceAsStream( name );
    }

    protected void runLoggerTest( final String filename,
                                  final LoggerStore store )
        throws Exception
    {
        BufferedReader reader = null;
        try
        {
            final Logger logger = store.getLogger();
            assertNotNull( "rootLogger for " + filename, logger );
            logger.info( MESSAGE );

            final File logFile = new File( m_logsDir, filename + ".log" );
            assertTrue( "Checking LogFile Exists: " + filename, logFile.exists() );

            reader = new BufferedReader( new InputStreamReader( new FileInputStream( logFile ) ) );
            final String line = reader.readLine();
            assertEquals( "First line Contents for logger" + filename,
                          MESSAGE, line );
            assertNull( "Second Line Contents for logger" + filename,
                        reader.readLine() );
        }
        finally
        {
            store.close();
            if( null != reader )
            {
                reader.close();
            }
        }
    }

    /**
     *  Builds an Element from a resource
     *  @param resource the InputStream of the configuration resource
     *  @param resolver the EntityResolver required by the DocumentBuilder -
     *                  or <code>null</code> if none required
     *  @param systemId the String encoding the systemId required by the InputSource -
     *                  or <code>null</code> if none required
     */
    private static Element buildElement( final InputStream resource,
                                         final EntityResolver resolver,
                                         final String systemId )
        throws Exception
    {
        DocumentBuilderFactory dbf = null;
        try
        {
            dbf = DocumentBuilderFactory.newInstance();
        }
        catch( FactoryConfigurationError e )
        {
            final String message = "Failed to create a DocumentBuilderFactory";
            throw new Exception( message, e );
        }

        try
        {
            dbf.setValidating( true );
            DocumentBuilder db = dbf.newDocumentBuilder();
            if( resolver != null )
            {
                db.setEntityResolver( resolver );
            }
            InputSource source = new InputSource( resource );
            if( systemId != null )
            {
                source.setSystemId( systemId );
            }
            Document doc = db.parse( source );
            return doc.getDocumentElement();
        }
        catch( Exception e )
        {
            final String message = "Failed to parse Document";
            throw new Exception( message, e );
        }
    }
}
