/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass;

import org.realityforge.metaclass.io.MetaClassAccessor;
import org.realityforge.metaclass.model.ClassDescriptor;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.2 $ $Date: 2003-09-28 03:58:01 $
 */
class MockAccessor
    implements MetaClassAccessor
{
    private final ClassDescriptor m_classDescriptor;
    private int m_accessCount;

    public MockAccessor( final ClassDescriptor classDescriptor )
    {
        m_classDescriptor = classDescriptor;
    }

    public ClassDescriptor getClassDescriptor( String classname,
                                               ClassLoader classLoader )
        throws MetaClassException
    {
        m_accessCount++;
        if( null != m_classDescriptor && classname.equals( m_classDescriptor.getName() ) )
        {
            return m_classDescriptor;
        }
        else
        {
            throw new MetaClassException( "Missing " + classname );
        }
    }

    public int getAccessCount()
    {
        return m_accessCount;
    }
}
