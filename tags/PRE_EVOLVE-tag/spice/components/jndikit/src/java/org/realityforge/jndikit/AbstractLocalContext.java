/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.jndikit;

import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.ContextNotEmptyException;
import javax.naming.InvalidNameException;
import javax.naming.Name;
import javax.naming.NameAlreadyBoundException;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.NotContextException;
import javax.naming.OperationNotSupportedException;
import javax.naming.Reference;
import javax.naming.Referenceable;

/**
 * Abstract local JNDI Context that can be inherited from to
 * provide a particular type of Context. These contexts are assumed to be
 * on the same machine.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.2 $
 */
public abstract class AbstractLocalContext
    extends AbstractContext
{
    private Context m_parent;
    private Namespace m_namespace;

    public AbstractLocalContext( final Namespace namespace,
                                 final Hashtable environment,
                                 final Context parent )
    {
        super( environment );
        m_namespace = namespace;
        m_parent = parent;
    }

    /**
     * Utility method to retrieve parent Context.
     *
     * @return the parent Context if any
     */
    protected final Context getParent()
    {
        return m_parent;
    }

    /**
     * Utility method to retrieve the Namespace.
     *
     * @return the Namespace
     */
    protected final Namespace getNamespace()
    {
        return m_namespace;
    }

    protected boolean isDestroyableContext( final Object object )
        throws NamingException
    {
        return getClass().isInstance( object );
    }

    protected abstract Context newContext()
        throws NamingException;

    protected abstract Context cloneContext()
        throws NamingException;

    /**
     * Helper method to bind
     */
    protected void bind( final Name name, Object object, final boolean rebind )
        throws NamingException
    {
        if( isSelf( name ) )
        {
            throw new InvalidNameException( "Failed to bind self" );
        }

        if( 1 == name.size() )
        {
            boolean alreadyBound;
            try
            {
                localLookup( name );
                alreadyBound = true;
            }
            catch( final NamingException ne )
            {
                alreadyBound = false;
            }

            if( !rebind && alreadyBound )
            {
                throw new NameAlreadyBoundException( name.get( 0 ) );
            }
            else
            {
                //Should this occur here or in the factories ???
                if( object instanceof Referenceable )
                {
                    object = ( (Referenceable)object ).getReference();
                }

                // Call getStateToBind for using any state factories
                final Name atom = name.getPrefix( 1 );
                object = m_namespace.getStateToBind( object, atom, this, getRawEnvironment() );

                doLocalBind( name, object );
            }
        }
        else
        {
            final Context context = lookupSubContext( getPathName( name ) );
            if( rebind )
            {
                context.rebind( getLeafName( name ), object );
            }
            else
            {
                context.bind( getLeafName( name ), object );
            }
        }
    }

    protected abstract void doLocalBind( Name name, Object object )
        throws NamingException;

    public void close()
    {
        m_parent = null;
        m_namespace = null;
    }

    /**
     * Create a Subcontext.
     *
     * @param name the name of subcontext
     * @return the created context
     * @throws NamingException if an error occurs
     *         (ie context exists, badly formated name etc)
     */
    public Context createSubcontext( final Name name )
        throws NamingException
    {
        final Context context = newContext();
        bind( name, context );
        return context;
    }

    public void destroySubcontext( final Name name )
        throws NamingException
    {
        if( isSelf( name ) )
        {
            throw new InvalidNameException( "Failed to destroy self" );
        }

        if( 1 == name.size() )
        {
            Object object = null;
            try
            {
                object = localLookup( name );
            }
            catch( final NamingException ne )
            {
                return;
            }

            checkUnbindContext( name, object );

            doLocalUnbind( name );
        }
        else
        {
            final Context context = lookupSubContext( getPathName( name ) );

            Object object = null;

            final Name atom = getLeafName( name );
            try
            {
                object = context.lookup( atom );
            }
            catch( final NamingException ne )
            {
                return;
            }

            checkUnbindContext( atom, object );

            context.destroySubcontext( atom );
        }
    }

    protected void checkUnbindContext( final Name name, final Object entry )
        throws NamingException
    {
        if( !isDestroyableContext( entry ) )
        {
            throw new NotContextException( name.toString() );
        }

        final Context context = (Context)entry;
        if( context.list( "" ).hasMoreElements() )
        {
            throw new ContextNotEmptyException( name.toString() );
        }
    }

    public String getNameInNamespace()
        throws NamingException
    {
        throw new OperationNotSupportedException( "Namespace has no notion of a 'full name'" );
    }

    protected NameParser getNameParser()
        throws NamingException
    {
        return m_namespace.getNameParser();
    }

    /**
     * Enumerates the names bound in the named context.
     *
     * @param name the name of the context
     * @return the enumeration
     * @throws javax.naming.NamingException if an error occurs
     */
    public NamingEnumeration list( final Name name )
        throws NamingException
    {
        if( isSelf( name ) )
        {
            return doLocalList();
        }
        else
        {
            // Perhaps 'name' names a context
            final Context context = lookupSubContext( name );
            return context.list( "" );
        }
    }

    protected abstract NamingEnumeration doLocalList()
        throws NamingException;

    protected abstract NamingEnumeration doLocalListBindings()
        throws NamingException;

    /**
     * Enumerates the names bound in the named context, along with the objects bound to them.
     *
     * @param name the name of the context
     * @return the enumeration
     * @throws javax.naming.NamingException if an error occurs
     */
    public NamingEnumeration listBindings( final Name name )
        throws NamingException
    {
        if( isSelf( name ) )
        {
            return doLocalListBindings();
        }
        else
        {
            // Perhaps 'name' names a context
            final Context context = lookupSubContext( name );
            return context.listBindings( "" );
        }
    }

    /**
     * Get the object named.
     *
     * @param name the name
     * @return the object
     * @throws NamingException if an error occurs
     *         (ie object name is inavlid or unbound)
     */
    public Object lookup( final Name name )
        throws NamingException
    {
        //if it refers to base context return a copy of it.
        if( isSelf( name ) )
        {
            return cloneContext();
        }

        if( 1 == name.size() )
        {
            Object obj = localLookup( name );
            if( obj instanceof Reference )
            {
                try
                {
                    obj = m_namespace.getObjectInstance( obj, name, this, this.getEnvironment() );
                }
                catch( Exception e )
                {
                    throw new NamingException( "Could not resolve reference" );
                }
            }

            return obj;
        }
        else
        {
            final Context context = lookupSubContext( getPathName( name ) );
            return context.lookup( getLeafName( name ) );
        }
    }

    /**
     * Lookup entry in local context.
     *
     * @param name the name in local context (size() == 1)
     * @return the bound object
     * @throws javax.naming.NamingException if an error occurs
     */
    protected Object localLookup( final Name name )
        throws NamingException
    {
        final Object value = doLocalLookup( name );

        // Call getObjectInstance for using any object factories
        try
        {
            final Name atom = name.getPrefix( 1 );
            return m_namespace.getObjectInstance( value, atom, this, getRawEnvironment() );
        }
        catch( final Exception e )
        {
            final NamingException ne = new NamingException( "getObjectInstance failed" );
            ne.setRootCause( e );
            throw ne;
        }
    }

    /**
     * Actually lookup raw entry in local context.
     * When overidding this it is not neccesary to resolve references etc.
     *
     * @param name the name in local context (size() == 1)
     * @return the bound object
     * @throws javax.naming.NamingException if an error occurs
     */
    protected abstract Object doLocalLookup( Name name )
        throws NamingException;

    /**
     * Lookup a sub-context of current context.
     * Note that name must have 1 or more elements.
     *
     * @param name the name of subcontext
     * @return the sub-Context
     * @throws javax.naming.NamingException if an error occurs (like named entry is not a Context)
     */
    protected Context lookupSubContext( final Name name )
        throws NamingException
    {
        final Name atom = name.getPrefix( 1 );
        Object object = localLookup( atom );

        if( 1 != name.size() )
        {
            if( !( object instanceof Context ) )
            {
                throw new NotContextException( atom.toString() );
            }

            object = ( (Context)object ).lookup( name.getSuffix( 1 ) );
        }

        if( !( object instanceof Context ) )
        {
            throw new NotContextException( name.toString() );
        }

        //((Context)object).close();
        return (Context)object;
    }

    /**
     * Unbind a object from a name.
     *
     * @param name the name
     * @throws javax.naming.NamingException if an error occurs
     */
    public void unbind( final Name name )
        throws NamingException
    {
        if( isSelf( name ) )
        {
            throw new InvalidNameException( "Cannot unbind self" );
        }
        else if( 1 == name.size() )
        {
            doLocalUnbind( name );
        }
        else
        {
            final Context context = lookupSubContext( getPathName( name ) );
            context.unbind( getLeafName( name ) );
        }
    }

    /**
     * Actually unbind raw entry in local context.
     *
     * @param name the name in local context (size() == 1)
     * @throws javax.naming.NamingException if an error occurs
     */
    protected abstract void doLocalUnbind( Name name )
        throws NamingException;
}
