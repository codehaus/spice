/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.jndikit;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.MarshalledObject;
import java.util.Hashtable;
import java.util.Iterator;
import javax.naming.Binding;
import javax.naming.CommunicationException;
import javax.naming.ConfigurationException;
import javax.naming.Context;
import javax.naming.InvalidNameException;
import javax.naming.Name;
import javax.naming.NameClassPair;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.Reference;
import javax.naming.Referenceable;

/**
 * Context that hooks up to a remote source.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.3 $
 */
public class RemoteContext
    extends AbstractContext
    implements Serializable
{
    public static final String NAMESPACE_NAME = "org.realityforge.jndikit.Namespace/NAME";
    public static final String NAMESPACE = "org.realityforge.jndikit.Namespace";
    public static final String NAMING_PROVIDER = "org.realityforge.jndikit.NamingProvider";

    private transient NamingProvider m_provider;
    private transient NameParser m_nameParser;
    private transient Namespace m_namespace;

    private Name m_baseName;

    //for deserialisation
    public RemoteContext()
    {
    }

    public RemoteContext( final Hashtable environment, final Name baseName )
        throws NamingException
    {
        super( environment );
        m_baseName = baseName;
    }

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

        String className = null;

        object = getNamespace().getStateToBind( object, name, this, getRawEnvironment() );

        if( object instanceof Reference )
        {
            className = ( (Reference)object ).getClassName();
        }
        else if( object instanceof Referenceable )
        {
            object = ( (Referenceable)object ).getReference();
            className = ( (Reference)object ).getClassName();
        }
        else
        {
            className = object.getClass().getName();

            try
            {
                object = new MarshalledObject( object );
            }
            catch( final IOException ioe )
            {
                throw new NamingException( "Only Reference, Referenceables and " +
                                           "Serializable objects can be bound " +
                                           "to context" );
            }
        }

        try
        {
            if( rebind )
            {
                getProvider().rebind( getAbsoluteName( name ), className, object );
            }
            else
            {
                getProvider().bind( getAbsoluteName( name ), className, object );
            }
        }
        catch( final Exception e )
        {
            throw handleException( e );
        }
    }

    /**
     * Release resources associated with context.
     */
    public void close()
    {
        super.close();
        m_namespace = null;
        m_provider = null;
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
        if( isSelf( name ) )
        {
            throw new InvalidNameException( "Failed to create null subcontext" );
        }

        Context result = null;
        try
        {
            result = getProvider().createSubcontext( getAbsoluteName( name ) );
        }
        catch( final Exception e )
        {
            throw handleException( e );
        }

        fillInContext( result );

        return result;
    }

    public void destroySubcontext( final Name name )
        throws NamingException
    {
        if( isSelf( name ) )
        {
            throw new InvalidNameException( "Failed to destroy self" );
        }

        try
        {
            getProvider().destroySubcontext( getAbsoluteName( name ) );
        }
        catch( final Exception e )
        {
            throw handleException( e );
        }
    }

    public String getNameInNamespace()
        throws NamingException
    {
        return getAbsoluteName( getNameParser().parse( "" ) ).toString();
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
        try
        {
            final NameClassPair[] result = getProvider().list( getAbsoluteName( name ) );
            return new ArrayNamingEnumeration( this, getNamespace(), result );
        }
        catch( final Exception e )
        {
            throw handleException( e );
        }
    }

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
        try
        {
            final Binding[] result = getProvider().listBindings( getAbsoluteName( name ) );

            for( int i = 0; i < result.length; i++ )
            {
                final Object object = result[ i ].getObject();
                if( object instanceof Context )
                {
                    fillInContext( (Context)object );
                }
            }

            return new ArrayNamingEnumeration( this, getNamespace(), result );
        }
        catch( final Exception e )
        {
            throw handleException( e );
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
        if( isSelf( name ) )
        {
            return new RemoteContext( getRawEnvironment(), m_baseName );
        }

        //actually do a real-lookup
        Object object = null;
        try
        {
            object = getProvider().lookup( getAbsoluteName( name ) );

            if( object instanceof MarshalledObject )
            {
                object = ( (MarshalledObject)object ).get();
            }

            object = getNamespace().getObjectInstance( object, name, this, getRawEnvironment() );

            if( object instanceof Context )
            {
                fillInContext( (Context)object );
            }
        }
        catch( final Exception e )
        {
            throw handleException( e );
        }

        return object;
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
            throw new InvalidNameException( "Failed to unbind self" );
        }

        try
        {
            getProvider().unbind( getAbsoluteName( name ) );
        }
        catch( final Exception e )
        {
            throw handleException( e );
        }
    }

    protected void fillInContext( final Context object )
        throws NamingException
    {
        final Hashtable environment = getRawEnvironment();
        final Iterator keys = environment.keySet().iterator();

        while( keys.hasNext() )
        {
            final String key = (String)keys.next();
            final Object value = environment.get( key );
            object.addToEnvironment( key, value );
        }
    }

    protected Namespace getNamespace()
        throws NamingException
    {
        if( null == m_namespace )
        {
            final Object object = getRawEnvironment().get( RemoteContext.NAMESPACE );

            if( !( object instanceof Namespace ) || null == object )
            {
                throw new ConfigurationException( "Context does not contain Namespace" );
            }
            else
            {
                m_namespace = (Namespace)object;
            }
        }

        return m_namespace;
    }

    protected NamingProvider getProvider()
        throws NamingException
    {
        if( null == m_provider )
        {
            final Object object = getRawEnvironment().get( RemoteContext.NAMING_PROVIDER );

            if( !( object instanceof NamingProvider ) || null == object )
            {
                throw new ConfigurationException( "Context does not contain provider" );
            }
            else
            {
                m_provider = (NamingProvider)object;
            }
        }

        return m_provider;
    }

    protected NameParser getNameParser()
        throws NamingException
    {
        if( null == m_nameParser )
        {
            //Make sure provider is valid and returns nameparser
            try
            {
                m_nameParser = getProvider().getNameParser();
            }
            catch( final Exception e )
            {
                throw handleException( e );
            }

        }
        return m_nameParser;
    }

    protected Name getAbsoluteName( final Name name )
        throws NamingException
    {
        return composeName( name, m_baseName );
    }

    protected NamingException handleException( final Exception e )
    {
        if( e instanceof NamingException )
        {
            return (NamingException)e;
        }
        else
        {
            return new CommunicationException( e.toString() );
        }
    }
}
