/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.tools.tasks;

/**
 * An Ant type representing a set of Filter definitions.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-11-20 06:42:49 $
 */
public class FilterSet
    extends PluginSet
{
    /**
     * Add an Filter definition that will create Filter to process metadata.
     *
     * @param element the Filter definition
     */
    public void addFilter( final PluginElement element )
    {
        addPlugin( "Filter", element );
    }

    /**
     * Add a set of Filters.
     *
     * @param set the Filter set
     */
    public void addFilterSet( final FilterSet set )
    {
        addPluginSet( set );
    }
}
