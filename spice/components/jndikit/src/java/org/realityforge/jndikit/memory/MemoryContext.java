/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.jndikit.memory;

import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NameNotFoundException;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import org.realityforge.jndikit.AbstractLocalContext;
import org.realityforge.jndikit.Namespace;

/**
 * An in memory context implementation.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.2 $
 */
public class MemoryContext
    extends AbstractLocalContext
{
    private Hashtable m_bindings;

    protected MemoryContext( final Namespace namespace,
                             final Hashtable environment,
                             final Context parent,
                             final Hashtable bindings )
    {
        super( namespace, environment, parent );
        m_bindings = bindings;
    }

    public MemoryContext( final Namespace namespace,
                          final Hashtable environment,
                          final Context parent )
    {
        this( namespace, environment, parent, new Hashtable( 11 ) );
    }

    protected Context newContext()
        throws NamingException
    {
        return new MemoryContext( getNamespace(), getRawEnvironment(), getParent() );
    }

    protected Context cloneContext()
        throws NamingException
    {
        return new MemoryContext( getNamespace(), getRawEnvironment(), getParent(), m_bindings );
    }

    protected void doLocalBind( final Name name, final Object object )
        throws NamingException
    {
        m_bindings.put( name.get( 0 ), object );
    }

    protected NamingEnumeration doLocalList()
        throws NamingException
    {
        return new MemoryNamingEnumeration( this, getNamespace(), m_bindings, false );
    }

    protected NamingEnumeration doLocalListBindings()
        throws NamingException
    {
        return new MemoryNamingEnumeration( this, getNamespace(), m_bindings, true );
    }

    /**
     * Actually lookup raw entry in local context.
     * When overidding this it is not neccesary to resolve references etc.
     *
     * @param name the name in local context (size() == 1)
     * @return the bound object
     * @throws javax.naming.NamingException if an error occurs
     */
    protected Object doLocalLookup( final Name name )
        throws NamingException
    {
        final Object object = m_bindings.get( name.get( 0 ) );
        if( null == object )
        {
            throw new NameNotFoundException( name.get( 0 ) );
        }
        return object;
    }

    /**
     * Actually unbind raw entry in local context.
     *
     * @param name the name in local context (size() == 1)
     * @throws javax.naming.NamingException if an error occurs
     */
    protected void doLocalUnbind( final Name name )
        throws NamingException
    {
        m_bindings.remove( name.get( 0 ) );
    }
}

