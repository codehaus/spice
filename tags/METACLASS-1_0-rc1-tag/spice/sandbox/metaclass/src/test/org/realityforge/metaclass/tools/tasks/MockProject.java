/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.tools.tasks;

import java.util.HashMap;
import org.apache.tools.ant.Project;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.2 $ $Date: 2003-11-20 10:00:39 $
 */
class MockProject
    extends Project
{
    private final HashMap m_references = new HashMap();

    void bindReference( final String key,
                        final Object value )
    {
        m_references.put( key, value );
    }

    public Object getReference( final String key )
    {
        return m_references.get( key );
    }
}
