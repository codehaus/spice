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
import org.realityforge.metaclass.model.PackageDescriptor;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-08-23 04:34:29 $
 */
class MockAccessor
    implements MetaClassAccessor
{
    private final ClassDescriptor m_classDescriptor;
    private final PackageDescriptor m_packageDescriptor;
    private int m_accessCount;

    public MockAccessor( final ClassDescriptor classDescriptor,
                         final PackageDescriptor packageDescriptor )
    {
        m_classDescriptor = classDescriptor;
        m_packageDescriptor = packageDescriptor;
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

    public PackageDescriptor getPackageDescriptor( String name,
                                                   ClassLoader classLoader )
        throws MetaClassException
    {
        m_accessCount++;
        if( null != m_packageDescriptor && name.equals( m_packageDescriptor.getName() ) )
        {
            return m_packageDescriptor;
        }
        else
        {
            throw new MetaClassException( "Missing " + name );
        }
    }

    public int getAccessCount()
    {
        return m_accessCount;
    }
}
