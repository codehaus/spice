/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.tools.tasks;

import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Path;

/**
 * An element used by ant to configure an interceptor.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-08-31 07:25:57 $
 */
public class InterceptorElement
{
    /**
     * The fileset to use
     */
    private Path m_path;

    /**
     * The classname of the interceptor.
     */
    private String m_name;

    /**
     * Add the classpath that interceptor loaded from.
     *
     * @param set the classpath that interceptor loaded from.
     */
    public void addClasspath( final FileSet set )
    {
        if( null == m_path )
        {
            m_path = new Path( set.getProject() );
        }
        m_path.addFileset( set );
    }

    /**
     * Set the classname of the interceptor.
     *
     * @param name the classname of the interceptor.
     */
    public void setName( final String name )
    {
        m_name = name;
    }

    /**
     * Return the path defining fileset to load interceptor from.
     *
     * @return the path defining fileset to load interceptor from.
     */
    public Path getPath()
    {
        return m_path;
    }

    /**
     * Return the classname of the interceptor.
     *
     * @return the classname of the interceptor.
     */
    public String getName()
    {
        return m_name;
    }
}
