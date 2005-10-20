/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.jervlet.containers.jetty.pico;

import junit.framework.TestCase;

import java.io.Reader;
import java.io.StringReader;
import java.io.InputStream;
import java.io.IOException;

import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.runtime.InvokerHelper;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyCodeSource;
import groovy.lang.Script;
import groovy.lang.Binding;

/**
 * Test cases for <code>GroovyJettyContainerBuilder</code>
 *
 * @author Johan Sjoberg
 * @author Paul Hammant
 */
public class GroovyJettyContainerBuilderTestCase extends TestCase
{
    private static final String m_jettyConfiguration =
      "../../testdata/jetty/jetty.xml";
    private static final String m_plainWebapp =
      "../../testdata/webapps/plain";
    private static final String m_plainWebappWAR =
      "../../testdata/webapps/plain.war";
    private static final String m_picoWebapp =
      "../../testdata/webapps/pico";

    /**
     * A simple test
     *
     * @throws CompilationFailedException if the groovy script couldn't be compiled
     */
    public void testSimple() throws CompilationFailedException
    {
        System.out.println( "\n----- testSimple() -----\n" );
        final Reader script = new StringReader(
          "package org.codehaus.spice.jervlet.containers.jetty.pico\n" +
          "import org.picocontainer.defaults.DefaultPicoContainer\n" +
          "import org.codehaus.spice.jervlet.containers.jetty.pico.GroovyJettyContainerBuilder\n" +
          "import org.codehaus.spice.jervlet.containers.jetty.pico.PicoJettyContainer\n" +
          "import org.codehaus.spice.jervlet.impl.pico.PicoInstantiator\n" +
          "import org.codehaus.spice.jervlet.impl.StandardServletInstantiator\n" +
          "import org.codehaus.spice.jervlet.impl.Pinger\n" +

          "def pico = new DefaultPicoContainer()\n" +
          "def pinger = new Pinger()\n" +
          "pinger.ping( '' +  System.currentTimeMillis() )\n" +
          "pico.registerComponentInstance( pinger )\n" +

          "def builder = new GroovyJettyContainerBuilder()\n" +
          "def instantiator = new PicoInstantiator( pico )\n" +
          "def webServer = builder.picoJettyContainer()\n" +
          "{\n" +
              "httpListener( port:'16842' )\n" +

              "context( webPath:'pico',\n" +
                       "warPath:'" + m_picoWebapp + "',\n" +
                       "instantiator:instantiator )\n" +

              "context( webPath:'plain',\n" +
                       "warPath:'" + m_plainWebapp + "',\n" +
                       "instantiator: new StandardServletInstantiator() )\n" +
          "}\n" +

          "webServer.start()\n" +
          "webServer.stop()" );

        doGroovy( new InputStream()
                      {
                          public int read() throws IOException
                          {
                             return script.read();
                          }
                      } );
        assertEquals( "", "" );
    }

    /**
     * Test various parts of <code>GroovyJettyContainerBuilder</code>
     *
     * @throws CompilationFailedException if the groovy script couldn't be compiled
     */
    public void testContainerBuilder() throws CompilationFailedException
    {
        System.out.println( "\n----- testContainerBuilder() -----\n" );
        final Reader script = new StringReader(
          "package org.codehaus.spice.jervlet.containers.jetty.pico\n" +
          "import org.picocontainer.defaults.DefaultPicoContainer\n" +
          "import org.codehaus.spice.jervlet.containers.jetty.pico.GroovyJettyContainerBuilder\n" +
          "import org.codehaus.spice.jervlet.containers.jetty.pico.PicoJettyContainer\n" +
          "import org.codehaus.spice.jervlet.impl.pico.PicoInstantiator\n" +
          "import org.codehaus.spice.jervlet.impl.Pinger\n" +
          "import org.codehaus.spice.jervlet.impl.DefaultContext\n" +
          "import org.codehaus.spice.jervlet.impl.SystemOutContextMonitor\n" +
          "import org.codehaus.spice.jervlet.impl.NoopListenerMonitor\n" +

          "def pico = new DefaultPicoContainer()\n" +
          "def pinger = new Pinger()\n" +
          "pinger.ping( '' +  System.currentTimeMillis() )\n" +
          "pico.registerComponentInstance( pinger )\n" +
          "def builder = new GroovyJettyContainerBuilder()\n" +
          "def instantiator = new PicoInstantiator( pico )\n" +
          "def contextMonitor = new SystemOutContextMonitor()\n" +
          "def listenerMonitor = new NoopListenerMonitor()\n" +

          "def webServer = builder.picoJettyContainer( contextMonitor:contextMonitor, listenerMonitor:listenerMonitor )\n" +
          "{\n" +
              "configuration( configuration:'" + m_jettyConfiguration + "' )\n" +
              "{\n" +
                  "parameter( key:'jetty.port', value:'16842' )\n" +
              "}\n" +
              "listener( port:'16843', type:'http' )\n" +
              "listener( port:'16844', type:'ajp13', host:'localhost' )\n" +
              "context( webPath:'pico1', warPath:'" + m_picoWebapp + "', instantiator:instantiator, hosts:'localhost' )\n" +
          "}\n" +

          "def contextHandler = webServer.createContextHandler()\n" +
          "def context2 = builder.context( webPath:'pico2',\n" +
                                          "warPath:'" + m_picoWebapp + "',\n" +
                                          "extractWar:false,\n" +
                                          "instantiator:instantiator,\n" +
                                          "hosts:'0.0.0.0, localhost' )\n" +
          "contextHandler.addContext( context2 )\n" +

          "webServer.start()\n" +

          "def context3 = builder.context( webPath:'pico3', warPath:'" + m_picoWebapp + "', extractWar:false, instantiator:instantiator )\n" +
          "contextHandler.addContext( context3 )\n" +
          "contextHandler.startContext( context3 )\n" +
          "contextHandler.stopContext( context3 )\n" +
          "contextHandler.removeContext( context3 )\n" +

          "webServer.stop()" );

        doGroovy( new InputStream()
                      {
                          public int read() throws IOException
                          {
                             return script.read();
                          }
                      } );
        assertEquals( "", "" );
    }

    /**
     * Run a groovy script
     *
     * @param is the input stream holding the script
     * @throws CompilationFailedException if the script couldn't be compiled
     */
    protected void doGroovy( InputStream is ) throws CompilationFailedException
    {
        GroovyClassLoader loader = new GroovyClassLoader( getClass().getClassLoader() );
        GroovyCodeSource groovyCodeSource = new GroovyCodeSource(
          is,"nanocontainer.groovy", "groovyGeneratedForNanoContainer" );
        Class scriptClass = loader.parseClass( groovyCodeSource );
        Script groovyScript = InvokerHelper.createScript( scriptClass, null );
        Binding binding = new Binding();
        groovyScript.setBinding( binding );
        groovyScript.run();
    }
}
