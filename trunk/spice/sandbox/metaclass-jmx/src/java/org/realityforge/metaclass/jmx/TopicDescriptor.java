/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.jmx;

import javax.management.modelmbean.ModelMBeanInfo;

/**
 * A simple object that contains MBeanInfo object and associated name.
 * The name is the "topic" of the info object. Each object can have
 * multiple "topics" associated with it in the JMX system. Usually
 * one topic for the object (with null name) and one for each management
 * interface with specified name.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-10-17 07:38:50 $
 */
public class TopicDescriptor
{
    /**
     * The topic name.
     */
    private final String m_name;

    /**
     * The associated info object.
     */
    private final ModelMBeanInfo m_info;

    /**
     * Create descriptor with specified name and info.
     *
     * @param name the name
     * @param info the info
     */
    public TopicDescriptor( final String name,
                            final ModelMBeanInfo info )
    {
        if( null == info )
        {
            throw new NullPointerException( "info" );
        }
        m_name = name;
        m_info = info;
    }

    /**
     * Return the topic name (May be null).
     *
     * @return the topic name (May be null).
     */
    public String getName()
    {
        return m_name;
    }

    /**
     * Return the associated Info object.
     *
     * @return the associated Info object.
     */
    public ModelMBeanInfo getInfo()
    {
        return m_info;
    }
}