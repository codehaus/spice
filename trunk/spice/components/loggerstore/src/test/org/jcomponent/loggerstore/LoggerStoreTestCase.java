/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.loggerstore;

import java.util.Properties;

import org.apache.avalon.excalibur.logger.Log4JLoggerManager;
import org.apache.avalon.excalibur.logger.LogKitLoggerManager;
import org.apache.avalon.excalibur.logger.LoggerManager;
import org.apache.avalon.framework.configuration.DefaultConfigurationBuilder;
import org.apache.avalon.framework.context.DefaultContext;
import org.apache.avalon.framework.logger.NullLogger;
import org.jcontainer.dna.impl.ConsoleLogger;
import org.jcomponent.loggerstore.stores.ConsoleLoggerStore;
import org.jcomponent.loggerstore.stores.Jdk14LoggerStore;
import org.jcomponent.loggerstore.stores.Log4JLoggerStore;
import org.jcomponent.loggerstore.stores.LogKitLoggerStore;

/**
 *  Test case for LoggerStore
 *
 * @author <a href="mailto:mauro.talevi at aquilonia.org">Mauro Talevi</a>
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 */
public class LoggerStoreTestCase
    extends AbstractTestCase
{

    public LoggerStoreTestCase( final String name )
    {
        super( name );
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

    // ConsoleLoggerStore tests
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
        performConsoleTest( store, ConsoleLogger.LEVEL_NONE );
    }


    // LogKitLoggerStore tests
    public void testLogKitExcaliburConfiguration()
        throws Exception
    {
        final LoggerManager loggerManager = new LogKitLoggerManager();
        final DefaultConfigurationBuilder builder = new DefaultConfigurationBuilder();
        final LoggerStore store =
            new LogKitLoggerStore( loggerManager, null, null, builder.build( getResource( "logkit-excalibur.xml" ) ) );
        runLoggerTest( "logkit-excalibur", store, ConsoleLogger.LEVEL_DEBUG );
    }

    public void testLogKitExcaliburConfigurationWithLogger()
        throws Exception
    {
        final LoggerManager loggerManager = new LogKitLoggerManager();
        final DefaultConfigurationBuilder builder = new DefaultConfigurationBuilder();
        final LoggerStore store =
            new LogKitLoggerStore( loggerManager, new NullLogger(), null, builder.build( getResource( "logkit-excalibur.xml" ) ) );
        runLoggerTest( "logkit-excalibur", store, ConsoleLogger.LEVEL_DEBUG );
    }

    public void testLogKitExcaliburConfigurationWithContext()
        throws Exception
    {
        final LoggerManager loggerManager = new LogKitLoggerManager();
        final DefaultConfigurationBuilder builder = new DefaultConfigurationBuilder();
        final LoggerStore store =
            new LogKitLoggerStore( loggerManager, null, new DefaultContext(), builder.build( getResource( "logkit-excalibur.xml" ) ) );
        runLoggerTest( "logkit-excalibur", store, ConsoleLogger.LEVEL_DEBUG );
    }

    public void testLogKitExcaliburConfigurationNoDebug()
        throws Exception
    {
        final LoggerManager loggerManager = new LogKitLoggerManager();
        final DefaultConfigurationBuilder builder = new DefaultConfigurationBuilder();
        final LoggerStore store =
            new LogKitLoggerStore( loggerManager, null, null, builder.build( getResource( "logkit-excalibur.xml" ) ) );
        runLoggerTest( "logkit-excalibur", store, ConsoleLogger.LEVEL_NONE );
    }

    public void testLogKitExcaliburConfigurationNoLog()
        throws Exception
    {
        final LoggerManager loggerManager = new LogKitLoggerManager();
        final DefaultConfigurationBuilder builder = new DefaultConfigurationBuilder();
        final LoggerStore store =
            new LogKitLoggerStore( loggerManager, null, null, builder.build( getResource( "logkit-excalibur.xml" ) ) );
        runLoggerTest( "logkit-excalibur", store );
    }

    public void testLogKitExcaliburConfigurationNoManager()
        throws Exception
    {
        try
        {
            final LoggerStore store =
                new LogKitLoggerStore( null, null, null, null);
            fail( "Expected to get an exception as LoggerManager is null." );
        }
        catch( final Exception e )
        {
        }
    }

    public void testLogKitExcaliburConfigurationInvalidManager()
        throws Exception
    {
        try
        {
            final LoggerManager loggerManager = new Log4JLoggerManager();
            final DefaultConfigurationBuilder builder = new DefaultConfigurationBuilder();
            final LoggerStore store =
                new LogKitLoggerStore( loggerManager, null, null, builder.build( getResource( "log4j.xml" ) ) );
            fail( "Expected to get an exception as LoggerManager is invalid." );
        }
        catch( final Exception e )
        {
        }
    }

    // Log4JLoggerStore tests
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
        runLoggerTest( "log4j-xml", store, ConsoleLogger.LEVEL_NONE );
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
        runLoggerTest( "log4j-xml", store, ConsoleLogger.LEVEL_NONE );
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
        runLoggerTest( "log4j-properties", store, ConsoleLogger.LEVEL_NONE );
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

    // JDK14LoggerStore tests
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
        runLoggerTest( "jdk14", store, ConsoleLogger.LEVEL_NONE );
    }

    public void testJDK14ConfigurationNoLog()
        throws Exception
    {
        final LoggerStore store =
            new Jdk14LoggerStore( getResource( "logging.properties" ) );
        runLoggerTest( "jdk14", store );
    }

}
