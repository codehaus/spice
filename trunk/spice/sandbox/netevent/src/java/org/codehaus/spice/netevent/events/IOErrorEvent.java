/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.netevent.events;

import java.io.IOException;
import java.net.SocketAddress;
import org.codehaus.spice.netevent.transport.TcpTransport;

/**
 * Abstract event for IO errors.
 * 
 * @author Peter Donald
 * @version $Revision: 1.2 $ $Date: 2004-01-07 04:17:23 $
 */
public abstract class IOErrorEvent
    extends AbstractTransportEvent
{
    /** The Error. */
    private final IOException _ioe;

    /**
     * Create event for transport with error.
     * 
     * @param transport the transport
     * @param ioe the error
     */
    public IOErrorEvent( final TcpTransport transport,
                         final IOException ioe )
    {
        super( transport );
        if( null == ioe )
        {
            throw new NullPointerException( "ioe" );
        }
        _ioe = ioe;
    }

    /**
     * Return the error.
     * 
     * @return the error.
     */
    public IOException getIoe()
    {
        return _ioe;
    }

    /**
     * @see Object#toString()
     */
    public String toString()
    {
        final SocketAddress address =
            getTransport().getChannel().socket().getRemoteSocketAddress();
        return getClass().getName() + "[" + getIoe() +
               " error connected to " + address + "]";
    }
}
