/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.jmx;

import org.realityforge.metaclass.introspector.MetaClassAccessor;
import org.realityforge.metaclass.introspector.MetaClassException;
import org.realityforge.metaclass.model.ClassDescriptor;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.2 $ $Date: 2003-10-13 23:57:09 $
 */
class MockAccessor
    implements MetaClassAccessor
{
    private final ClassDescriptor m_classDescriptor;

    MockAccessor( final ClassDescriptor classDescriptor )
    {
        m_classDescriptor = classDescriptor;
    }

    public ClassDescriptor getClassDescriptor( String classname,
                                               ClassLoader classLoader )
        throws MetaClassException
    {
        if( null != m_classDescriptor )
        {
            return m_classDescriptor;
        }
        else
        {
            throw new MetaClassException( "Missing " + classname );
        }
    }
}
