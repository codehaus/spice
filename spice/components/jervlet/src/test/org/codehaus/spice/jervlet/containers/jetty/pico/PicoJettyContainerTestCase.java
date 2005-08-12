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
import java.io.File;
import java.net.URL;
import java.net.MalformedURLException;

import org.codehaus.spice.jervlet.impl.*;
import org.codehaus.spice.jervlet.impl.pico.PicoInstantiator;
import org.codehaus.spice.jervlet.ContextHandler;
import org.codehaus.spice.jervlet.Context;
import org.codehaus.spice.jervlet.Instantiator;
import org.codehaus.spice.jervlet.Listener;

import org.picocontainer.MutablePicoContainer;
import org.picocontainer.defaults.DefaultPicoContainer;

/**
 * TestCase for PicoJettyContainer
 *
 * @author Johan Sjoberg
 */
public class PicoJettyContainerTestCase extends TestCase
{
    private static final String m_defaultConfiguration =
      "./testdata/jetty/jetty.xml";
    private static final String m_webapp1 =
      "../../testdata/webapps/template";
    private static final String m_webapp2 =
      "../../testdata/webapps/template2.war";

    private int m_count = 1;

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

    /**
     * Create an empty container and start, deploy a
     * webapp and stop. Also test that the context
     * are added and removed correctly.
     *
     * @throws Exception
     */
    public void testEmptyWithWebapps() throws Exception
    {
        Context context1 = getStandardContext( m_webapp1 );
        Context context2 = getStandardContext( m_webapp2 );

        PicoJettyContainer container = new PicoJettyContainer( null, null, null );
        container.start();

        ContextHandler contextHandler = container.createContextHandler();

        contextHandler.addContext( context1 );
        contextHandler.addContext( context2 );

        contextHandler.startContext( context1 );
        assertTrue( contextHandler.isStarted( context1 ) );
        contextHandler.startContext( context2 );
        assertTrue( contextHandler.isStarted( context1 ) );
        assertTrue( contextHandler.isStarted( context2 ) );

        contextHandler.stopContext( context1 );
        assertFalse( contextHandler.isStarted( context1 ) );
        contextHandler.stopContext( context2 );
        assertFalse( contextHandler.isStarted( context1 ) );
        assertFalse( contextHandler.isStarted( context2 ) );

        assertTrue( 2 == contextHandler.getContexts().size() );
        contextHandler.removeContext( context1 );
        assertTrue( 1 == contextHandler.getContexts().size() );
        contextHandler.removeContext( context2 );
        assertTrue( 0 == contextHandler.getContexts().size() );

        container.stop();
    }

    /**
     * Test that a pico-servlet component gets instantiated
     *
     * --------------- UNDER CONSTRUCTION! ---------------
     *
     * @throws Exception
     */
    public void testEmptyWithPicoInstantiator() throws Exception
    {
        DefaultPicoContainer pico = new DefaultPicoContainer();
        Context context = getPicoContext( m_webapp1, pico );

        PicoJettyContainer container = new PicoJettyContainer( null, null, null );
        container.start();

        Listener listener = new DefaultListener( "localhost", 8421, Listener.HTTP );
        container.addListener( listener );
        container.startListener( listener );

        ContextHandler contextHandler = container.createContextHandler();
        contextHandler.addContext( context );
        contextHandler.startContext( context );
        contextHandler.stopContext( context );
        contextHandler.removeContext( context );

        container.stopListener( listener );
        container.stop();
    }

    /**
     * Create a basic context, using the standard servlet
     * instantiator.
     */
    private Context getStandardContext( final String filePath )
        throws MalformedURLException
    {
        String path = "context" + m_count++;
        String[] virtualHosts = null;
        URL resource = (new File( filePath )).toURL();
        boolean extractWAR = false;
        Instantiator instantiator = new StandardServletInstantiator();

        DefaultContext context = new DefaultContext( path,
                                                     virtualHosts,
                                                     resource,
                                                     extractWAR,
                                                     instantiator );
        return context;
    }

    /**
     * Create a Pico context, using <code>PicoInstantiator</code>.
     *
     * @filePath path to the web resource
     * @pico a pico container for this context
     *       (used when instantiation servlets)
     */
    private Context getPicoContext( final String filePath,
                                    final MutablePicoContainer pico )
        throws MalformedURLException
    {
        String path = "context" + m_count++;
        String[] virtualHosts = null;
        URL resource = (new File( filePath )).toURL();
        boolean extractWAR = false;
        Instantiator instantiator = new PicoInstantiator( pico );

        DefaultContext context = new DefaultContext( path,
                                                     virtualHosts,
                                                     resource,
                                                     extractWAR,
                                                     instantiator );
        return context;
    }
}
