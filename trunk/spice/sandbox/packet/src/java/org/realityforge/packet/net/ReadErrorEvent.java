/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.packet.net;

import java.io.IOException;
import org.realityforge.packet.net.transport.TcpTransport;

/**
 * An Event indicating a read error.
 * 
 * @author Peter Donald
 * @version $Revision: 1.2 $ $Date: 2004-01-07 02:40:43 $
 */
public class ReadErrorEvent
    extends IOErrorEvent
{
    /**
     * Create event.
     * 
     * @param transport the transport
     * @param ioe the error
     */
    public ReadErrorEvent( final TcpTransport transport,
                           final IOException ioe )
    {
        super( transport, ioe );
    }
}
