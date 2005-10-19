/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.jervlet.containers.jetty.pico;

import groovy.util.BuilderSupport;

import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.net.URL;
import java.net.MalformedURLException;
import java.io.File;

import org.codehaus.spice.jervlet.ContextMonitor;
import org.codehaus.spice.jervlet.ListenerMonitor;
import org.codehaus.spice.jervlet.Listener;
import org.codehaus.spice.jervlet.ListenerException;
import org.codehaus.spice.jervlet.Context;
import org.codehaus.spice.jervlet.Instantiator;
import org.codehaus.spice.jervlet.ContextException;
import org.codehaus.spice.jervlet.impl.DefaultListener;
import org.codehaus.spice.jervlet.impl.DefaultContext;

/**
 * Groovy container builder using
 * <code>PicoJettyContainer</code> under the hood.
 *
 * @author Johan Sjoberg
 * @author Paul Hammant
 */
public class GroovyJettyContainerBuilder extends BuilderSupport
{
    public GroovyJettyContainerBuilder()
    {
    }

    /**
     * Set a child object on its parent
     * <br/><br/>
     * Possible parent - child combinations are:
     * <ul>
     *  <li>PicoJettyContainer - JettyContainerConfiguration</li>
     *  <li>DefaultJettyContainerConfiguration - Properties</li>
     *  <li>PicoJettyContainer - Listener</li>
     *  <li>PicoJettyContainer - Context</li>
     * </ul>
     * If a <code>Context</code> is added as a child to a
     * <code>PicoJettyContainer</code> a new <code>ContextHandler</code>
     * is created and used for the new context. There is no way of
     * getting a reference to this context handler again. If access to
     * the context or context handler is needed, create a new context
     * handler using a <code>PicoJettyContainer</code> and deploy contexts
     * directly to it.
     *
     * TODO: Sould PicoJettyContainer also implement ContextHandler for a default and open ContextHandler?
     *
     * @param object the parent
     * @param object1 the child
     */
    protected void setParent( Object object, Object object1 )
    {
        if( object instanceof PicoJettyContainer
          && object1 instanceof JettyContainerConfiguration )
        {
            PicoJettyContainer container = (PicoJettyContainer)object;
            DefaultJettyContainerConfiguration configuration =
              (DefaultJettyContainerConfiguration)object1;
            container.setConfiguration( configuration );
        }
        else if( object instanceof DefaultJettyContainerConfiguration
          && object1 instanceof Properties )
        {
            Properties property = (Properties)object1;
            DefaultJettyContainerConfiguration configuration =
              (DefaultJettyContainerConfiguration)object;
            if( null == configuration.getProperties() )
            {
                configuration.setProperties( property );
            }
            else
            {
                configuration.getProperties().putAll( property );
            }
        }
        else if( object instanceof PicoJettyContainer
          && object1 instanceof Listener )
        {
            try
            {
                PicoJettyContainer container = (PicoJettyContainer)object;
                container.initialize();
                container.addListener( (Listener)object1 );
            }
            catch( ListenerException le )
            {
                throw new RuntimeException( "The given listener couldn't be added.", le );
            }
        }
        else if( object instanceof PicoJettyContainer
          && object1 instanceof Context )
        {
            try
            {
                PicoJettyContainer container = (PicoJettyContainer)object;
                container.initialize();
                container.createContextHandler().addContext( (Context)object1 );
            }
            catch( ContextException ce )
            {
                throw new RuntimeException( "The given context couldn't be added.", ce );
            }
        }
    }

    /**
     * Create a new instance of something.
     *
     * @param name the command
     * @return a new instance of some type or null
     */
    protected Object createNode( Object name )
    {
        return createNode( name, Collections.EMPTY_MAP );
    }

    /**
     * Not used
     *
     * @param name
     * @param value
     * @return null
     */
    protected Object createNode( Object name, Object value )
    {
        return null;
    }

    /**
     * Create a new instance of something.
     * <br/><br/>
     * Possible commands are:
     * <ul>
     *  <li>picoJettyContainer</li>
     *  <li>configuration</li>
     *  <li>parameter</li>
     *  <li>listener</li>
     *  <li>context</li>
     * </ul>
     *
     * @param object the command
     * @param map parameters
     * @return a new instance of some type or null
     */
    protected Object createNode( Object object, Map map )
    {
        if( object instanceof String )
        {
            final String command = (String)object;
            if( "picoJettyContainer".equals( command ) )
            {
                return getPicoJettyContainer( map );
            }
            else if( "configuration".equals( command ) )
            {
                return getConfiguration( map );
            }
            else if( "parameter".equals( command ) )
            {
                return getParameter( map );
            }
            else if( "listener".equals( command ) )
            {
                return getListener( map );
            }
            else if( "context".equals( command ) )
            {
                return getContext( map );
            }
        }
        return null;
    }

    /**
     * Not used
     *
     * @param name
     * @param attributes
     * @param object1
     * @return null
     */
    protected Object createNode( Object name, Map attributes, Object object1 )
    {
        return null;
    }

    /**
     * Create a PicoJettyContainer
     * <br/><br/>
     * Possible parameters are:
     * <ul>
     *  <li>contextMonitor</li>
     *  <li>listenerMonitor</li>
     * </ul>
     * @param map a map of parameters. Used keys are
     *        <code>contextMonitor</code> and <code>listenerMonitor</code>.
     * @return a new PicoJettyContainer, initialized with the
     *         optional monitors.
     */
    private PicoJettyContainer getPicoJettyContainer( Map map )
    {
        ContextMonitor contextMonitor = (ContextMonitor)map.get( "contextMonitor" );
        ListenerMonitor listenerMonitor = (ListenerMonitor)map.get( "listenerMonitor" );
        return new PicoJettyContainer( contextMonitor, listenerMonitor );
    }

