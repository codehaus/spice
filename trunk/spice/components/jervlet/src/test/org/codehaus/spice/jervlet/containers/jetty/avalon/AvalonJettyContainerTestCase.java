/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.jervlet.containers.jetty.avalon;

import junit.framework.TestCase;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.DefaultConfiguration;
import org.apache.avalon.framework.logger.ConsoleLogger;

import java.util.Properties;
import java.util.Enumeration;

/**
 * TestCase for AvalonJettyContainer
 *
 * @author Johan Sjoberg
 */
public class AvalonJettyContainerTestCase extends TestCase
{
    private static final String m_jettyConfiguration =
      "../../testdata/jetty/jetty.xml";
    private static final String m_plainWebapp =
      "../../testdata/webapps/plain";
    private static final String m_plainWebappWAR =
      "../../testdata/webapps/plain.war";
    private static final String m_avalonWebapp =
      "../../testdata/webapps/avalon";

    /**
     * Create an empty container and start/stop it.
     *
     * @throws Exception
     */
    public void testEmpty() throws Exception
    {
        AvalonJettyContainer container =
          getContainer( new DefaultConfiguration( "", "" ) );
        container.start();
        container.stop();
    }

    /**
     * Create a container with the default configuration and
     * start/stop it.
     *
     * @throws Exception
     */
    public void testDefaultConfigurationFile() throws Exception
    {
        AvalonJettyContainer container =
          getContainer( getStringConfiguration( m_jettyConfiguration ) );
        container.start();
        container.stop();
    }

    /**
     * Create a container with one property (jetty.port) and
     * start/stop it.
     *
     * @throws Exception
     */
    public void testProperties() throws Exception
    {
        Properties properties = new Properties();
        properties.setProperty( "jetty.port", "8421" );
        AvalonJettyContainer container =
          getContainer( getPropertiesConfiguration( m_jettyConfiguration, properties ) );
        container.start();
        container.stop();
    }

    /**
     * Fetch a container by giving a configuration.
     *
     * @param configuration configuration for the continer
     * @return an initialized AvalonJettyContainer
     * @throws Exception on all errors
     */
    private AvalonJettyContainer getContainer( Configuration configuration )
        throws Exception
    {
        AvalonJettyContainer avalonJettyContainer = new AvalonJettyContainer();
        avalonJettyContainer.enableLogging( new ConsoleLogger() );
        avalonJettyContainer.configure( configuration );
        avalonJettyContainer.initialize();
        return avalonJettyContainer;
    }


    /**
     * Create a configuraion including Jetty system properties
     *
     * @param jettyConfigurationFile path to Jetty's configuration file
     * @param properties the properties to add to the configuration
     * @return a new configuration object
     */
    private Configuration getPropertiesConfiguration( String jettyConfigurationFile,
                                                      Properties properties )
    {
        DefaultConfiguration configuration = (DefaultConfiguration)
          getStringConfiguration( jettyConfigurationFile );
        DefaultConfiguration propertiesConfiguration =
          new DefaultConfiguration( "jetty-system-properties", "" );
        for( Enumeration propertyNames = properties.propertyNames();
             propertyNames.hasMoreElements(); )
        {
            String key = (String)propertyNames.nextElement();
            String value = properties.getProperty( key );
            System.out.println( "Setting property [" + key + "=" + value + "].");
            DefaultConfiguration propertyConfiguration =
              new DefaultConfiguration( "property", "" );
            propertyConfiguration.setAttribute( "key", key );
            propertyConfiguration.setAttribute( "value", value );
            propertiesConfiguration.addChild( propertyConfiguration );
        }
        configuration.addChild( propertiesConfiguration );
        return configuration;
    }

    /**
     * Create a configuration using a Jetty configuration file
     *
     * @param jettyConfigurationFile path to Jetty's configuration file
     * @return a new configuration object
     */
    private Configuration getStringConfiguration( String jettyConfigurationFile )
    {
        DefaultConfiguration configuration = new DefaultConfiguration( "", "" );
        DefaultConfiguration configurationFile =
          new DefaultConfiguration( "jetty-configuration-file", "" );
        configurationFile.setValue( jettyConfigurationFile );
        configuration.addChild( configurationFile );
        return configuration;
    }
}