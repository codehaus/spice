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
    
    public void testInvalidConfiguratorType()
        throws Exception
    {
        try
        {
            Configurator.createLoggerStore( "blah", "org/realityforge/loggerstore/logging.properties" );
            fail( "Expected exception as invalid type specified" );
        }
        catch( final Exception e )
        {
        }
    }
    
    public void testLogKitExcaliburConfigurator()
        throws Exception
    {
        runLoggerTest( "logkit-excalibur", Configurator.createLoggerStore( Configurator.LOGKIT_EXCALIBUR, "org/realityforge/loggerstore/logkit-excalibur.xml" ),
                       ConsoleLogger.LEVEL_DEBUG );
        runLoggerTest( "logkit-excalibur", Configurator.createLoggerStore( Configurator.LOGKIT_EXCALIBUR, getResource( "logkit-excalibur.xml" )),
                       ConsoleLogger.LEVEL_DEBUG );
    }

    public void testLogKitExcaliburConfiguratorNoDebug()
        throws Exception
    {
        runLoggerTest( "logkit-excalibur", Configurator.createLoggerStore( Configurator.LOGKIT_EXCALIBUR, "org/realityforge/loggerstore/logkit-excalibur.xml" ),
                       ConsoleLogger.LEVEL_DISABLED );
        runLoggerTest( "logkit-excalibur", Configurator.createLoggerStore( Configurator.LOGKIT_EXCALIBUR, getResource( "logkit-excalibur.xml" )),
                       ConsoleLogger.LEVEL_DISABLED );
     }

    public void testLogKitExcaliburConfiguratorNoLog()
        throws Exception
    {
        runLoggerTest( "logkit-excalibur", Configurator.createLoggerStore( Configurator.LOGKIT_EXCALIBUR, "org/realityforge/loggerstore/logkit-excalibur.xml" ) );
        runLoggerTest( "logkit-excalibur", Configurator.createLoggerStore( Configurator.LOGKIT_EXCALIBUR, getResource( "logkit-excalibur.xml" )) );
     }

    public void testLogKitSimpleConfigurator()
        throws Exception
    {
        runLoggerTest( "logkit-simple", Configurator.createLoggerStore( Configurator.LOGKIT_SIMPLE, "org/realityforge/loggerstore/logkit-simple.xml" ),
                       ConsoleLogger.LEVEL_DEBUG );
        runLoggerTest( "logkit-simple", Configurator.createLoggerStore( Configurator.LOGKIT_SIMPLE, getResource( "logkit-simple.xml" )),
                       ConsoleLogger.LEVEL_DEBUG );
    }

    public void testLogKitSimpleConfiguratorNoDebug()
        throws Exception
    {
        runLoggerTest( "logkit-simple", Configurator.createLoggerStore( Configurator.LOGKIT_SIMPLE, "org/realityforge/loggerstore/logkit-simple.xml" ),
                       ConsoleLogger.LEVEL_DISABLED );
        runLoggerTest( "logkit-simple", Configurator.createLoggerStore( Configurator.LOGKIT_SIMPLE, getResource( "logkit-simple.xml" )),
                       ConsoleLogger.LEVEL_DISABLED );
     }

    public void testLogKitSimpleConfiguratorNoLog()
        throws Exception
    {
        runLoggerTest( "logkit-simple", Configurator.createLoggerStore( Configurator.LOGKIT_SIMPLE, "org/realityforge/loggerstore/logkit-simple.xml" ) );
        runLoggerTest( "logkit-simple", Configurator.createLoggerStore( Configurator.LOGKIT_SIMPLE, getResource( "logkit-simple.xml" )) );
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


}
