/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.jndikit.rmi.server;

import java.io.Serializable;
import java.util.ArrayList;
import javax.naming.Binding;
import javax.naming.CompositeName;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NameClassPair;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import org.realityforge.jndikit.RemoteContext;
import org.realityforge.jndikit.rmi.RMINamingProvider;

/**
 * The RMI implementation of provider.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.2 $
 */
public class RMINamingProviderImpl
    implements Serializable, RMINamingProvider
{
    private Context m_root;

    public RMINamingProviderImpl( final Context root )
    {
        m_root = root;
    }

    public NameParser getNameParser()
        throws NamingException
    {
        return m_root.getNameParser( new CompositeName() );
    }

    public void bind( final Name name, final String className, final Object object )
        throws NamingException
    {
        final Binding binding = new Binding( name.toString(), className, object, true );
        m_root.bind( name, binding );
    }

    public void rebind( final Name name, final String className, final Object object )
        throws NamingException
    {
        final Binding binding = new Binding( name.toString(), className, object, true );
        m_root.rebind( name, binding );
    }

    public Context createSubcontext( final Name name )
        throws NamingException
    {
        m_root.createSubcontext( name );

        final RemoteContext context = new RemoteContext( null, name );
        return context;
    }

    public void destroySubcontext( final Name name )
        throws NamingException
    {
        m_root.destroySubcontext( name );
    }

    public NameClassPair[] list( final Name name )
        throws NamingException
    {
        //Remember that the bindings returned by this
        //actually have a nested Binding as an object
        final NamingEnumeration enum = m_root.listBindings( name );
        final ArrayList pairs = new ArrayList();

        while( enum.hasMore() )
        {
            final Binding binding = (Binding)enum.next();
            final Object object = binding.getObject();

            String className = null;

            //check if it is an entry or a context
            if( object instanceof Binding )
            {
                //must be an entry
                final Binding entry = (Binding)binding.getObject();
                className = entry.getObject().getClass().getName();
            }
            else if( object instanceof Context )
            {
                //must be a context
                className = RemoteContext.class.getName();
            }
            else
            {
                className = object.getClass().getName();
            }

            pairs.add( new NameClassPair( binding.getName(), className ) );
        }

        return (NameClassPair[])pairs.toArray( new NameClassPair[ 0 ] );
    }

    public Binding[] listBindings( final Name name )
        throws NamingException
    {
        //Remember that the bindings returned by this
        //actually have a nested Binding as an object
        final NamingEnumeration enum = m_root.listBindings( name );
        final ArrayList bindings = new ArrayList();

        while( enum.hasMore() )
        {
            final Binding binding = (Binding)enum.next();
            Object object = binding.getObject();
            String className = null;

            //check if it is an entry or a context
            if( object instanceof Binding )
            {
                //must be an entry
                final Binding entry = (Binding)binding.getObject();
                object = entry.getObject();
                className = object.getClass().getName();
            }
            else if( object instanceof Context )
            {
                //must be a context
                className = RemoteContext.class.getName();
                object = new RemoteContext( null, name );
            }
            else
            {
                className = object.getClass().getName();
            }

            final Binding result =
                new Binding( binding.getName(), className, object );
            bindings.add( result );
        }

        return (Binding[])bindings.toArray( new Binding[ 0 ] );
    }

    public Object lookup( final Name name )
        throws NamingException
    {
        Object object = m_root.lookup( name );

        //check if it is an entry or a context
        if( object instanceof Binding )
        {
            object = ( (Binding)object ).getObject();
        }
        else if( object instanceof Context )
        {
            //must be a context
            object = new RemoteContext( null, name.getPrefix( name.size() - 1 ) );
        }

        return object;
    }

    public void unbind( final Name name )
        throws NamingException
    {
        m_root.unbind( name );
    }
}
