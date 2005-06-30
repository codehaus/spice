/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.jndikit.rmi.test;

import java.util.Hashtable;
import java.util.Random;
import javax.naming.Context;
import javax.naming.spi.InitialContextFactory;

import org.codehaus.spice.jndikit.rmi.RMIInitialContextFactory;
import org.codehaus.spice.jndikit.rmi.server.Main;

/**
 * Helper for setting up and tearing down the RMI naming provider.
 *
 * @author Peter Donald
 * @author Tim Anderson
 * @version $Revision: 1.1 $ $Date: 2005-06-30 04:22:16 $
 */
public class RMITestSetup
{

    private Main m_server;
    private Thread m_serverThread;
    private static final Random RANDOM = new Random();
    private int m_port;
    private final InitialContextFactory m_factory;

    public RMITestSetup()
    {
        this( new RMIInitialContextFactory() );
    }

    public RMITestSetup( RMIInitialContextFactory factory )
    {
        m_factory = factory;

    }

    public void setUp() throws Exception
    {
        m_port = 1500 + Math.abs( RANDOM.nextInt() % 1000 );

        startServer();
    }

    public Context getRoot() throws Exception
    {
        final Hashtable environment = new Hashtable();
        environment.put( Context.PROVIDER_URL, "rmi://localhost:" + m_port );
        final Context root = m_factory.getInitialContext( environment );
        return root;
    }

    public void tearDown()
        throws Exception
    {
        try
        {
            stopServer();
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }
    }

    private void startServer()
        throws Exception
    {
        m_server = new Main( true, m_port );
        m_server.start();

        m_serverThread = new Thread( m_server );
        m_serverThread.start();
        while( !m_server.isRunning() )
        {
            Thread.yield();
        }
    }

    private void stopServer()
        throws Exception
    {
        m_server.stop();
        m_server.dispose();
        m_serverThread.interrupt();
    }


}
