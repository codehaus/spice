/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.tools.tasks;

import org.apache.tools.ant.Task;
import org.apache.tools.ant.BuildException;

/**
 * An ant task to add one plugin set to another.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-11-20 10:00:38 $
 */
abstract class AddToPluginSetTask
    extends Task
{
    /**
     * The rexpected type of plugin.
     */
    private Class m_type;

    /**
     * The PluginSet that will be added to.
     */
    private String m_id;

    /**
     * The PluginSet that will be added.
     */
    private String m_refid;

    /**
     * Create task.
     *
     * @param type the expected type of PluginSet.
     */
    AddToPluginSetTask( final Class type )
    {
        m_type = type;
    }

    /**
     * Set the id for PluginSet to add to.
     *
     * @param id the id for PluginSet to add to.
     */
    public void setId( final String id )
    {
        m_id = id;
    }

    /**
     * Set the id of PluginSet that will be added.
     *
     * @param refid the id of PluginSet that will be added.
     */
    public void setRefid( final String refid )
    {
        m_refid = refid;
    }

    /**
     * Join PluginSets.
     *
     * @throws BuildException if bad ids specified
     */
    public void execute()
        throws BuildException
    {
        validate();
        final Object idObject = getProject().getReference( m_id );
        final Object refidObject = getProject().getReference( m_refid );

        if( !m_type.isInstance( idObject ) )
        {
            final String message =
                "Object referenced by id is not a " +
                m_type.getName();
            throw new BuildException( message );
        }
        if( !m_type.isInstance( refidObject ) )
        {
            final String message =
                "Object referenced by refid is not a " +
                m_type.getName();
            throw new BuildException( message );
        }

        final PluginSet base = (PluginSet)idObject;
        final PluginSet other = (PluginSet)refidObject;
        base.addPluginSet( other );
    }

    /**
     * Validate correct attributes have been specified.
     */
    void validate()
    {
        if( null == m_id )
        {
            throw new BuildException( "id not specified" );
        }
        if( null == m_refid )
        {
            throw new BuildException( "refid not specified" );
        }
    }
}
