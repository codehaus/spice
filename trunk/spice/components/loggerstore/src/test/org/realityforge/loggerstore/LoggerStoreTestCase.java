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
import java.util.HashMap;
import java.net.URL;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import junit.framework.TestCase;
import org.apache.avalon.framework.configuration.DefaultConfigurationBuilder;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.container.ContainerUtil;
import org.apache.avalon.framework.logger.ConsoleLogger;
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
    private static final String MESSAGE = "Testing Logger";
    private static final String MESSAGE2 = "This occurs in sub-category";

    private File m_logsDir;

    public LoggerStoreTestCase( final String name )
    {
        super( name );
    }

    protected void setUp() throws Exception
    {
        m_logsDir = new File( "logs/" );
        m_logsDir.mkdirs();
    }

    public void testNullRootLogger()
        throws Exception
    {
        final LoggerStore store = new MalformedLoggerStore();
        try
        {
            store.getLogger();
            fail( "Expected to get an exception as no root logger is defined." );
        }
        catch( final Exception e )
        {
        }
    }

    public void testConsoleLoggerStore()
        throws Exception
    {
        final LoggerStore store =
            new ConsoleLoggerStore( ConsoleLogger.LEVEL_DEBUG );
        performConsoleTest( store, ConsoleLogger.LEVEL_DEBUG );
    }

    public void testConsoleLoggerStoreNoDebug()
        throws Exception
    {
        final LoggerStore store =
            new ConsoleLoggerStore( ConsoleLogger.LEVEL_DEBUG );
        performConsoleTest( store, ConsoleLogger.LEVEL_DISABLED );
    }

    public void testJDK14LoggerStoreFactoryInvalidInput()
        throws Exception
    {
        runInvalidInputData( new Jdk14LoggerStoreFactory() );
    }

    public void testJDK14LoggerStoreFactoryWithProperties()
        throws Exception
    {
        final Properties properties = new Properties();
        properties.load( getResource( "logging.properties" ) );
        final HashMap config = new HashMap();
        config.put( Properties.class.getName(), properties );

        runFactoryTest( new Jdk14LoggerStoreFactory(),
                        ConsoleLogger.LEVEL_DEBUG,
                        config,
                        "jdk14" );
    }

    public void testJDK14LoggerStoreFactoryWithStreams()
        throws Exception
    {
        runStreamBasedFactoryTest( "logging.properties",
                                   new Jdk14LoggerStoreFactory(),
                                   ConsoleLogger.LEVEL_DEBUG,
                                   "jdk14",
                                   new HashMap() );
    }

    public void testJDK14Configuration()
        throws Exception
    {
        final LoggerStore store =
            new Jdk14LoggerStore( getResource( "logging.properties" ) );
        runLoggerTest( "jdk14", store, ConsoleLogger.LEVEL_DEBUG );
    }

    public void testJDK14ConfigurationNoDebug()
        throws Exception
    {
        final LoggerStore store =
            new Jdk14LoggerStore( getResource( "logging.properties" ) );
        runLoggerTest( "jdk14", store, ConsoleLogger.LEVEL_DISABLED );
    }

    public void testJDK14ConfigurationNoLog()
        throws Exception
    {
        final LoggerStore store =
            new Jdk14LoggerStore( getResource( "logging.properties" ) );
        runLoggerTest( "jdk14", store );
    }

    public void testLogKitLoggerStoreFactoryInvalidInput()
        throws Exception
    {
        runInvalidInputData( new LogKitLoggerStoreFactory() );
    }

    public void testLogKitLoggerStoreFactoryWithConfiguration()
        throws Exception
    {
        final DefaultConfigurationBuilder builder = new DefaultConfigurationBuilder();
        final HashMap config = new HashMap();
        config.put( Configuration.class.getName(), builder.build( getResource( "logkit.xml" ) ) );

        runFactoryTest( new LogKitLoggerStoreFactory(),
                        ConsoleLogger.LEVEL_DEBUG,
                        config,
                        "logkit" );
    }

    public void testLogKitLoggerStoreFactoryWithStreams()
        throws Exception
    {
        runStreamBasedFactoryTest( "logkit.xml",
                                   new LogKitLoggerStoreFactory(),
                                   ConsoleLogger.LEVEL_DEBUG,
                                   "logkit",
                                   new HashMap() );
    }

    public void testLogKitConfiguration()
        throws Exception
    {
        final DefaultConfigurationBuilder builder = new DefaultConfigurationBuilder();
        final LoggerStore store =
            new LogKitLoggerStore( builder.build( getResource( "logkit.xml" ) ) );
        runLoggerTest( "logkit", store, ConsoleLogger.LEVEL_DEBUG );
    }

    public void testLogKitConfigurationNoDebug()
        throws Exception
    {
        final DefaultConfigurationBuilder builder = new DefaultConfigurationBuilder();
        final LoggerStore store =
            new LogKitLoggerStore( builder.build( getResource( "logkit.xml" ) ) );
        runLoggerTest( "logkit", store, ConsoleLogger.LEVEL_DISABLED );
    }

    public void testLogKitConfigurationNoLog()
        throws Exception
    {
        final DefaultConfigurationBuilder builder = new DefaultConfigurationBuilder();
        final LoggerStore store =
            new LogKitLoggerStore( builder.build( getResource( "logkit.xml" ) ) );
        runLoggerTest( "logkit", store );
    }

    public void testLog4jLoggerStoreFactoryInvalidInput()
        throws Exception
    {
        runInvalidInputData( new Log4JLoggerStoreFactory() );
    }

    public void testLog4jLoggerStoreFactoryWithProperties()
        throws Exception
    {
        final Properties properties = new Properties();
        properties.load( getResource( "log4j.properties" ) );
        final HashMap config = new HashMap();
        config.put( Properties.class.getName(), properties );

        runFactoryTest( new Log4JLoggerStoreFactory(),
                        ConsoleLogger.LEVEL_DEBUG,
                        config,
                        "log4j-properties" );
    }

    public void testLog4jLoggerStoreFactoryWithElement()
        throws Exception
    {
        final HashMap config = new HashMap();
        final Element element =
            buildElement( getResource( "log4j.xml" ),
                          new org.apache.log4j.xml.Log4jEntityResolver(),
                          null );
        config.put( Element.class.getName(), element );

        runFactoryTest( new Log4JLoggerStoreFactory(),
                        ConsoleLogger.LEVEL_DEBUG,
                        config,
                        "log4j-xml" );
    }

    public void testLog4JLoggerStoreFactoryWithStreamsAsXML()
        throws Exception
    {
        runStreamBasedFactoryTest( "log4j.xml",
                                   new Log4JLoggerStoreFactory(),
                                   ConsoleLogger.LEVEL_DEBUG,
                                   "log4j-xml",
                                   new HashMap() );
    }

    public void testLog4JLoggerStoreFactoryWithStreamsAsProperties()
        throws Exception
    {
        final HashMap inputData = new HashMap();
        inputData.put( Log4JLoggerStoreFactory.CONFIGURATION_TYPE,
                       Log4JLoggerStoreFactory.PROPERTIES );
        runStreamBasedFactoryTest( "log4j.properties",
                                   new Log4JLoggerStoreFactory(),
                                   ConsoleLogger.LEVEL_DEBUG,
                                   "log4j-properties",
                                   inputData );
    }

    public void testLog4JElementConfiguration()
        throws Exception
    {
        final LoggerStore store =
            new Log4JLoggerStore( buildElement( getResource( "log4j.xml" ),
                                                new org.apache.log4j.xml.Log4jEntityResolver(), null ) );
        runLoggerTest( "log4j-xml", store, ConsoleLogger.LEVEL_DEBUG );
    }

    public void testLog4JElementConfigurationNoDebug()
        throws Exception
    {
        final LoggerStore store =
            new Log4JLoggerStore( buildElement( getResource( "log4j.xml" ),
                                                new org.apache.log4j.xml.Log4jEntityResolver(), null ) );
        runLoggerTest( "log4j-xml", store, ConsoleLogger.LEVEL_DISABLED );
    }

    public void testLog4JElementConfigurationNoLog()
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
        runLoggerTest( "log4j-xml", store, ConsoleLogger.LEVEL_DEBUG );
    }

    public void testLog4JInputStreamConfigurationNoDebug()
        throws Exception
    {
        final LoggerStore store =
            new Log4JLoggerStore( getResource( "log4j.xml" ) );
        runLoggerTest( "log4j-xml", store, ConsoleLogger.LEVEL_DISABLED );
    }

    public void testLog4JInputStreamConfigurationNoLog()
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
        runLoggerTest( "log4j-properties", store, ConsoleLogger.LEVEL_DEBUG );
    }

    public void testLog4JPropertiesConfigurationNoDebug()
        throws Exception
    {
        final Properties properties = new Properties();
        properties.load( getResource( "log4j.properties" ) );
        final LoggerStore store =
            new Log4JLoggerStore( properties );
        runLoggerTest( "log4j-properties", store, ConsoleLogger.LEVEL_DISABLED );
    }

    public void testLog4JPropertiesConfigurationNoLog()
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
                                  final LoggerStore store,
                                  final int level )
        throws Exception
    {
        ContainerUtil.enableLogging( store, new ConsoleLogger( level ) );
        runLoggerTest( filename, store );
    }

    private void runLoggerTest( final String filename,
                                final LoggerStore store )
        throws Exception
    {
        BufferedReader reader = null;
        try
        {
            final Logger logger = store.getLogger();
            assertNotNull( "rootLogger for " + filename, logger );
            logger.info( MESSAGE );
            final Logger noExistLogger = store.getLogger( "no-exist" );
            assertNotNull( "noExistLogger for " + filename, noExistLogger );
            noExistLogger.info( MESSAGE2 );

            assertEquals( "Same Logger returned multiple times:",
                          noExistLogger,
                          store.getLogger( "no-exist" ) );

            try
            {
                store.getLogger( null );
                fail( "Expected a NullPointerException when passing " +
                      "null in for getLogger parameter" );
            }
            catch( final NullPointerException npe )
            {
                assertEquals( "NullPointer message", "name", npe.getMessage() );
            }

            final File logFile = new File( m_logsDir, filename + ".log" );
            assertTrue( "Checking LogFile Exists: " + filename, logFile.exists() );

            reader = new BufferedReader( new InputStreamReader( new FileInputStream( logFile ) ) );
            assertEquals( "First line Contents for logger" + filename,
                          MESSAGE, reader.readLine() );
            assertEquals( "Second line Contents for logger" + filename,
                          MESSAGE2, reader.readLine() );
            assertNull( "Third Line Contents for logger" + filename,
                        reader.readLine() );
            reader.close();
            logFile.delete();

            if( !( store instanceof Jdk14LoggerStore ) )
            {
                final Logger nejney = store.getLogger( "nejney" );
                nejney.info( MESSAGE );

                final File logFile2 = new File( m_logsDir, filename + "2.log" );
                reader = new BufferedReader( new InputStreamReader( new FileInputStream( logFile2 ) ) );
                assertEquals( "First line Contents for nejney logger" + filename,
                              MESSAGE, reader.readLine() );
                assertNull( "Second Line Contents for nejney logger" + filename,
                            reader.readLine() );
                reader.close();
                logFile2.delete();
            }
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

    private void performConsoleTest( final LoggerStore store, final int level ) throws Exception
    {
        ContainerUtil.enableLogging( store, new ConsoleLogger( level ) );
        final Logger logger = store.getLogger();
        assertNotNull( "rootLogger for console", logger );
        logger.info( MESSAGE );
        final Logger noExistLogger = store.getLogger( "no-exist" );
        assertNotNull( "noExistLogger for console", noExistLogger );
        noExistLogger.info( MESSAGE2 );
        store.close();
    }

    private void runInvalidInputData( final LoggerStoreFactory factory )
    {
        try
        {
            factory.createLoggerStore( new HashMap() );
            fail( "Expected createLoggerStore to fail with invalid input data" );
        }
        catch( Exception e )
        {
        }
    }

    private void runStreamBasedFactoryTest( final String inputFile,
                                            final LoggerStoreFactory factory,
                                            final int level,
                                            final String outputFile,
                                            final HashMap inputData )
        throws Exception
    {
        //URL Should in file: format
        final URL url = getClass().getResource( inputFile );
        assertEquals( "URL is of file type", url.getProtocol(), "file" );

        final HashMap config = new HashMap();
        config.put( LoggerStoreFactory.URL_LOCATION,
                    url.toExternalForm() );
        config.putAll( inputData );
        runFactoryTest( factory,
                        level,
                        config,
                        outputFile );
        final HashMap config2 = new HashMap();
        config2.put( URL.class.getName(), url );
        config2.putAll( inputData );
        runFactoryTest( factory,
                        level,
                        config2,
                        outputFile );
        final String filename = url.toExternalForm().substring( 5 );
        final HashMap config3 = new HashMap();
        config3.put( LoggerStoreFactory.FILE_LOCATION, filename );
        config3.putAll( inputData );
        runFactoryTest( factory,
                        level,
                        config3,
                        outputFile );
        final HashMap config4 = new HashMap();
        config4.put( File.class.getName(), new File( filename ) );
        config4.putAll( inputData );
        runFactoryTest( factory,
                        level,
                        config4,
                        outputFile );
        final HashMap config5 = new HashMap();
        config5.put( InputStream.class.getName(),
                     new FileInputStream( filename ) );
        config5.putAll( inputData );
        runFactoryTest( factory,
                        level,
                        config5,
                        outputFile );
    }

    private void runFactoryTest( final LoggerStoreFactory factory, final int level, final HashMap config, final String filename ) throws Exception
    {
        ContainerUtil.enableLogging( factory, new ConsoleLogger( level ) );
        final LoggerStore store = factory.createLoggerStore( config );
        runLoggerTest( filename, store, level );
    }
}
