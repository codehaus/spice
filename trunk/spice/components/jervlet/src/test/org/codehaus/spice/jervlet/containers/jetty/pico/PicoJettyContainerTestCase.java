/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.jervlet.containers.jetty.pico;

import junit.framework.TestCase;
import org.codehaus.spice.jervlet.Context;
import org.codehaus.spice.jervlet.ContextHandler;
import org.codehaus.spice.jervlet.Instantiator;
import org.codehaus.spice.jervlet.Listener;
import org.codehaus.spice.jervlet.impl.NoopContextMonitor;
import org.codehaus.spice.jervlet.impl.NoopListenerMonitor;
import org.codehaus.spice.jervlet.impl.DefaultListener;
import org.codehaus.spice.jervlet.impl.StandardServletInstantiator;
import org.codehaus.spice.jervlet.impl.DefaultContext;
import org.codehaus.spice.jervlet.impl.Pinger;
import org.codehaus.spice.jervlet.impl.pico.PicoInstantiator;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.defaults.DefaultPicoContainer;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.GetMethodWebRequest;

/**
 * TestCase for PicoJettyContainer
 *
 * @author Johan Sjoberg
 */
public class PicoJettyContainerTestCase extends TestCase
{
    private static final String m_defaultConfiguration =
      "../../testdata/jetty/jetty.xml";
    private static final String m_plainWebapp =
      "../../testdata/webapps/plain";
    private static final String m_plainWebappWAR =
      "../../testdata/webapps/plain.war";
    private static final String m_picoWebapp =
      "../../testdata/webapps/pico";

    private int m_count = 1;

    /**
     * Create an empty container and start/stop it.
     *
     * @throws Exception
     */
    public void testEmpty() throws Exception
    {
        PicoJettyContainer container = new PicoJettyContainer();
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

        WebConversation conversation = new WebConversation();
        WebRequest request = new GetMethodWebRequest( "http://localhost:10080/plain/message.txt" );
        WebResponse response = conversation.getResponse( request );
        String responseBody = response.getText();
        assertEquals(  "plain", responseBody );

        request = new GetMethodWebRequest( "http://localhost:10080/plain/plain-servlet" );
        response = conversation.getResponse( request );
        responseBody = response.getText();
        assertEquals(  "org.codehaus.spice.jervlet.impl.PlainTestServlet", responseBody );

        request = new GetMethodWebRequest( "http://localhost:10080/plain/plain-filter-servlet" );
        response = conversation.getResponse( request );
        responseBody = response.getText();
        assertEquals(  "org.codehaus.spice.jervlet.impl.PlainTestFilter "
          + "org.codehaus.spice.jervlet.impl.PlainTestServlet", responseBody );

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
        properties.setProperty( "jetty.port", "16842" );

        DefaultJettyContainerConfiguration configuration =
          new DefaultJettyContainerConfiguration();
        configuration.setConfiguration( (new File( m_defaultConfiguration )).toURL() );
        configuration.setProperties( properties );

        PicoJettyContainer container = new PicoJettyContainer( configuration,
                                                               new NoopContextMonitor(),
                                                               new NoopListenerMonitor() );
        container.start();

        WebConversation conversation = new WebConversation();
        WebRequest request = new GetMethodWebRequest( "http://localhost:16842/plain/plain-servlet" );
        WebResponse response = conversation.getResponse( request );
        String responseBody = response.getText();
        assertEquals(  "org.codehaus.spice.jervlet.impl.PlainTestServlet", responseBody );

        request = new GetMethodWebRequest( "http://localhost:16842/plain/message.txt" );
        response = conversation.getResponse( request );
        responseBody = response.getText();
        assertEquals(  "plain", responseBody );

        request = new GetMethodWebRequest( "http://localhost:16842/plain/plain-filter-servlet" );
        response = conversation.getResponse( request );
        responseBody = response.getText();
        assertEquals(  "org.codehaus.spice.jervlet.impl.PlainTestFilter "
          + "org.codehaus.spice.jervlet.impl.PlainTestServlet", responseBody );

        container.stop();
    }

