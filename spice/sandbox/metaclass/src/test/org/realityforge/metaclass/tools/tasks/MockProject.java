/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.tools.tasks;

import org.apache.tools.ant.Project;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-11-20 06:42:49 $
 */
class MockProject
    extends Project
{
    private final Object m_reference;

    MockProject( final Object reference )
    {
        m_reference = reference;
    }

    public Object getReference( final String key )
    {
        return m_reference;
    }
}
