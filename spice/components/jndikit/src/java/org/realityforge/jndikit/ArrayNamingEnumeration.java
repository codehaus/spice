/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.jndikit;

import java.util.NoSuchElementException;
import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.NamingException;

/**
 * Class for building NamingEnumerations.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.2 $
 */
final class ArrayNamingEnumeration
    extends AbstractNamingEnumeration
{
    protected Object[] m_items;
    protected int m_index;

    public ArrayNamingEnumeration( final Context owner,
                                   final Namespace namespace,
                                   final Object[] items )
    {
        super( owner, namespace );
        m_items = items;
        //m_index = 0;
    }

    public boolean hasMoreElements()
    {
        return m_index < m_items.length;
    }

    public Object next()
        throws NamingException
    {
        if( !hasMore() )
        {
            throw new NoSuchElementException();
        }

        final Object object = m_items[ m_index++ ];

        if( object instanceof Binding )
        {
            final Binding binding = (Binding)object;
            final Object resolvedObject = resolve( binding.getName(), binding.getObject() );
            binding.setObject( resolvedObject );
        }

        return object;
    }

    public void close()
    {
        super.close();
        m_items = null;
    }
}
