/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.tools.tasks;

import com.thoughtworks.qdox.model.JavaClass;

/**
 * Multicast filter for passing a JavaClass through multiple filters.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-08-31 08:09:49 $
 */
class MulticastJavaClassFilter
    implements JavaClassFilter
{
    /**
     * The filters to iterate over.
     */
    private final JavaClassFilter[] m_filters;

    /**
     * Create a multicast filter for specified filters.
     *
     * @param filters the filters
     */
    public MulticastJavaClassFilter( final JavaClassFilter[] filters )
    {
        if( null == filters )
        {
            throw new NullPointerException( "filters" );
        }
        for( int i = 0; i < filters.length; i++ )
        {
            if( null == filters[ i ] )
            {
                throw new NullPointerException( "filters[" + i + "]" );
            }
        }
        m_filters = filters;
    }

    /**
     * This method provides an access point for subclasses to use custom filters
     * on the list of classes parsed, i.e. to return null if the class has been filtered.
     *
     * @param javaClass the JavaClass
     * @return javaClass or null
     */
    public JavaClass filterClass( JavaClass javaClass )
    {
        for( int i = 0; i < m_filters.length; i++ )
        {
            javaClass = m_filters[ i ].filterClass( javaClass );
            if( null == javaClass )
            {
                return null;
            }
        }
        return javaClass;
    }
}
