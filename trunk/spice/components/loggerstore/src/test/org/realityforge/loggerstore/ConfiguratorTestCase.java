/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.loggerstore;

import org.apache.avalon.framework.logger.ConsoleLogger;

/**
 *  Test case for Configurator
 *
 * @author <a href="mailto:mauro.talevi at aquilonia.org">Mauro Talevi</a>
 * @author <a href="mailto:peter at apache.org">Peter Donald</a>
 */
public class ConfiguratorTestCase
    extends AbstractTestCase
{
    public ConfiguratorTestCase( final String name )
    {
        super( name );
    }
    
    public void testJDK14Configurator()
        throws Exception
    {
        runLoggerTest( "jdk14", Configurator.createLoggerStore( Configurator.JDK14, "org/realityforge/loggerstore/logging.properties" ),
                       ConsoleLogger.LEVEL_DEBUG );
        runLoggerTest( "jdk14", Configurator.createLoggerStore( Configurator.JDK14, getResource( "logging.properties" )),
                       ConsoleLogger.LEVEL_DEBUG );
    }

    public void testJDK14ConfiguratorNoDebug()
        throws Exception
    {
        runLoggerTest( "jdk14", Configurator.createLoggerStore( Configurator.JDK14, "org/realityforge/loggerstore/logging.properties" ),
                       ConsoleLogger.LEVEL_DISABLED );
        runLoggerTest( "jdk14", Configurator.createLoggerStore( Configurator.JDK14, getResource( "logging.properties" )),
                       ConsoleLogger.LEVEL_DISABLED );
    }

    public void testJDK14ConfiguratorNoLog()
        throws Exception
    {
        runLoggerTest( "jdk14", Configurator.createLoggerStore( Configurator.JDK14, "org/realityforge/loggerstore/logging.properties" ) );
        runLoggerTest( "jdk14", Configurator.createLoggerStore( Configurator.JDK14, getResource( "logging.properties" )) );
    }

    public void testLogKitConfigurator()
        throws Exception
    {
        runLoggerTest( "logkit", Configurator.createLoggerStore( Configurator.LOGKIT, "org/realityforge/loggerstore/logkit.xml" ),
                       ConsoleLogger.LEVEL_DEBUG );
        runLoggerTest( "logkit", Configurator.createLoggerStore( Configurator.LOGKIT, getResource( "logkit.xml" )),
                       ConsoleLogger.LEVEL_DEBUG );
    }

    public void testLogKitConfiguratorNoDebug()
        throws Exception
    {
        runLoggerTest( "logkit", Configurator.createLoggerStore( Configurator.LOGKIT, "org/realityforge/loggerstore/logkit.xml" ),
                       ConsoleLogger.LEVEL_DISABLED );
        runLoggerTest( "logkit", Configurator.createLoggerStore( Configurator.LOGKIT, getResource( "logkit.xml" )),
                       ConsoleLogger.LEVEL_DISABLED );
     }

    public void testLogKitConfiguratorNoLog()
        throws Exception
    {
        runLoggerTest( "logkit", Configurator.createLoggerStore( Configurator.LOGKIT, "org/realityforge/loggerstore/logkit.xml" ) );
        runLoggerTest( "logkit", Configurator.createLoggerStore( Configurator.LOGKIT, getResource( "logkit.xml" )) );
     }

  
    public void testLog4JDOMConfigurator()
        throws Exception
    {
        runLoggerTest( "log4j-xml", Configurator.createLoggerStore( Configurator.LOG4J_DOM, "org/realityforge/loggerstore/log4j.xml" ),
                       ConsoleLogger.LEVEL_DEBUG );
        runLoggerTest( "log4j-xml", Configurator.createLoggerStore( Configurator.LOG4J_DOM, getResource( "log4j.xml" )),
                       ConsoleLogger.LEVEL_DEBUG );
    }

    public void testLog4JDOMConfiguratorNoDebug()
        throws Exception
    {
        runLoggerTest( "log4j-xml", Configurator.createLoggerStore( Configurator.LOG4J_DOM, "org/realityforge/loggerstore/log4j.xml" ),
                       ConsoleLogger.LEVEL_DISABLED );
        runLoggerTest( "log4j-xml", Configurator.createLoggerStore( Configurator.LOG4J_DOM, getResource( "log4j.xml" )),
                       ConsoleLogger.LEVEL_DISABLED );
    }

    public void testLog4JDOMConfiguratorNoLog()
        throws Exception
    {
        runLoggerTest( "log4j-xml", Configurator.createLoggerStore( Configurator.LOG4J_DOM, "org/realityforge/loggerstore/log4j.xml" ) );
        runLoggerTest( "log4j-xml", Configurator.createLoggerStore( Configurator.LOG4J_DOM, getResource( "log4j.xml" )) );
    }

    public void testLog4JPropertyConfigurator()
        throws Exception
    {
        runLoggerTest( "log4j-properties", Configurator.createLoggerStore( Configurator.LOG4J_PROPERTY, "org/realityforge/loggerstore/log4j.properties" ),
                       ConsoleLogger.LEVEL_DEBUG );
        runLoggerTest( "log4j-properties", Configurator.createLoggerStore( Configurator.LOG4J_PROPERTY, getResource( "log4j.properties" )),
                       ConsoleLogger.LEVEL_DEBUG );
    }

    public void testLog4JPropertyConfiguratorNoDebug()
        throws Exception
    {
        runLoggerTest( "log4j-properties", Configurator.createLoggerStore( Configurator.LOG4J_PROPERTY, "org/realityforge/loggerstore/log4j.properties" ),
                       ConsoleLogger.LEVEL_DISABLED );
        runLoggerTest( "log4j-properties", Configurator.createLoggerStore( Configurator.LOG4J_PROPERTY, getResource( "log4j.properties" )),
                       ConsoleLogger.LEVEL_DISABLED );
    }

    public void testLog4JPropertyConfiguratorNoLog()
        throws Exception
    {
        runLoggerTest( "log4j-properties", Configurator.createLoggerStore( Configurator.LOG4J_PROPERTY, "org/realityforge/loggerstore/log4j.properties" ) );
        runLoggerTest( "log4j-properties", Configurator.createLoggerStore( Configurator.LOG4J_PROPERTY, getResource( "log4j.properties" )) );
    }


}
