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
import org.codehaus.spice.jndikit.rmi.RMIInitialContextFactory;
import org.codehaus.spice.jndikit.rmi.server.Main;
import org.codehaus.spice.jndikit.test.AbstractContextTestCase;

/**
 * Unit test for RMI context
 *
 * @author Peter Donald
 * @version $Revision: 1.1 $
 */
public class RMIContextTestCase
    extends AbstractContextTestCase
{
    private static int c_id = 0;
    private Main m_server;
    private Thread m_serverThread;
    private static final Random RANDOM = new Random();
    private int m_port;

    public void setUp()
        throws Exception
    {
        m_port = 1500 + Math.abs( RANDOM.nextInt() % 1000 );

        startServer();

        final RMIInitialContextFactory factory = new RMIInitialContextFactory();
        final Hashtable environment = new Hashtable();
        environment.put( Context.PROVIDER_URL, "rmi://localhost:" + m_port );
        final Context root = factory.getInitialContext( environment );
        setRoot( root );

        setContext( root.createSubcontext( "test" + c_id++ ) );
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
