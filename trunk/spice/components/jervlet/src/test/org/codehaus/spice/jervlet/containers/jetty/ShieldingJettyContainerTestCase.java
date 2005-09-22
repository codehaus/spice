/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.jervlet.containers.jetty;

import junit.framework.TestCase;

import java.util.Properties;
import java.io.File;

import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.WebResponse;

import org.codehaus.spice.jervlet.Context;
import org.codehaus.spice.jervlet.Listener;
import org.codehaus.spice.jervlet.ContextHandler;

import org.codehaus.spice.jervlet.impl.Pinger;
import org.codehaus.spice.jervlet.impl.DefaultListener;
import org.codehaus.spice.jervlet.impl.DefaultContext;
import org.codehaus.spice.jervlet.impl.StandardServletInstantiator;
import org.codehaus.spice.jervlet.impl.dna.DNAInstantiator;
import org.codehaus.spice.jervlet.impl.avalon.AvalonInstantiator;
import org.codehaus.spice.jervlet.impl.pico.PicoInstantiator;

import org.picocontainer.defaults.DefaultPicoContainer;
import org.codehaus.dna.impl.DefaultResourceLocator;
import org.apache.avalon.framework.logger.ConsoleLogger;
import org.apache.avalon.framework.service.DefaultServiceManager;

/**
 * TestCase for ShieldingJettyContainer
 *
 * @author Johan Sjoberg
 */
public class ShieldingJettyContainerTestCase extends TestCase
{
    private static final String DEFAULT_CONFIGURATION =
      "../../testdata/jetty/jetty.xml";
    private static final String m_plainWebapp =
      "../../testdata/webapps/plain";
    private static final String m_avalonWebapp =
      "../../testdata/webapps/avalon";
    private static final String m_dnaWebapp =
      "../../testdata/webapps/dna";
    private static final String m_picoWebapp =
      "../../testdata/webapps/pico";

    /**
     * Test a dafault plain Jetty, no conf but with the
     * shielding turned on.
     *
     * @throws Exception on all errors
     */
    public void testCreationNoConf() throws Exception
    {
        ShieldingJettyContainer container = new ShieldingJettyContainer();
        container.initialize();
        container.start();
        container.stop();
    }

