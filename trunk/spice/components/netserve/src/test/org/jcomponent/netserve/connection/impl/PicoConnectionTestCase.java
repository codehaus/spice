/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.netserve.connection.impl;

import org.jcomponent.netserve.connection.ConnectionManager;
import org.jcomponent.threadpool.ThreadPool;

/**
 * TestCase for {@link PicoConnectionManager}.
 *
 * @author <a href="mailto:mauro.talevi at aquilonia.org">Mauro Talevi</a>
 * @version $Revision: 1.1 $ $Date: 2003-08-31 10:38:23 $
 */
public class PicoConnectionTestCase
    extends AbstractConnectionTestCase
{
    public PicoConnectionTestCase( final String name )
    {
        super( name );
    }

    protected void setUp() throws Exception
    {
        setMonitor( createConnectionMonitor() );
    }

    protected ConnectionManager createConnectionManager( boolean addThreadPool, 
                                                          final int soTimeout, 
                                                          final boolean forceShutdown, 
                                                          final int shutdownTimeout ) 
        throws Exception
    {

        ThreadPool threadPool = null;
        if ( addThreadPool )
        {
            threadPool = new TestThreadPool();
        } 
        final ConnectionManager cm = new PicoConnectionManager.WithMonitorAndConfig( 
                    createConnectionMonitor(), 
                    threadPool, 
                    soTimeout, 
                    forceShutdown, 
                    shutdownTimeout );
        return cm;
    }

    protected ConnectionMonitor createConnectionMonitor()
    {
        final ConnectionMonitor monitor = new ConsoleConnectionMonitor();
        return monitor;        
    }
    protected ConnectionMonitor createConnectionMonitorNoLogging()
    {
        final ConnectionMonitor monitor = new ConsoleConnectionMonitor( false );
        return monitor;        
    }

    protected void disposeConnectionManager( final ConnectionManager cm )
    {
        final PicoConnectionManager pico = (PicoConnectionManager)cm;
        pico.dispose();
    }
}
