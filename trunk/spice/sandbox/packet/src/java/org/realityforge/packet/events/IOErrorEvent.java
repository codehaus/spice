/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.packet.events;

import java.io.IOException;
import org.realityforge.packet.transport.TcpTransport;

/**
 * Abstract event for IO errors.
 * 
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2003-12-17 00:20:44 $
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
}