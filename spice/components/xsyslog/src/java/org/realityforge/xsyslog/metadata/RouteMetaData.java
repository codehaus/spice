/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.xsyslog.metadata;

/**
 * Representation of a route between particular channels and
 * destinations. This includes a list of destinations, a list of
 * channels to include, a list of channels to exclude and a list
 * of filters to apply before sending messages to destinations.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-04-07 05:07:40 $
 */
public class RouteMetaData
{
    /**
     * The names of the destinations to route to.
     */
    private final String[] m_destinations;

    /**
     * The names/patterns of channels to include.
     */
    private final String[] m_includes;

    /**
     * The names/patterns of channels to exclude.
     */
    private final String[] m_excludes;

    /**
     * The level to filter log messages at.
     */
    private final String m_level;

    /**
     * Create a route.
     *
     * @param destinations the destinations to route to
     * @param includes the channels to include
     * @param excludes the channels to exclude
     */
    public RouteMetaData( final String[] destinations,
                          final String[] includes,
                          final String[] excludes,
                          final String level )
    {
        if( null == destinations )
        {
            throw new NullPointerException( "destinations" );
        }
        if( null == includes )
        {
            throw new NullPointerException( "includes" );
        }
        if( null == excludes )
        {
            throw new NullPointerException( "excludes" );
        }
        m_destinations = destinations;
        m_includes = includes;
        m_excludes = excludes;
        m_level = level;
    }

    /**
     * Return the names of the destinations to route to.
     *
     * @return the names of the destinations to route to.
     */
    public String[] getDestinations()
    {
        return m_destinations;
    }

    /**
     * Return the level to filter log messages at.
     *
     * @return the level to filter log messages at.
     */
    public String getLevel()
    {
        return m_level;
    }

    /**
     * Return the names/patterns of channels to include.
     *
     * @return the names/patterns of channels to include.
     */
    public String[] getIncludes()
    {
        return m_includes;
    }

    /**
     * Return the names/patterns of channels to exclude.
     *
     * @return the names/patterns of channels to exclude.
     */
    public String[] getExcludes()
    {
        return m_excludes;
    }
}
