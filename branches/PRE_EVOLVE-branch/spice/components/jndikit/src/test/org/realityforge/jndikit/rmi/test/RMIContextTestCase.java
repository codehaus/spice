/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.jndikit.rmi.test;

import java.util.Hashtable;
import javax.naming.Context;
import org.realityforge.jndikit.rmi.RMIInitialContextFactory;
import org.realityforge.jndikit.rmi.server.Main;
import org.realityforge.jndikit.test.AbstractContextTestCase;

/**
 * Unit test for RMI context
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.6 $
 */
public class RMIContextTestCase
    extends AbstractContextTestCase
{
    private static int c_id = 0;
    private static int c_instanceCount = 0;
    private static Main c_server = new Main( true, 1977 );
    private static Thread c_serverThread;
    private static boolean c_setUp = false;

    public RMIContextTestCase( String name )
    {
        super( name );
        c_instanceCount++;
    }

    public void setUp() throws Exception
    {
        startServer();

        final RMIInitialContextFactory factory = new RMIInitialContextFactory();
        final Context root = factory.getInitialContext( new Hashtable() );
        setRoot( root );

        setContext( root.createSubcontext( "test" + c_id++ ) );
    }

    public void tearDown() throws Exception
    {
        super.tearDown();
        stopServer();
    }

    private void startServer() throws Exception
    {
        if( !c_setUp )
        {
            c_server.start();

            c_serverThread = new Thread( c_server );
            c_serverThread.start();
            c_setUp = true;
        }
    }

    private void stopServer() throws Exception
    {
        if( c_id >= c_instanceCount )
        {
            c_server.stop();
            c_server.dispose();
            c_serverThread.interrupt();
        }
    }
}