    /**
     * Create an empty container and start, deploy
     * and stop some webapps. Also test that the context
     * are added and removed correctly.
     *
     * @throws Exception
     */
    public void testEmptyWithWebapps() throws Exception
    {

        PicoJettyContainer container = new PicoJettyContainer( null, null, null );
        container.start();

        ContextHandler contextHandler = container.createContextHandler();

        Context context1 = getStandardContext( m_plainWebapp );
        Context context2 = getStandardContext( m_plainWebappWAR );

        contextHandler.addContext( context1 );
        contextHandler.addContext( context2 );

        contextHandler.startContext( context1 );
        assertTrue( contextHandler.isStarted( context1 ) );
        contextHandler.startContext( context2 );
        assertTrue( contextHandler.isStarted( context1 ) );
        assertTrue( contextHandler.isStarted( context2 ) );

        Listener listener = new DefaultListener( "localhost", 16842, Listener.HTTP );
        container.addListener( listener );
        container.startListener( listener );

        WebConversation conversation = new WebConversation();
        WebRequest request = new GetMethodWebRequest(
          "http://localhost:16842/context1/plain-servlet" );
        WebResponse response = conversation.getResponse( request );
        String responseBody = response.getText();
        assertEquals(  "org.codehaus.spice.jervlet.impl.PlainTestServlet", responseBody );

        container.stopListener( listener );

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
     * Test if pico-servlet components gets instantiated
     *
     * @throws Exception on all errors
     */
    public void testPicoInstantiator_AJP() throws Exception
    {
        DefaultPicoContainer pico = new DefaultPicoContainer();
        Pinger pinger = new Pinger();
        String message = Long.toString( System.currentTimeMillis() );
        pinger.ping( message );
        pico.registerComponentInstance( pinger );
        Context context = getPicoContext( m_picoWebapp, pico );

        PicoJettyContainer container = new PicoJettyContainer( null, null, null );
        container.start();

        Listener listener = new DefaultListener( "localhost", 16842, Listener.AJP13 );
        container.addListener( listener );
        container.startListener( listener );

        ContextHandler contextHandler = container.createContextHandler();
        contextHandler.addContext( context );
        contextHandler.startContext( context );

        assertEquals( "org.codehaus.spice.jervlet.impl.pico.PicoTestFilter "
          + message, pinger.getMessages().get( 1 ) );
        assertEquals( "org.codehaus.spice.jervlet.impl.pico.PicoTestServlet "
          + message, pinger.getMessages().get( 2 ) );

        contextHandler.stopContext( context );
        contextHandler.removeContext( context );

        container.stopListener( listener );
        container.stop();
    }

    /**
     * Test if pico-servlet components gets instantiated
     *
     * @throws Exception on all errors
     */
    public void testPicoInstantiator_HTTP() throws Exception
    {
        DefaultPicoContainer pico = new DefaultPicoContainer();
        Pinger pinger = new Pinger();
        String message = Long.toString( System.currentTimeMillis() );
        pinger.ping( message );
        pico.registerComponentInstance(  pinger );
        Context context = getPicoContext( m_picoWebapp, pico, "test" );

        PicoJettyContainer container = new PicoJettyContainer();
        container.start();

        Listener listener = new DefaultListener( "localhost", 16842, Listener.HTTP );
        container.addListener( listener );
        container.startListener( listener );

        ContextHandler contextHandler = container.createContextHandler();
        contextHandler.addContext( context );
        contextHandler.startContext( context );

        WebConversation conversation = new WebConversation();
        WebRequest request = new GetMethodWebRequest( "http://localhost:16842/test/pico-servlet" );
        WebResponse response = conversation.getResponse( request );
        String responseBody = response.getText();

        assertEquals(  "org.codehaus.spice.jervlet.impl.pico.PicoTestServlet "
          + message, responseBody );

        conversation = new WebConversation();
        request = new GetMethodWebRequest( "http://localhost:16842/test/pico-filter-servlet" );
        response = conversation.getResponse( request );
        responseBody = response.getText();

        assertEquals(  "org.codehaus.spice.jervlet.impl.pico.PicoTestFilter "
          + message +  " org.codehaus.spice.jervlet.impl.pico.PicoTestServlet "
          + message, responseBody );

        container.stop();

        assertEquals( "org.codehaus.spice.jervlet.impl.pico.PicoTestFilter "
          + message, pinger.getMessages().get( 1 ) );
        assertEquals( "org.codehaus.spice.jervlet.impl.pico.PicoTestServlet "
          + message, pinger.getMessages().get( 2 ) );
    }

    /**
     * Create a basic context, using the standard servlet
     * instantiator.
     */
    private Context getStandardContext( String filePath )
        throws MalformedURLException
    {
        String path = "context" + m_count++;
        String[] virtualHosts = null;
        URL resource = (new File( filePath )).toURL();
        boolean extractWAR = false;
        Instantiator instantiator = new StandardServletInstantiator();

        return new DefaultContext( path,
                                   virtualHosts,
                                   resource,
                                   extractWAR,
                                   instantiator );
    }

    /**
     * Create a Pico context, using <code>PicoInstantiator</code>.
     *
     * @param filePath path to the web resource   )
     * @param pico container for this context
     *       (used when instantiation servlets)
     */
    private Context getPicoContext( String filePath,
                                    MutablePicoContainer pico )
        throws MalformedURLException
    {
        return  getPicoContext( filePath, pico, "context" + m_count++ );
    }

    /**
     * Create a Pico context, using <code>PicoInstantiator</code>.
     *
     * @param filePath path to the web resource
     * @param pico container for this context
     *       (used when instantiation servlets)
     * @param path the context's path
     */
    private Context getPicoContext( String filePath,
                                    MutablePicoContainer pico,
                                    String path )
        throws MalformedURLException
    {
        String[] virtualHosts = null;
        URL resource = (new File( filePath )).toURL();
        boolean extractWAR = false;
        Instantiator instantiator = new PicoInstantiator( pico );

        return new DefaultContext( path,
                                   virtualHosts,
                                   resource,
                                   extractWAR,
                                   instantiator );
    }
}