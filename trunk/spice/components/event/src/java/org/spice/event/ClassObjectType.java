/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.spice.event;

import org.drools.spi.ObjectType;

/**
 * @author <a href="mailto:peter.royal@pobox.com">peter royal</a>
 */
public class ClassObjectType implements ObjectType
{
    private final Class m_clazz;

    public ClassObjectType( final Class clazz )
    {
        if( null == clazz )
        {
            throw new NullPointerException( "clazz" );
        }

        m_clazz = clazz;
    }

    public Class getType()
    {
        return m_clazz;
    }

    public boolean matches( final Object object )
    {
        return getType().isInstance( object );
    }

    public boolean equals( final Object o )
    {
        if( this == o ) return true;
        if( !( o instanceof ClassObjectType ) ) return false;

        final ClassObjectType classObjectType = (ClassObjectType)o;

        if( !getType().equals( classObjectType.getType() ) ) return false;

        return true;
    }

    public int hashCode()
    {
        return m_clazz.hashCode();
    }
}