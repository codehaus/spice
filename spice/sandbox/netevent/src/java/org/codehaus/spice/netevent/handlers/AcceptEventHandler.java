/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.netevent.handlers;

import java.io.IOException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import org.codehaus.spice.event.EventHandler;
import org.codehaus.spice.event.EventSink;
import org.codehaus.spice.netevent.events.AcceptErrorEvent;
import org.codehaus.spice.netevent.events.AcceptEvent;
import org.codehaus.spice.netevent.events.AcceptPossibleEvent;

/**
 * @author Peter Donald
 * @version $Revision: 1.4 $ $Date: 2004-01-16 00:17:45 $
 */
public class AcceptEventHandler
    extends AbstractDirectedHandler
{
    public AcceptEventHandler( final EventSink sink )
    {
        super( sink );
    }

    /**
     * @see EventHandler#handleEvent(Object)
     */
    public void handleEvent( final Object event )
    {
        final AcceptPossibleEvent ce = (AcceptPossibleEvent)event;
        final ServerSocketChannel ssChannel = (ServerSocketChannel)ce.getChannel();
        if( !ssChannel.isOpen() )
        {
            return;
        }

        try
        {
            final SocketChannel channel = ssChannel.accept();
            if( null != channel )
            {
                final AcceptEvent result = new AcceptEvent( channel );
                getSink().addEvent( result );
            }
        }
        catch( final IOException ioe )
        {
            final AcceptErrorEvent error =
                new AcceptErrorEvent( ssChannel, ce.getUserData(), ioe );
            getSink().addEvent( error );
        }
    }
}
