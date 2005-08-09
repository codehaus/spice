/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.jervlet.impl.pico;

import org.codehaus.spice.jervlet.ContextMonitor;
import org.codehaus.spice.jervlet.ListenerMonitor;
import org.codehaus.spice.jervlet.containers.jetty.JettyServer;
import org.codehaus.spice.jervlet.impl.NoopContextMonitor;
import org.codehaus.spice.jervlet.impl.NoopListenerMonitor;
import org.mortbay.jetty.Server;
import org.picocontainer.Startable;

/**
 * TODO must be a better way to integrate into pico lifecycle just for this one component
 *
 * @author <a href="mailto:peter.royal@pobox.com">peter royal</a>
 */
public class PicoJettyServer extends JettyServer implements Startable
{
    public PicoJettyServer()
    {
        super( new Server(), new NoopListenerMonitor(), new NoopContextMonitor() );
    }

    public PicoJettyServer( final Server jettyServer,
                            final ListenerMonitor listenerMonitor,
                            final ContextMonitor contextMonitor )
    {
        super( jettyServer, listenerMonitor, contextMonitor );
    }

    public void start()
    {
        try
        {
            super.start();
        }
        catch( Exception e )
        {
            throw new RuntimeException( e );
        }
    }

    public void stop()
    {
        try
        {
            super.stop();
        }
        catch( InterruptedException e )
        {
            throw new RuntimeException( e );
        }
    }
}