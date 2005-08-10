/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.jervlet.containers.jetty.avalon;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import java.util.List;

import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.activity.Startable;
import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.configuration.DefaultConfigurationSerializer;
import org.apache.avalon.framework.logger.LogEnabled;
import org.apache.avalon.framework.logger.Logger;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.avalon.framework.service.Serviceable;

import org.codehaus.spice.jervlet.Container;
import org.codehaus.spice.jervlet.ContextHandler;
import org.codehaus.spice.jervlet.ContextMonitor;
import org.codehaus.spice.jervlet.Listener;
import org.codehaus.spice.jervlet.ListenerException;
import org.codehaus.spice.jervlet.ListenerHandler;
import org.codehaus.spice.jervlet.ListenerMonitor;
import org.codehaus.spice.jervlet.containers.jetty.DefaultJettyContainer;
import org.codehaus.spice.jervlet.containers.jetty.JettyContainer;
import org.codehaus.spice.jervlet.containers.jetty.ShieldingJettyContainer;

/**
 * Avalon component wrapping a Jetty Server
 *
 * @author Johan Sjoberg
 * @dna.component
 */
public class AvalonJettyContainer
    implements Container, ListenerHandler, LogEnabled,
      Configurable, Serviceable, Initializable, Startable
{
    private Logger m_logger;
    private JettyContainer m_container;

    /**
     * Recieve a logger
     *
     * @param logger The Avalon <code>Logger</code>
     * @dna.logger
     */
    public void enableLogging( final Logger logger )
    {
        m_logger = logger;
    }

    /**
     * Configure the class
     *
     * @param configuration The Avalon <code>Configuration</code>
     * @throws ConfigurationException if the configuration was incorrect
     * @dna.configuration
     */
    public void configure( final Configuration configuration ) throws ConfigurationException
    {
        m_logger.debug( "Configuring..." );
        if( configuration.getChild( "shield-jetty" ).getValueAsBoolean( true ) )
        {
            final Properties jettyProperties = getJettyProperties( configuration );
            m_container = new ShieldingJettyContainer();
            ((ShieldingJettyContainer)m_container).addJettyProperties( jettyProperties );
        }
        else
        {
            m_container = new DefaultJettyContainer();
        }
        m_container.setJettyConfiguration( getJettyConfiguration( configuration ) );
    }

    /**
     * Get the Service Manager.
     *
     * @param manager The service manager.
     *
     * @dna.dependency type="org.codehaus.spice.jervlet.ContextMonitor" optional=true
     * @dna.dependency type="org.codehaus.spice.jervlet.listenerMonitor" optional=true
     */
    public void service( final ServiceManager manager ) throws ServiceException //TODO: Add support for Instantiators.
    {
        if( manager.hasService( ContextMonitor.class.getName() ) )
        {
            final ContextMonitor contextMonitor =
              (ContextMonitor)manager.lookup( ContextMonitor.class.getName() );
            if( null == contextMonitor )
            {
                m_logger.error( "ContextMonitor was null, leaving it unset." );
            }
            m_container.setContextMonitor( contextMonitor );
        }
        else
        {
            m_logger.info( "Got no ContextMonitor, leaving it unset." );
            m_container.setContextMonitor( null );
        }

        if( manager.hasService( ListenerMonitor.class.getName() ) )
        {
            final ListenerMonitor listenerMonitor =
              (ListenerMonitor)manager.lookup( ListenerMonitor.class.getName() );
            if( null == listenerMonitor )
            {
                m_logger.error( "ListenerMonitor was null, leaving it unset." );
            }
            m_container.setListenerMonitor( listenerMonitor );
        }
        else
        {
            m_logger.info( "Got no ListenerMonitor, leaving it unset." );
            m_container.setListenerMonitor( null );
        }
    }

    /**
     * Initialize the component
     *
     * @throws Exception on all errors
     */
    public void initialize() throws Exception
    {
        m_logger.info( "Initializing..." );
        m_container.initialize();
    }

    /**
     * Start the Jetty container
     *
     * @throws Exception on all errors
     */
    public void start() throws Exception
    {
        m_logger.info( "Starting..." );
        m_container.start();
    }

    /**
     * Stop the Jetty container
     *
     * @throws Exception on all errors
     */
    public void stop() throws Exception
    {
        m_logger.info( "Stopping..." );
        m_container.stop();
    }

    /**
     * Populate a new <code>Properties</code> object with values from
     * a configuration. The format is:
     * <br/><br/>
     * &lt;system-properties&gt;
     * &lt;property key="the key" value="the value"/&gt;
     * ...
     * &lt;/system-properties&gt;
     * <br/><br/>
     * If no properties were configured the returned
     * <code>Properties</code> object will be empty.
     *
     * @param configuration Avalon configuration
     * @return a new <code>Properties</code> object, filled with
     *         given system properties
     */
    private Properties getJettyProperties( final Configuration configuration )
    {
        final Properties properties = new Properties();
        final Configuration[] propertyElements =
          configuration.getChild( "jetty-system-properties" ).getChildren( "property" );

        for( int i = 0; i < propertyElements.length; i++ )
        {
            properties.setProperty( propertyElements[i].getAttribute( "key", "" ),
                                    propertyElements[i].getAttribute( "value", "" ) );
        }

        return properties;
    }

    /**
     * Create a configuration object to instantiate Jetty with.
     * <br/></br>
     * There are four possible return values;
     * (i) a <code>String</code> representing a Jetty configuration file,
     * (ii) a <code>String</code> that IS the Jetty configuration,
     * (iii) a <code>URL</code> pointing at a Jetty configuration and
     * (iv) null.
     * Note that no checks about the configuration's correctness are
     * done here.
     * <br/></br>
     * Accepted configuration elements are
     * <b>jetty-configuration-file</b>,
     * <b>jetty-configuration</b> and
     * <b>jetty-configuration-url</b>. Note that the Jetty
     * configuration always starts with a <b>&lt;Configure&gt;</b>
     * element.
     *
     * @param configuration Avalon configuration
     * @return <code>String</code>, <code>URL</code> or <code>null</code>
     */
    private Object getJettyConfiguration( final Configuration configuration )
        throws ConfigurationException
    {
        String configurationFile = configuration.getChild(
          "jetty-configuration-file" ).getValue( null );
        if( null != configurationFile )
        {
            configurationFile = configurationFile.replace( '\\', File.separatorChar );
            configurationFile = configurationFile.replace( '/', File.separatorChar );
            m_logger.info( "Using Jetty configuration from file [" + configurationFile + "]." );
            return configurationFile;
        }

        if( null != configuration.getChild( "jetty-configuration" ).getValue( null ) )
        {
            String configurationString;
            try
            {
                final DefaultConfigurationSerializer serializer =
                  new DefaultConfigurationSerializer();
                configurationString = serializer.serialize(
                  configuration.getChild( "jetty-configuration" ).getChild( "Configure" ) );
            }
            catch( Exception e )
            {
                final String message = "Serialization of Jetty's configuration failed.";
                m_logger.error( message, e );
                throw new ConfigurationException( message, e );
            }
            if( configurationString.indexOf( "?>" ) > 0 )
            {
                configurationString = configurationString.substring(
                  configurationString.indexOf( "?>" ) + 2 );
            }
            m_logger.info( "Using embedded Jetty configuration." );
            return configurationString;
        }

        final String urlString = configuration.getChild(
            "jetty-configuration-url", true ).getValue( null );
        if( null != urlString )
        {
            final URL urlConfiguration;
            try
            {
                urlConfiguration = new URL( urlString );
            }
            catch( MalformedURLException mue )
            {
                final String message = "Bad Jetty configuration URL.";
                m_logger.error( message, mue );
                throw new ConfigurationException( message, mue );
            }
            m_logger.info( "Using Jetty configuration from URL ["
              + urlConfiguration.getPath() + "]." );
            return urlConfiguration;
        }
        return null;
    }

    /**
     * Create a new context handler.
     *
     * @return A new ContextHandler.
     */
    public ContextHandler createContextHandler()
    {
        return m_container.createContextHandler();
    }

    /**
     * Destroy a context handler. If the given context
     * handler still has contexts, they will be stopped
     * and removed before destruction.
     *
     * @param contextHandler The ContextHandler to destroy
     */
    public void destroyContextHandler( ContextHandler contextHandler )
    {
        m_container.destroyContextHandler( contextHandler );
    }

    /**
     * Add a <code>Listener</code> the container.
     */
    public void addListener( Listener listener ) throws ListenerException
    {
        m_container.addListener( listener );
    }

    /**
     * Remove a <code>Listener</code> from the container.
     */
    public void removeListener( Listener listener ) throws ListenerException
    {
        m_container.removeListener( listener );
    }

    /**
     * Start a <code>Listener</code>.
     */
    public void startListener( Listener listener ) throws ListenerException
    {
        m_container.startListener( listener );
    }

    /**
     * Stop a <code>Listener</code>.
     */
    public void stopListener( Listener listener ) throws ListenerException
    {
        m_container.stopListener( listener );
    }

    /**
     * Fetch a list of all current <code>Listener</code>s. If there are
     * no listener the returned list can be empty.
     *
     * @return All new list all current listeners.
     */
    public List getListeners()
    {
        return m_container.getListeners();
    }

    /**
     * Check if a <code>Listener</code> is started or not.
     *
     * @param listener The listener
     * @return True if the listener is started, else false.
     */
    public boolean isStarted( Listener listener )
    {
        return m_container.isStarted( listener );
    }
}
