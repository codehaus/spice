/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.loggerstore;

import java.io.InputStream;

import junit.framework.TestCase;

import org.apache.avalon.framework.logger.Logger;

/**
 *  Test case for LoggerStore
 *
 * @author <a href="mailto:mauro.talevi at aquilonia.org">Mauro Talevi</a>
 * @author <a href="mailto:peter at apache.org">Peter Donald</a>
 */
public class LoggerStoreTestCase
    extends TestCase
{
    public LoggerStoreTestCase( final String name )
    {
        super( name );
    }

    public void testJDK14Configuration()
        throws Exception
    {
        final LoggerStore store =
            new Jdk14LoggerStore( getResource( "logging.properties" ) );
        runLoggerTest( "JDK14", store );
    }

    public void testLogKitConfiguration()
        throws Exception
    {
        final LoggerStore store =
            new LogKitLoggerStore( Configurator.buildConfiguration( getResource( "logkit.xml" ) ) );
        runLoggerTest( "LogKit", store );
    }

    public void testLog4JElementConfiguration()
        throws Exception
    {
        final LoggerStore store =
            new Log4JLoggerStore( Configurator.buildElement( getResource( "log4j.xml" ),
                                  new org.apache.log4j.xml.Log4jEntityResolver(), null ) );
        runLoggerTest( "Log4jElement", store );
    }
    
    public void testLog4JInputStreamConfiguration()
        throws Exception
    {
        final LoggerStore store =
            new Log4JLoggerStore( getResource( "log4j.xml" ) );
        runLoggerTest( "Log4jInputStream", store );
    }
    
    public void testLog4JPropertiesConfiguration()
        throws Exception
    {
        final LoggerStore store =
            new Log4JLoggerStore( Configurator.buildProperties( getResource( "log4j.properties" ) ) );
        runLoggerTest( "Log4jProperties", store );
    }
    
    protected final InputStream getResource( final String name )
    {
        return getClass().getResourceAsStream( name );
    }

    protected void runLoggerTest( final String prefix,
                                  final LoggerStore store )
        throws Exception
    {
        final Logger logger = store.getLogger();
        assertNotNull( "rootLogger", logger );
        logger.info( "Testing Logger" );
    }
}
