/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.xsyslog.metadata;

import org.w3c.dom.Element;

/**
 * Representation of a the configuration of route map between
 * destinations and channels. This includes definitions of
 * destinations and routes.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-06-27 03:27:51 $
 */
public class DestinationMetaData
{
    /**
     * The name of the destination.
     */
    private final String m_name;

    /**
     * The type of the destination.
     */
    private final String m_type;

    /**
     * The configuration of the destination.
     */
    private final Element m_configuration;

    /**
     * Create the destination.
     *
     * @param name the name of the destination.
     * @param type the type of the destination.
     * @param configuration the configuration for destination
     */
    public DestinationMetaData( String name, String type, Element configuration )
    {
        m_name = name;
        m_type = type;
        m_configuration = configuration;
    }

    /**
     * Return the name of the destination.
     *
     * @return the name of the destination.
     */
    public String getName()
    {
        return m_name;
    }

    /**
     * Return the type of the destination.
     *
     * @return the type of the destination.
     */
    public String getType()
    {
        return m_type;
    }

    /**
     * Return the configuration of the destination.
     *
     * @return the configuration of the destination.
     */
    public Element getConfiguration()
    {
        return m_configuration;
    }
}