    /**
     * Test jetty with default configuration, shielded,
     * but no parameters.
     *
     * @throws Exception on all errors
     */
    public void testCreationDefaultConfiguration() throws Exception
    {
        ShieldingJettyContainer container = new ShieldingJettyContainer();
        container.setJettyConfiguration( DEFAULT_CONFIGURATION );
        container.initialize();
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
     * Test jetty with default configuration, shielded and
     * with one (overriding) parameter.
     *
     * @throws Exception on all errors
     */
    public void testCreationDefaultConfigurationOneParameter() throws Exception
    {
        ShieldingJettyContainer container = new ShieldingJettyContainer();
        container.setJettyConfiguration( DEFAULT_CONFIGURATION );
        Properties properties = new Properties();
        properties.setProperty( "jetty.port", "10081" );
        container.addJettyProperties( properties );
        container.initialize();
        container.start();

        WebConversation conversation = new WebConversation();
        WebRequest request = new GetMethodWebRequest( "http://localhost:10081/plain/message.txt" );
        WebResponse response = conversation.getResponse( request );
        String responseBody = response.getText();
        assertEquals(  "plain", responseBody );

        request = new GetMethodWebRequest( "http://localhost:10081/plain/plain-servlet" );
        response = conversation.getResponse( request );
        responseBody = response.getText();
        assertEquals(  "org.codehaus.spice.jervlet.impl.PlainTestServlet", responseBody );

        request = new GetMethodWebRequest( "http://localhost:10081/plain/plain-filter-servlet" );
        response = conversation.getResponse( request );
        responseBody = response.getText();
        assertEquals(  "org.codehaus.spice.jervlet.impl.PlainTestFilter "
          + "org.codehaus.spice.jervlet.impl.PlainTestServlet", responseBody );

        container.stop();
    }

    /**
     * Test if pico-servlet components gets instantiated
     *
     * @throws Exception on all errors
     */
    public void testPlainContext() throws Exception
    {
        Context context = new DefaultContext( "plain",
                                              null,
                                              (new File( m_plainWebapp )).toURL(),
                                              false,
                                              new StandardServletInstantiator() );

        ShieldingJettyContainer container = new ShieldingJettyContainer();
        container.initialize();
        container.start();

        Listener listener = new DefaultListener( null, 16842, Listener.HTTP );
        container.addListener( listener );
        container.startListener( listener );

        ContextHandler contextHandler = container.createContextHandler();
        contextHandler.addContext( context );
        contextHandler.startContext( context );

        WebConversation conversation = new WebConversation();
        WebRequest request = new GetMethodWebRequest( "http://localhost:16842/plain/message.txt" );
        WebResponse response = conversation.getResponse( request );
        String responseBody = response.getText();
        assertEquals(  "plain", responseBody );

        request = new GetMethodWebRequest( "http://localhost:16842/plain/plain-servlet" );
        response = conversation.getResponse( request );
        responseBody = response.getText();

        assertEquals(  "org.codehaus.spice.jervlet.impl.PlainTestServlet", responseBody );

        conversation = new WebConversation();
        request = new GetMethodWebRequest( "http://localhost:16842/plain/plain-filter-servlet" );
        response = conversation.getResponse( request );
        responseBody = response.getText();

        assertEquals(  "org.codehaus.spice.jervlet.impl.PlainTestFilter "
          +  "org.codehaus.spice.jervlet.impl.PlainTestServlet", responseBody );

        container.stop();
    }


    /**
     * Test if pico-servlet components gets instantiated
     *
     * @throws Exception on all errors
     */
    public void testPicoContext() throws Exception
    {
        Pinger pinger = new Pinger();
        String message = Long.toString( System.currentTimeMillis() );
        pinger.ping( message );

        DefaultPicoContainer pico = new DefaultPicoContainer();
        pico.registerComponentInstance(  pinger );

        Context context = new DefaultContext( "pico",
                                              null,
                                              (new File( m_picoWebapp )).toURL(),
                                              false,
                                              new PicoInstantiator( pico ) );

        ShieldingJettyContainer container = new ShieldingJettyContainer();
        container.initialize();
        container.start();

        Listener listener = new DefaultListener( null, 16842, Listener.HTTP );
        container.addListener( listener );
        container.startListener( listener );

        ContextHandler contextHandler = container.createContextHandler();
        contextHandler.addContext( context );
        contextHandler.startContext( context );

        WebConversation conversation = new WebConversation();
        WebRequest request = new GetMethodWebRequest( "http://localhost:16842/pico/message.txt" );
        WebResponse response = conversation.getResponse( request );
        String responseBody = response.getText();
        assertEquals(  "pico", responseBody );

        request = new GetMethodWebRequest( "http://localhost:16842/pico/pico-servlet" );
        response = conversation.getResponse( request );
        responseBody = response.getText();

        assertEquals(  "org.codehaus.spice.jervlet.impl.pico.PicoTestServlet "
          + message, responseBody );

        conversation = new WebConversation();
        request = new GetMethodWebRequest( "http://localhost:16842/pico/pico-filter-servlet" );
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
     * Test if avalon-servlet components gets instantiated
     *
     * @throws Exception on all errors
     */
    public void testAvalonContext() throws Exception
    {
        Pinger pinger = new Pinger();
        String message = Long.toString( System.currentTimeMillis() );
        pinger.ping( message );

        DefaultServiceManager serviceManager = new DefaultServiceManager();
        serviceManager.put( "pinger", pinger );

        Context context = new DefaultContext( "avalon", null, (new File( m_avalonWebapp )).toURL(),
          false, new AvalonInstantiator( new org.apache.avalon.framework.context.DefaultContext(),
          serviceManager, new ConsoleLogger() ) );

        ShieldingJettyContainer container = new ShieldingJettyContainer();
        container.initialize();
        container.start();

        Listener listener = new DefaultListener( null, 16842, Listener.HTTP );
        container.addListener( listener );
        container.startListener( listener );

        ContextHandler contextHandler = container.createContextHandler();
        contextHandler.addContext( context );
        contextHandler.startContext( context );

        WebConversation conversation = new WebConversation();
        WebRequest request = new GetMethodWebRequest( "http://localhost:16842/avalon/message.txt" );
        WebResponse response = conversation.getResponse( request );
        String responseBody = response.getText();
        assertEquals(  "avalon", responseBody );

        request = new GetMethodWebRequest( "http://localhost:16842/avalon/avalon-servlet" );
        response = conversation.getResponse( request );
        responseBody = response.getText();

        assertEquals(  "org.codehaus.spice.jervlet.impl.avalon.AvalonTestServlet "
          + message, responseBody );

        conversation = new WebConversation();
        request = new GetMethodWebRequest( "http://localhost:16842/avalon/avalon-filter-servlet" );
        response = conversation.getResponse( request );
        responseBody = response.getText();

        assertEquals(  "org.codehaus.spice.jervlet.impl.avalon.AvalonTestFilter "
          + message +  " org.codehaus.spice.jervlet.impl.avalon.AvalonTestServlet "
          + message, responseBody );

        container.stop();

        assertEquals( "org.codehaus.spice.jervlet.impl.avalon.AvalonTestFilter "
          + message, pinger.getMessages().get( 1 ) );
        assertEquals( "org.codehaus.spice.jervlet.impl.avalon.AvalonTestServlet "
          + message, pinger.getMessages().get( 2 ) );
    }

    /**
     * Test if dna-servlet components gets instantiated
     *
     * @throws Exception on all errors
     */
    public void testDNAContext() throws Exception
    {
        Pinger pinger = new Pinger();
        String message = Long.toString( System.currentTimeMillis() );
        pinger.ping( message );

        DefaultResourceLocator resourceLocator = new DefaultResourceLocator();
        resourceLocator.put( "pinger", pinger );

        Context context = new DefaultContext( "dna", null,
          (new File( m_dnaWebapp )).toURL(),
          false, new DNAInstantiator( resourceLocator,
          new org.codehaus.dna.impl.ConsoleLogger() ) );

        ShieldingJettyContainer container = new ShieldingJettyContainer();
        container.initialize();
        container.start();

        Listener listener = new DefaultListener( null, 16842, Listener.HTTP );
        container.addListener( listener );
        container.startListener( listener );

        ContextHandler contextHandler = container.createContextHandler();
        contextHandler.addContext( context );
        contextHandler.startContext( context );

        WebConversation conversation = new WebConversation();
        WebRequest request = new GetMethodWebRequest( "http://localhost:16842/dna/message.txt" );
        WebResponse response = conversation.getResponse( request );
        String responseBody = response.getText();
        assertEquals(  "dna", responseBody );

        request = new GetMethodWebRequest( "http://localhost:16842/dna/dna-servlet" );
        response = conversation.getResponse( request );
        responseBody = response.getText();

        assertEquals(  "org.codehaus.spice.jervlet.impl.dna.DNATestServlet "
          + message, responseBody );

        conversation = new WebConversation();
        request = new GetMethodWebRequest( "http://localhost:16842/dna/dna-filter-servlet" );
        response = conversation.getResponse( request );
        responseBody = response.getText();

        assertEquals(  "org.codehaus.spice.jervlet.impl.dna.DNATestFilter "
          + message +  " org.codehaus.spice.jervlet.impl.dna.DNATestServlet "
          + message, responseBody );

        container.stop();

        assertEquals( "org.codehaus.spice.jervlet.impl.dna.DNATestFilter "
          + message, pinger.getMessages().get( 1 ) );
        assertEquals( "org.codehaus.spice.jervlet.impl.dna.DNATestServlet "
          + message, pinger.getMessages().get( 2 ) );
    }
}