/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.jndikit.rmi;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.rmi.MarshalledObject;
import java.util.Hashtable;
import javax.naming.ConfigurationException;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.ServiceUnavailableException;
import javax.naming.Name;
import javax.naming.spi.InitialContextFactory;
import org.realityforge.jndikit.DefaultNamespace;
import org.realityforge.jndikit.Namespace;
import org.realityforge.jndikit.NamingProvider;
import org.realityforge.jndikit.RemoteContext;

/**
 * Initial context factory for memorycontext.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.2 $
 */
public class RMIInitialContextFactory
    implements InitialContextFactory
{
    public Context getInitialContext( final Hashtable environment )
        throws NamingException
    {
        final NamingProvider provider = newNamingProvider( environment );
        environment.put( RemoteContext.NAMING_PROVIDER, provider );

        final Namespace namespace = newNamespace( environment );
        environment.put( RemoteContext.NAMESPACE, namespace );

        final Name baseName = namespace.getNameParser().parse( "" );
        return new RemoteContext( environment, baseName );
    }

    protected NamingProvider newNamingProvider( final Hashtable environment )
        throws NamingException
    {
        final String url = (String)environment.get( Context.PROVIDER_URL );
        if( null == url )
        {
            return newNamingProvider( "localhost", 1977 );
        }
        else
        {
            if( !url.startsWith( "rmi://" ) )
            {
                throw new ConfigurationException( "Malformed url - " + url );
            }

            final int index = url.indexOf( ':', 6 );
            int end = index;

            int port = 1977;

            if( -1 == index )
            {
                end = url.length();
            }
            else
            {
                port = Integer.parseInt( url.substring( index + 1 ) );
            }

            final String host = url.substring( 6, end );

            return newNamingProvider( host, port );
        }
    }

    protected NamingProvider newNamingProvider( final String host,
                                                final int port )
        throws NamingException
    {
        Socket socket = null;

        try
        {
            socket = new Socket( host, port );

            final BufferedInputStream buffered =
                new BufferedInputStream( socket.getInputStream() );
            final ObjectInputStream input = new ObjectInputStream( buffered );

            final MarshalledObject object =
                (MarshalledObject)input.readObject();
            final NamingProvider provider =(NamingProvider)object.get();

            socket.close();
            socket = null;

            return provider;
        }
        catch( final Exception e )
        {
            final ServiceUnavailableException sue =
                new ServiceUnavailableException( e.getMessage() );
            sue.setRootCause( e );
            throw sue;
        }
        finally
        {
            if( null != socket )
            {
                try
                {
                    socket.close();
                }
                catch( final IOException ioe )
                {
                    //Ignored.
                }
            }
        }
    }

    protected Namespace newNamespace( final Hashtable environment )
        throws NamingException
    {
        try
        {
            final NamingProvider provider =
                (NamingProvider)environment.get( RemoteContext.NAMING_PROVIDER );

            return new DefaultNamespace( provider.getNameParser() );
        }
        catch( final Exception e )
        {
            if( e instanceof NamingException )
            {
                throw (NamingException)e;
            }
            else
            {
                final ServiceUnavailableException sue =
                    new ServiceUnavailableException( e.getMessage() );
                sue.setRootCause( e );
                throw sue;
            }
        }
    }
}