    /**
     * Create a JettyContainerConfiguration
     *
     * @param map a map of parameters. Used keys are
     *        <code>configuration</code> and <code>configurationURL</code>.
     * @return a new instance of a <code>DefaultJettyContainerConfiguration</code>.
     * @throws IllegalArgumentException if
     */
    private JettyContainerConfiguration getConfiguration( Map map )
    {
        if( map.containsKey( "configuration" ) &&
          map.containsKey( "configurationURL" ) )
        {
            throw new IllegalArgumentException( "Either 'configuration' or "
              + "'configurationURL' can be given. Not both." );
        }
        DefaultJettyContainerConfiguration configuration = new DefaultJettyContainerConfiguration();
        if( map.containsKey( "configuration" ) )
        {
            configuration.setConfiguration( map.get( "configuration" ) );
        }
        else if( map.containsKey( "configurationURL" ) )
        {
            try
            {
                URL url = new URL( (String)map.get( "configurationURL" ) );
                configuration.setConfiguration( url );
            }
            catch( MalformedURLException mue )
            {
                throw new IllegalArgumentException( "The given URL [" +
                  map.get( "configurationURL" ) + "] was not well formed. " +
                  "Error message was [" + mue.getMessage() + "]." );
            }
        }
        return configuration;
    }

    /**
     * Fetch a Jetty configuration parameter from a given map
     *
     * @param map a map including the keys <code>key</code>
     *        and <code>value</code>.
     * @return a new Properties instance with one key value pair
     * @throws IllegalArgumentException if 'key' or 'value' was missing
     *         in the given map
     */
    private Properties getParameter( Map map )
    {
        if( !map.containsKey( "key" ) || !map.containsKey( "value" ) )
        {
            throw new IllegalArgumentException( "Missing argument. "
              + "Both 'key' and 'value' are needed." );
        }
        Properties properties = new Properties();
        properties.setProperty( (String)map.get( "key" ), (String)map.get( "value" ) );
        return properties;
    }

    /**
     * Create a listener
     *
     * @param map with at least the parameters <code>port</code>
     *        and <code>type</code>. <code>host</code> is also
     *        supported.
     * @return a new <code>DefaultListener</code> instance
     */
    private Listener getListener( Map map )
    {
        //System.out.println( "getListener [" + map + "]" );
        if( !map.containsKey( "port" ) || !map.containsKey( "type" ) )
        {
            throw new IllegalArgumentException( "Missing argument. "
              + "At least 'port' and 'type' must be given." );
        }
        int type = -1;
        final String typeString = (String)map.get( "type" );
        if( typeString.equalsIgnoreCase( "http" ) )
        {
            type = Listener.HTTP;
        }
        else if( typeString.equalsIgnoreCase( "tsl") || typeString.equalsIgnoreCase( "ssl" ) )
        {
            type = Listener.TSL;
        }
        else if( typeString.equalsIgnoreCase( "ajp13" ) )
        {
            type = Listener.AJP13;
        }
        if( -1 == type )
        {
            throw new IllegalArgumentException( "Unknown listener type [" +
              typeString + "]." );
        }
        return new DefaultListener( (String)map.get( "host" ),
                                    Integer.parseInt( (String)map.get( "port" ) ),
                                    type );
    }

    /**
     * Create a context
     *
     * @param map map of needed arguments
     * @return a new context
     * @throws IllegalArgumentException if a mandatory
     *         parameter was missing in the given map
     *         or if the parameters had errors
     */
    private Context getContext( Map map )
    {
        if( !map.containsKey( "webPath" ) ||
            !map.containsKey( "warPath" ) ||
            !map.containsKey( "instantiator" ) )
        {
            throw new IllegalArgumentException( "Missing argument. At least" +
              " 'instantiator', 'webPath' and 'warPath' must be given." );
        }
        String webPath = (String)map.get( "webPath" );
        String[] hosts = getHosts( (String)map.get( "hosts" ) );
        URL resource;
        boolean extractWar = false;
        Instantiator instantiator = (Instantiator)map.get( "instantiator" );
        try
        {
            File resourceFile = new File( (String)map.get( "warPath" ) );
            if( !resourceFile.exists() )
            {
                throw new IllegalArgumentException( "File [" +
                  map.get( "warParh" ) + "] given in parameter 'warPath' doesn't exist." );
            }
            resource = resourceFile.toURL();
        }
        catch( MalformedURLException mue )
        {
            throw new IllegalArgumentException( "Couldn't translate parameter "
              + "'warPath' into a URL. Message was [" + mue.getMessage() + "]." );
        }
        return new DefaultContext( webPath,
                                   hosts,
                                   resource,
                                   extractWar,
                                   instantiator );
    }

    /**
     * Create an array from the ',' separated host list.
     *
     * @param hostString the string to split up
     * @return a new string array or null
     */
    private String[] getHosts( final String hostString )
    {
        if( null == hostString || "".equals( hostString ) )
        {
            return null;
        }
        String[] hosts = hostString.split( "," );
        for( int i = 0; i < hosts.length; i++ )
        {
            hosts[i] = hosts[i].trim();
            System.out.println( hosts[i] );
        }
        return hosts;
    }
}