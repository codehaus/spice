/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.jndikit;

import java.util.Hashtable;
import javax.naming.CompositeName;
import javax.naming.Context;
import javax.naming.InvalidNameException;
import javax.naming.Name;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.spi.ResolveResult;

/**
 * Abstract JNDI Context that can be inherited from to
 * provide a particular type of Context.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.2 $
 */
public abstract class AbstractURLContext
    extends AbstractContext
    implements NameParser
{
    private final String m_scheme;

    public AbstractURLContext( final String scheme, final Hashtable environment )
    {
        super( environment );
        m_scheme = scheme;
    }

    public Name parse( final String name )
        throws NamingException
    {
        return ( new CompositeName().add( name ) );
    }

    protected NameParser getNameParser()
        throws NamingException
    {
        return this;
    }

    /**
     * Helper method to bind
     */
    protected void bind( final Name name, final Object object, final boolean rebind )
        throws NamingException
    {
        final ResolveResult resolveResult = getBaseURLContext( name, getRawEnvironment() );
        final Context context = (Context)resolveResult.getResolvedObj();

        try
        {
            if( rebind )
            {
                context.rebind( resolveResult.getRemainingName(), object );
            }
            else
            {
                context.bind( resolveResult.getRemainingName(), object );
            }
        }
        finally
        {
            context.close();
        }
    }

    /**
     * Create a Subcontext.
     *
     * @param name the name of subcontext
     * @return the created context
     * @throws NamingException if an error occurs (ie context
     *            exists, badly formated name etc)
     */
    public Context createSubcontext( final Name name )
        throws NamingException
    {
        final ResolveResult resolveResult = getBaseURLContext( name, getRawEnvironment() );
        final Context context = (Context)resolveResult.getResolvedObj();

        try
        {
            return context.createSubcontext( resolveResult.getRemainingName() );
        }
        finally
        {
            context.close();
        }
    }

    public void destroySubcontext( final Name name )
        throws NamingException
    {
        final ResolveResult resolveResult = getBaseURLContext( name, getRawEnvironment() );
        final Context context = (Context)resolveResult.getResolvedObj();

        try
        {
            context.destroySubcontext( resolveResult.getRemainingName() );
        }
        finally
        {
            context.close();
        }
    }

    public String getNameInNamespace()
        throws NamingException
    {
        return "";
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
        final ResolveResult resolveResult = getBaseURLContext( name, getRawEnvironment() );
        final Context context = (Context)resolveResult.getResolvedObj();

        try
        {
            return context.list( resolveResult.getRemainingName() );
        }
        finally
        {
            context.close();
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
        final ResolveResult resolveResult = getBaseURLContext( name, getRawEnvironment() );
        final Context context = (Context)resolveResult.getResolvedObj();

        try
        {
            return context.listBindings( resolveResult.getRemainingName() );
        }
        finally
        {
            context.close();
        }
    }

    /**
     * Get the object named.
     *
     * @param name the name
     * @return the object
     * @throws NamingException if an error occurs (ie object
     *            name is inavlid or unbound)
     */
    public Object lookup( final Name name )
        throws NamingException
    {
        final ResolveResult resolveResult = getBaseURLContext( name, getRawEnvironment() );
        final Context context = (Context)resolveResult.getResolvedObj();

        try
        {
            return context.lookup( resolveResult.getRemainingName() );
        }
        finally
        {
            context.close();
        }
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
        final ResolveResult resolveResult = getBaseURLContext( name, getRawEnvironment() );
        final Context context = (Context)resolveResult.getResolvedObj();

        try
        {
            context.unbind( resolveResult.getRemainingName() );
        }
        finally
        {
            context.close();
        }
    }

    protected ResolveResult getBaseURLContext( final Name name, final Hashtable environment )
        throws NamingException
    {
        if( name.isEmpty() )
        {
            throw new InvalidNameException( "Unable to locate URLContext will empty name" );
        }

        final String nameString = name.toString();
        int index = nameString.indexOf( ':' );

        if( -1 == index )
        {
            final String explanation =
                "Unable to build URLContext as it does not specify scheme";
            throw new InvalidNameException( explanation );
        }

        final String scheme = nameString.substring( 0, index );
        final int end = getEndIndexOfURLPart( nameString, index + 1 );
        final String urlPart = nameString.substring( index + 1, end );
        final String namePart = nameString.substring( end );

        if( !m_scheme.equals( scheme ) )
        {
            final String explanation =
                "Bad Scheme use to build URLContext (" +
                scheme + "). " + "Expected " + m_scheme;
            throw new InvalidNameException( explanation );
        }

        final Context context = newContext( urlPart );

        return new ResolveResult( context, new CompositeName( namePart ) );
    }

    /**
     * Find end index of url part in string.
     * Default implementation looks for
     * //.../[name-part]
     * ///[name-part]
     * //... (no name part)
     * [name-part]
     *
     * @param name the name
     * @param index the index where "scheme:" ends
     * @return the index where url ends
     * @throws javax.naming.NamingException if an error occurs
     */
    protected int getEndIndexOfURLPart( final String name, final int index )
        throws NamingException
    {
        int result = 0;

        //does it start with //
        if( name.startsWith( "//", index ) )
        {
            //does it have .../  following ???
            int end = name.indexOf( "/", index + 2 );

            if( -1 != end )
            {
                result = end;
            }
            else
            {
                result = name.length();
            }
        }

        return result;
    }

    /**
     * Return a new instance of the base context for a URL.
     * This must be implemented in particular URLContext.
     *
     * @param urlPart the part of url string not including "scheme:"
     * @return a base URLContext for urlPart
     * @throws javax.naming.NamingException if an error occurs
     */
    protected abstract Context newContext( String urlPart )
        throws NamingException;
}
