/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.loggerstore;

import java.util.HashMap;
import java.util.Properties;

import org.apache.avalon.excalibur.logger.SimpleLogKitManager;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.DefaultConfigurationBuilder;
import org.apache.avalon.framework.container.ContainerUtil;
import org.apache.avalon.framework.logger.ConsoleLogger;
import org.apache.avalon.framework.logger.Logger;
import org.apache.avalon.framework.logger.NullLogger;
import org.w3c.dom.Element;

/**
 *  Test case for LoggerStoreFactory
 *
 * @author <a href="mailto:mauro.talevi at aquilonia.org">Mauro Talevi</a>
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 */
public class LoggerStoreFactoryTestCase
    extends AbstractTestCase
{
    public LoggerStoreFactoryTestCase( final String name )
    {
        super( name );
    }

    // InitialLoggerStoreFactory tests
    public void testInitialLoggerStoreFactoryFromConfigurerClassLoader()
        throws Exception
    {
        Thread.currentThread().setContextClassLoader( null );
        final HashMap config = new HashMap();
        runFactoryTest( new InitialLoggerStoreFactory(),
                        ConsoleLogger.LEVEL_DEBUG,
                        config,
                        "log4j-properties" );
    }

    public void testInitialLoggerStoreFactoryUsingDefaults()
        throws Exception
    {
        final HashMap config = new HashMap();
        config.put( ClassLoader.class.getName(),
                    ClassLoader.getSystemClassLoader().getParent() );
        final InitialLoggerStoreFactory factory = new InitialLoggerStoreFactory();
        ContainerUtil.enableLogging( factory, new ConsoleLogger( ConsoleLogger.LEVEL_DEBUG ) );
        try
        {
            factory.createLoggerStore( config );
            fail( "Expected to not be able to create LoggerStoreFactory as no type was specified or on ClassPath" );
        }
        catch( final Exception e )
        {
        }
    }

    public void testInitialLoggerStoreFactoryUsingSpecifiedType()
        throws Exception
    {
        final HashMap config = new HashMap();
        config.put( InitialLoggerStoreFactory.INITIAL_FACTORY,
                    ConsoleLoggerStoreFactory.class.getName() );
        final InitialLoggerStoreFactory factory = new InitialLoggerStoreFactory();
        ContainerUtil.enableLogging( factory, new ConsoleLogger( ConsoleLogger.LEVEL_DEBUG ) );
        final LoggerStore store = factory.createLoggerStore( config );
        performConsoleTest( store, ConsoleLogger.LEVEL_DEBUG );
    }

    public void testInitialLoggerStoreFactoryWithInvalidType()
        throws Exception
    {
        final HashMap config = new HashMap();
        config.put( InitialLoggerStoreFactory.INITIAL_FACTORY, "Blah" );
        final InitialLoggerStoreFactory factory = new InitialLoggerStoreFactory();
        ContainerUtil.enableLogging( factory, new ConsoleLogger( ConsoleLogger.LEVEL_DEBUG ) );
        try
        {
            factory.createLoggerStore( config );
            fail( "Expected exception as invalid type specified" );
        }
        catch( final Exception e )
        {
        }
    }

    public void testInitialLoggerStoreFactoryFromSpecifiedClassLoader()
        throws Exception
    {
        Thread.currentThread().setContextClassLoader( null );
        final HashMap config = new HashMap();
        config.put( ClassLoader.class.getName(),
                    InitialLoggerStoreFactory.class.getClassLoader() );
        runFactoryTest( new InitialLoggerStoreFactory(),
                        ConsoleLogger.LEVEL_DEBUG,
                        config,
                        "log4j-properties" );
    }

    public void testInitialLoggerStoreFactoryFromContextClassLoader()
        throws Exception
    {
        Thread.currentThread().setContextClassLoader( InitialLoggerStoreFactory.class.getClassLoader() );
        final HashMap config = new HashMap();
        runFactoryTest( new InitialLoggerStoreFactory(),
                        ConsoleLogger.LEVEL_DEBUG,
                        config,
                        "log4j-properties" );
    }


    // LogKitLoggerStoreFactory tests
    public void testLogKitLoggerStoreFactoryInvalidInput()
        throws Exception
    {
        runInvalidInputData( new LogKitLoggerStoreFactory() );
    }

    public void testLogKitLoggerStoreFactoryWithConfigurationAndDefaultLoggerManager()
        throws Exception
    {
        final DefaultConfigurationBuilder builder = new DefaultConfigurationBuilder();
        final HashMap config = new HashMap();
        config.put( Configuration.class.getName(),
                    builder.build( getResource( "logkit-excalibur.xml" ) ) );
        config.put( Logger.class.getName(), new NullLogger() );

        runFactoryTest( new LogKitLoggerStoreFactory(),
                        ConsoleLogger.LEVEL_INFO,
                        config,
                        "logkit-excalibur" );
    }

    public void testLogKitLoggerStoreFactoryWithConfigurationAndLoggerManager()
            throws Exception
    {
        final DefaultConfigurationBuilder builder = new DefaultConfigurationBuilder();
        final HashMap config = new HashMap();
        config.put( Configuration.class.getName(),
                    builder.build( getResource( "logkit-simple.xml" ) ) );
        config.put( LogKitLoggerStoreFactory.LOGGER_MANAGER,
                    SimpleLogKitManager.class.getName() );

        runFactoryTest( new LogKitLoggerStoreFactory(),
                        ConsoleLogger.LEVEL_INFO,
                        config,
                        "logkit-simple" );
    }

    public void testLogKitLoggerStoreFactoryWithStreamsAndDefaultLoggerManager()
        throws Exception
    {
        final HashMap config = new HashMap();
        runStreamBasedFactoryTest( "logkit-excalibur.xml",
                                   new LogKitLoggerStoreFactory(),
                                   ConsoleLogger.LEVEL_INFO,
                                   "logkit-excalibur",
                                   config );
    }

    public void testLogKitLoggerStoreFactoryWithStreamsAndLoggerManager()
        throws Exception
    {

        final HashMap config = new HashMap();
        config.put( LogKitLoggerStoreFactory.LOGGER_MANAGER,
                    SimpleLogKitManager.class.getName() );
        runStreamBasedFactoryTest( "logkit-simple.xml",
                                   new LogKitLoggerStoreFactory(),
                                   ConsoleLogger.LEVEL_INFO,
                                   "logkit-simple",
                                   config );
    }

    public void testExcaliburLogKitLoggerStoreFactoryInvalidInput()
        throws Exception
    {
        runInvalidInputData( new ExcaliburLogKitLoggerStoreFactory() );
    }

    public void testExcaliburLogKitLoggerStoreFactoryWithConfiguration()
        throws Exception
    {
        final DefaultConfigurationBuilder builder = new DefaultConfigurationBuilder();
        final HashMap config = new HashMap();
        config.put( Configuration.class.getName(),
                    builder.build( getResource( "logkit-excalibur.xml" ) ) );
        config.put( Logger.class.getName(), new NullLogger() );

        runFactoryTest( new ExcaliburLogKitLoggerStoreFactory(),
                        ConsoleLogger.LEVEL_INFO,
                        config,
                        "logkit-excalibur" );
    }

    public void testExcaliburLogKitLoggerStoreFactoryWithStreams()
        throws Exception
    {
        runStreamBasedFactoryTest( "logkit-excalibur.xml",
                                   new ExcaliburLogKitLoggerStoreFactory(),
                                   ConsoleLogger.LEVEL_INFO,
                                   "logkit-excalibur",
                                   new HashMap() );
    }

    public void testSimpleLogKitLoggerStoreFactoryInvalidInput()
        throws Exception
    {
        runInvalidInputData( new SimpleLogKitLoggerStoreFactory() );
    }

    public void testSimpleLogKitLoggerStoreFactoryWithConfiguration()
        throws Exception
    {
        final DefaultConfigurationBuilder builder = new DefaultConfigurationBuilder();
        final HashMap config = new HashMap();
        config.put( Configuration.class.getName(),
                    builder.build( getResource( "logkit-simple.xml" ) ) );
        config.put( Logger.class.getName(), new NullLogger() );

        runFactoryTest( new SimpleLogKitLoggerStoreFactory(),
                        ConsoleLogger.LEVEL_INFO,
                        config,
                        "logkit-simple" );
    }

    public void testSimpleLogKitLoggerStoreFactoryWithStreams()
        throws Exception
    {
        final HashMap config = new HashMap();
        runStreamBasedFactoryTest( "logkit-simple.xml",
                                   new SimpleLogKitLoggerStoreFactory(),
                                   ConsoleLogger.LEVEL_INFO,
                                   "logkit-simple",
                                   config );
    }

    // Log4JLoggerStoreFactory tests
    public void testPropertyLog4jLoggerStoreFactoryInvalidInput()
        throws Exception
    {
        runInvalidInputData( new PropertyLog4JLoggerStoreFactory() );
    }

    public void testDOMLog4jLoggerStoreFactoryInvalidInput()
        throws Exception
    {
        runInvalidInputData( new DOMLog4JLoggerStoreFactory() );
    }

    public void testLog4jLoggerStoreFactoryWithProperties()
        throws Exception
    {
        final Properties properties = new Properties();
        properties.load( getResource( "log4j.properties" ) );
        final HashMap config = new HashMap();
        config.put( Properties.class.getName(), properties );

        runFactoryTest( new PropertyLog4JLoggerStoreFactory(),
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

        runFactoryTest( new DOMLog4JLoggerStoreFactory(),
                        ConsoleLogger.LEVEL_DEBUG,
                        config,
                        "log4j-xml" );
    }

    public void testLog4JLoggerStoreFactoryWithStreamsAsXML()
        throws Exception
    {
        runStreamBasedFactoryTest( "log4j.xml",
                                   new DOMLog4JLoggerStoreFactory(),
                                   ConsoleLogger.LEVEL_DEBUG,
                                   "log4j-xml",
                                   new HashMap() );
    }

    public void testLog4JLoggerStoreFactoryWithStreamsAsProperties()
        throws Exception
    {
        final HashMap inputData = new HashMap();
        runStreamBasedFactoryTest( "log4j.properties",
                                   new PropertyLog4JLoggerStoreFactory(),
                                   ConsoleLogger.LEVEL_DEBUG,
                                   "log4j-properties",
                                   inputData );
    }

    // JDK14LoggerStoreFactory tests
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

}
