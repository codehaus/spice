/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.xsyslog.metadata;

/**
 * Representation of a the configuration of route map between
 * destinations and channels. This includes definitions of
 * destinations and routes including list of destinations
 * predefined by application.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-06-27 03:27:51 $
 */
public class SyslogMetaData
{
    /**
     * The names of destinations predefined by the application.
     */
    private final String[] m_predefined;

    /**
     * The packages in which to search for destinations.
     */
    private final String[] m_packages;

    /**
     * The destinations that can be routed to.
     */
    private final DestinationMetaData[] m_destinations;

    /**
     * The routes between channels and destinations.
     */
    private final RouteMetaData[] m_routes;

    /**
     * Create the syslog configuration.
     *
     * @param predefined the names
     * @param packages the packages
     * @param destinations the destination
     * @param routes the routes
     */
    public SyslogMetaData( final String[] predefined,
                           final String[] packages,
                           final DestinationMetaData[] destinations,
                           final RouteMetaData[] routes )
    {
        if( null == predefined )
        {
            throw new NullPointerException( "predefined" );
        }
        if( null == packages )
        {
            throw new NullPointerException( "packages" );
        }
        if( null == destinations )
        {
            throw new NullPointerException( "destinations" );
        }
        if( null == routes )
        {
            throw new NullPointerException( "routes" );
        }
        m_predefined = predefined;
        m_packages = packages;
        m_destinations = destinations;
        m_routes = routes;
    }

    /**
     * Return the names of destinations predefined by the application.
     *
     * @return the names of destinations predefined by the application.
     */
    public String[] getPredefined()
    {
        return m_predefined;
    }

    /**
     * Return the packages in which to search for destinations.
     *
     * @return the packages in which to search for destinations.
     */
    public String[] getPackages()
    {
        return m_packages;
    }

    /**
     * Return the destinations that can be routed to.
     *
     * @return the destinations that can be routed to.
     */
    public DestinationMetaData[] getDestinations()
    {
        return m_destinations;
    }

    /**
     * Return the destination with specified name or null if no such destination.
     *
     * @return the destination with specified name or null if no such destination.
     */
    public DestinationMetaData getDestination( final String name )
    {
        for( int i = 0; i < m_destinations.length; i++ )
        {
            final DestinationMetaData destination = m_destinations[ i ];
            if( destination.getName().equals( name ) )
            {
                return destination;
            }
        }

        return null;
    }

    /**
     * Return the routes between channels and destinations.
     *
     * @return the routes between channels and destinations.
     */
    public RouteMetaData[] getRoutes()
    {
        return m_routes;
    }
}
