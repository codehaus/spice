/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.xsyslog.manager;

import org.apache.log.Hierarchy;
import org.apache.log.Logger;
import org.apache.log.LogTarget;
import org.apache.log.util.Closeable;
import org.apache.avalon.framework.logger.AbstractLogEnabled;
import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.activity.Disposable;
import org.apache.avalon.framework.context.Contextualizable;
import org.apache.avalon.framework.context.Context;
import org.apache.avalon.framework.context.ContextException;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationUtil;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.realityforge.xsyslog.metadata.SyslogMetaData;
import org.realityforge.xsyslog.metadata.RouteMetaData;
import org.realityforge.xsyslog.metadata.DestinationMetaData;
import org.realityforge.xsyslog.runtime.LogTargetFactory;
import org.realityforge.configkit.ConfigValidator;
import org.realityforge.configkit.ConfigValidatorFactory;
import org.w3c.dom.Element;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.io.InputStream;

/**
 * This class is responsible for managing the Loggers and LogTargets
 * for an SyslogMetaData.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-04-16 10:56:03 $
 * @phoenix.component
 */
public class SyslogManager
    extends AbstractLogEnabled
    implements Contextualizable, Initializable, Disposable
{
    /**
     * The logger hierarchy that is being managed.
     */
    private final Hierarchy m_hierarchy = new Hierarchy();

    /**
     * Logger listener that listens to creation og Loggers in hierarchy.
     */
    private final SyslogLoggerListener m_listener = new SyslogLoggerListener( this );

    /**
     * The map of {@link LogTarget} objects created by manager.
     */
    private final Map m_targets = new HashMap();

    /**
     * The map of {@link RouteMatcher} objects created by manager.
     */
    private final Map m_matchers = new HashMap();

    /**
     * The map of {@link ConfigValidator} objects created by manager.
     */
    private final Map m_validators = new HashMap();

    /**
     * The map of {@link org.realityforge.xsyslog.runtime.LogTargetFactory} objects created by manager.
     */
    private final Map m_factorys = new HashMap();

    /**
     * The MetaData that defines the logging space this manager manages.
     */
    private SyslogMetaData m_metaData;

    /**
     * Retrieve SyslogMetaData from context.
     *
     * @param context the context
     * @throws ContextException if unable to locate metadata
     * @phoenix.entry type="SyslogMetaData"
     */
    public void contextualize( final Context context )
        throws ContextException
    {
        m_metaData = (SyslogMetaData)context.get( SyslogMetaData.class.getName() );
    }

    /**
     * Setup the manager. This involves listening to
     * the Hierarchy for creation of any  new loggers
     * and setting up matcher and destination maps.
     *
     * @throws Exception if unable to initialize manager
     */
    public synchronized void initialize()
        throws Exception
    {
        m_hierarchy.addLoggerListener( m_listener );

        initializeRoutes();
        initializeTargets();
    }

    /**
     * Dispose the SyslogManager which involves
     */
    public synchronized void dispose()
    {
        //Remove listener for creation of new loggers
        m_hierarchy.removeLoggerListener( m_listener );

        //Remove all log targets ..
        //efectively invalidating the logger hierarchy
        m_hierarchy.getRootLogger().unsetLogTargets( true );

        m_matchers.clear();

        //Close all the Closeable LogTargets
        final Iterator iterator = m_targets.values().iterator();
        while( iterator.hasNext() )
        {
            final LogTarget target = (LogTarget)iterator.next();
            if( target instanceof Closeable )
            {
                try
                {
                    ( (Closeable)target ).close();
                }
                catch( final Exception e )
                {
                    final String message = "Error clsoing target: " + target;
                    getLogger().warn( message, e );
                }
            }
        }
        m_targets.clear();
    }

    /**
     * Method called when a logger is added to system.
     * This method should set up the logger and associate any
     * {@link LogTarget}s with Logger as necessary.
     *
     * @param channel the name of the channel
     * @param logger the logger object
     */
    void loggerCreated( final String channel, final Logger logger )
    {
        //Collect all the log targets declared for channel
        final List targets = new ArrayList();
        final RouteMetaData[] routes = m_metaData.getRoutes();
        for( int i = 0; i < routes.length; i++ )
        {
            final RouteMetaData route = routes[ i ];
            if( channelMatches( channel, route ) )
            {
                final String[] destinations = route.getDestinations();
                for( int j = 0; j < destinations.length; j++ )
                {
                    targets.add( getTarget( destinations[ j ] ) );
                }
            }
        }

        //Setup logger object
        final LogTarget[] logTargets =
            (LogTarget[])targets.toArray( new LogTarget[ targets.size() ] );
        logger.setAdditivity( false );
        logger.setLogTargets( logTargets );
    }

    /**
     * Create matchers for all the routes specified by metadata.
     */
    private void initializeRoutes()
    {
        final RouteMetaData[] routes = m_metaData.getRoutes();
        for( int i = 0; i < routes.length; i++ )
        {
            final RouteMetaData route = routes[ i ];
            final RouteMatcher matcher =
                new RouteMatcher( route.getIncludes(), route.getExcludes() );
            m_matchers.put( route, matcher );
        }
    }

    /**
     * Create all the LogTargets specified by MetaData.
     */
    private void initializeTargets()
        throws Exception
    {
        final DestinationMetaData[] destinations = m_metaData.getDestinations();
        for( int i = 0; i < destinations.length; i++ )
        {
            final DestinationMetaData destination = destinations[ i ];
            final String name = destination.getName();
            final String type = destination.getType();
            final Element configuration = destination.getConfiguration();
            final LogTarget target = createTarget( name, type, configuration );
            if( getLogger().isDebugEnabled() )
            {
                getLogger().debug( "Target named " + name + " of type " + type +
                                   " created as " + target );
            }
            m_targets.put( name, target );
        }
        m_validators.clear();
    }

    /**
     * Create a LogTarget for specified destination.
     *
     * @param name the name of destination
     * @param type the type of destination
     * @param element the configuration for destination
     * @return the created LogTarget
     * @throws Exception if unable to create target
     */
    private LogTarget createTarget( final String name,
                                    final String type,
                                    final Element element )
        throws Exception
    {
        final LogTargetFactory factory = getFactory( name, type );
        final ConfigValidator validator = getValidator( factory );
        if( null != validator )
        {
            final TargetErrorHandler errorHandler = new TargetErrorHandler( name, type );
            setupLogger( errorHandler );
            validator.validate( element );
        }
        else
        {
            if( getLogger().isDebugEnabled() )
            {
                getLogger().debug( "Target named " + name + " of type " + type +
                                   " is not being validated as no schema defined." );
            }
        }

        final Configuration configuration = ConfigurationUtil.toConfiguration( element );
        try
        {
            return factory.createTarget( configuration );
        }
        catch( ConfigurationException e )
        {
            if( getLogger().isWarnEnabled() )
            {
                getLogger().warn( "Target named " + name + " of type " + type +
                                  " could not be created due to: " + e, e );
            }
            throw e;
        }
    }

    /**
     * Get a LogTargetFactory for specified type.
     *
     * @param name the name of destination requesting factory
     * @param type the type of factory
     * @return the LogTargetFactory
     * @throws Exception if unable to get LogTargetFactory
     */
    private LogTargetFactory getFactory( final String name, final String type )
        throws Exception
    {
        LogTargetFactory factory = (LogTargetFactory)m_factorys.get( type );
        if( null != factory )
        {
            return factory;
        }
        final String[] packages = m_metaData.getPackages();
        for( int i = 0; i < packages.length; i++ )
        {
            final String classname = packages[ i ] + name;
            final Object object;
            try
            {
                final Class clazz = Class.forName( classname );
                object = clazz.newInstance();
            }
            catch( ClassNotFoundException e )
            {
                continue;
            }
            catch( Exception e )
            {
                final String message = classname + " could not be instantiated for destination " + name;
                throw new Exception( message );
            }

            if( !( object instanceof LogTargetFactory ) )
            {
                final String message =
                    classname + " does not designate a LogTargetFactory for destination " + name;
                throw new Exception( message );
            }
            m_factorys.put( type, object );
            return (LogTargetFactory)object;
        }

        final String message =
            "Could not load LogTargetFactory of type " +
            type + " for destination " + name;
        throw new Exception( message );
    }

    /**
     * Retrieve validator for specified factory.
     * If there is no validator cached for factory then
     * create one by looking for relaxNG schema named
     * <code>MyFactory-schema.xml</code>.
     *
     * @param factory the factory
     * @return the validator (may be null).
     * @throws Exception if unable to create validator from schema
     */
    private ConfigValidator getValidator( final LogTargetFactory factory )
        throws Exception
    {
        if( !m_validators.containsKey( factory ) )
        {
            final Class clazz = factory.getClass();
            final String resource = clazz.getName().replace( '.', '/' ) + "-schema.xml";
            final InputStream inputStream = clazz.getClassLoader().getResourceAsStream( resource );
            ConfigValidator validator = null;
            if( null != inputStream )
            {
                try
                {
                    validator =
                        ConfigValidatorFactory.create( ConfigValidatorFactory.RELAX_NG, inputStream );
                }
                catch( final Exception e )
                {
                    if( getLogger().isWarnEnabled() )
                    {
                        getLogger().warn( "Error reading schema: " + resource, e );
                    }
                    throw e;
                }
            }
            m_validators.put( factory, validator );
        }
        return (ConfigValidator)m_validators.get( factory );
    }

    /**
     * Test whether channel is part of specified route.
     *
     * @param channel the channel name
     * @param route the route
     * @return true if channel is a part of the route, false otherwise
     */
    private boolean channelMatches( final String channel,
                                    final RouteMetaData route )
    {
        final RouteMatcher matcher = (RouteMatcher)m_matchers.get( route );
        if( null == matcher )
        {
            final String message =
                "Trying to access non-existent route " + route;
            throw new IllegalStateException( message );
        }
        return matcher.match( channel );
    }

    /**
     * Return the target for specified destination.
     *
     * @param destination the destination represented by target
     * @return the target for specified destination.
     */
    private synchronized LogTarget getTarget( final String destination )
    {
        final LogTarget target = (LogTarget)m_targets.get( destination );
        if( null == target )
        {
            final String message =
                "Trying to access non-existent destination " + destination;
            throw new IllegalStateException( message );
        }
        return target;
    }
}

