/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.jndikit.memory;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.NoSuchElementException;
import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.NameClassPair;
import javax.naming.NamingException;
import org.realityforge.jndikit.AbstractNamingEnumeration;
import org.realityforge.jndikit.Namespace;

/**
 * Class for building NamingEnumerations.
 *
 * @author <a href="mailto:peter@apache.org">Peter Donald</a>
 * @version $Revision: 1.1 $
 */
final class MemoryNamingEnumeration
    extends AbstractNamingEnumeration
{
    protected Hashtable m_bindings;
    protected Iterator m_names;
    protected boolean m_returnBindings;

    public MemoryNamingEnumeration( final Context owner,
                                    final Namespace namespace,
                                    final Hashtable bindings,
                                    final boolean returnBindings )
    {
        super( owner, namespace );
        m_returnBindings = returnBindings;
        m_bindings = bindings;
        m_names = m_bindings.keySet().iterator();
    }

    public boolean hasMoreElements()
    {
        return m_names.hasNext();
    }

    public Object next()
        throws NamingException
    {
        if( !hasMore() )
        {
            throw new NoSuchElementException();
        }

        final String name = (String)m_names.next();
        Object object = m_bindings.get( name );

        if( !m_returnBindings )
        {
            return new NameClassPair( name, object.getClass().getName() );
        }
        else
        {
            return new Binding( name, resolve( name, object ) );
        }
    }

    public void close()
    {
        super.close();
        m_bindings = null;
    }
}
