/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.netserve.sockets.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import junit.framework.TestCase;
import org.jcomponent.netserve.sockets.ServerSocketFactory;
import org.jcomponent.netserve.sockets.SocketFactory;

/**
 * TestCase for {@link org.jcomponent.netserve.sockets.SocketFactory} and {@link org.jcomponent.netserve.sockets.ServerSocketFactory}.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.2 $ $Date: 2003-10-24 08:07:21 $
 */
public class SocketTestCase
    extends TestCase
{
    private static final int PORT = 1977;
    private static final InetAddress HOST = getLocalHost();
    private static final byte DATA = 23;

    public SocketTestCase( final String name )
    {
        super( name );
    }

    public void testDefault()
        throws Exception
    {
        final SocketFactory csf = new DefaultSocketFactory();
        final ServerSocketFactory ssf = new DefaultServerSocketFactory();

        doSocketPairTest( csf, ssf );
    }

    private void doSocketPairTest( final SocketFactory csf, final ServerSocketFactory ssf ) throws IOException
    {
        try
        {
            csf.createSocket( HOST, PORT );
            final String message = "Should not be able to create " +
                "client socket when server socket not initialized.";
            fail( message );
        }
        catch( IOException e )
        {
        }

        final ServerSocket ss1 = ssf.createServerSocket( PORT );
        readWriteTestsOnSingleSocket( ss1, csf );

        final ServerSocket ss2 = ssf.createServerSocket( PORT, 1 );
        readWriteTestsOnSingleSocket( ss2, csf );

        final ServerSocket ss3 = ssf.createServerSocket( PORT, 1, HOST );
        readWriteTestsOnSingleSocket( ss3, csf );
    }

    private void readWriteTestsOnSingleSocket( final ServerSocket serverSocket,
                                               final SocketFactory csf )
        throws IOException
    {
        final Socket client = csf.createSocket( HOST, PORT );
        final Socket server = serverSocket.accept();

        pushPullData( "Testing single socket conn", client, server );

        final Socket client2 = csf.createSocket( HOST, PORT, HOST, 0 );
        final Socket server2 = serverSocket.accept();
        pushPullData( "Testing single socket conn2", client2, server2 );

        serverSocket.close();
    }

    private void pushPullData( final String prefix,
                               final Socket client,
                               final Socket server )
        throws IOException
    {
        pushData( prefix + ": c2s", client, server );
        pushData( prefix + ": s2c", server, client );

        client.close();
        server.close();
    }

    /**
     * Push data from socket1 to socket2.
     */
    private void pushData( final String prefix,
                           final Socket socket1,
                           final Socket socket2 ) throws IOException
    {
        final OutputStream cos = socket1.getOutputStream();
        cos.write( DATA );
        cos.flush();

        final InputStream sis = socket2.getInputStream();
        assertEquals( prefix + ": Write/read byte", DATA, sis.read() );
    }

    private static InetAddress getLocalHost()
    {
        try
        {
            return InetAddress.getLocalHost();
        }
        catch( UnknownHostException e )
        {
            throw new IllegalStateException( e.toString() );
        }
    }
}
