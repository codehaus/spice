/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.tools.tasks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.DataType;
import org.apache.tools.ant.types.Reference;

/**
 * An Ant type that represents a set of Plugins.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-11-20 06:42:49 $
 */
abstract class PluginSet
    extends DataType
{
    /**
     * Set of PluginElement objects.
     */
    private final ArrayList m_plugins = new ArrayList();

    /**
     * Set of PluginSet objects.
     */
    private final ArrayList m_sets = new ArrayList();

    /**
     * Add a plugin to set.
     *
     * @param type the type
     * @param element the interceptor definition
     */
    void addPlugin( final String type,
                    final PluginElement element )
    {
        if( null == element.getName() )
        {
            throw new BuildException( type + " must have a name" );
        }
        m_plugins.add( element );
    }

    /**
     * Add a plugin to set.
     *
     * @param set the set
     */
    void addPluginSet( final PluginSet set )
    {
        m_sets.add( set );
    }

    /**
     * Convert PluginSet to a collection of plugins.
     *
     * @return the collection of Plugins
     */
    Collection toPlugins()
    {
        final Collection result = new ArrayList();
        result.addAll( m_plugins );
        final Iterator iterator = m_sets.iterator();
        while( iterator.hasNext() )
        {
            final PluginSet set = (PluginSet)iterator.next();
            result.addAll( set.toPlugins() );
        }
        return result;
    }

    /**
     * Makes this instance in effect a reference to another PluginSet
     * instance.
     *
     * <p>You must not set another attribute or nest elements inside
     * this element if you make it a reference.</p>
     *
     * @param reference the reference to which this instance is associated
     * @exception BuildException if this instance already has been configured.
     */
    public void setRefid( final Reference reference )
        throws BuildException
    {
        if( !m_plugins.isEmpty() || !m_sets.isEmpty() )
        {
            throw tooManyAttributes();
        }
        // change this to get the objects from the other reference
        final Object object =
            reference.getReferencedObject( getProject() );
        final Class clazz = getClass();
        if( clazz.isInstance( object ) )
        {
            final PluginSet other = (PluginSet)object;
            m_plugins.addAll( other.m_plugins );
            m_sets.addAll( other.m_sets );
        }
        else
        {
            final String message = reference.getRefId() +
                " doesn\'t refer to a " + clazz.getName();
            throw new BuildException( message );
        }

        super.setRefid( reference );
    }
}
