/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.jervlet.containers.jetty.pico;

import junit.framework.TestCase;

import java.util.Properties;

import org.codehaus.spice.jervlet.impl.NoopContextMonitor;
import org.codehaus.spice.jervlet.impl.NoopListenerMonitor;

/**
 * TestCase for PicoJettyContainer
 *
 * @author Johan Sjoberg
 */
public class PicoJettyContainerTestCase extends TestCase
{
    private static final String m_defaultConfiguration =
      "./testdata/jetty/jetty.xml";

    /**
     * Create an empty container and start/stop it.
     *
     * @throws Exception
     */
    public void testEmpty() throws Exception
    {
        PicoJettyContainer container = new PicoJettyContainer( null, null, null );
        container.start();
        container.stop();
    }

    /**
     * Create an empty container with Noop monitors and and empty
     * configuration (same result as testEmpty()).
     *
     * @throws Exception
     */
    public void testEmptyWithNoopMonitors() throws Exception
    {
        PicoJettyContainer container =
          new PicoJettyContainer( new DefaultJettyContainerConfiguration(),
                                  new NoopContextMonitor(),
                                  new NoopListenerMonitor() );
        container.start();
        container.stop();
    }

    /**
     * Create a container with the default configuration file
     * that comes with Jetty, and start/stop the container.
     *
     * @throws Exception
     */
    public void testDefaultConfigurationFile() throws Exception
    {
        DefaultJettyContainerConfiguration configuration =
          new DefaultJettyContainerConfiguration();
        configuration.setConfiguration( m_defaultConfiguration );
        PicoJettyContainer container = new PicoJettyContainer( configuration,
                                                               null,
                                                               null );
        container.start();
        container.stop();
    }

    /**
     * Create a container with one property (jetty.port) and
     * start/stop it using the default configuration.
     *
     * @throws Exception
     */
    public void testProperties() throws Exception
    {
        Properties properties = new Properties();
        properties.setProperty( "jetty.port", "8421" );

        DefaultJettyContainerConfiguration configuration =
          new DefaultJettyContainerConfiguration();
        configuration.setConfiguration( m_defaultConfiguration );
        configuration.setProperties( properties );

        PicoJettyContainer container = new PicoJettyContainer( configuration,
                                                               new NoopContextMonitor(),
                                                               new NoopListenerMonitor() );
        container.start();
        container.stop();
    }
}
