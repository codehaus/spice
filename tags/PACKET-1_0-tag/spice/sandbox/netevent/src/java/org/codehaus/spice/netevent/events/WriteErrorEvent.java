/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.netevent.events;

import java.io.IOException;
import org.codehaus.spice.netevent.transport.ChannelTransport;

/**
 * Event indicating a write error occured.
 * 
 * @author Peter Donald
 * @version $Revision: 1.2 $ $Date: 2004-01-08 03:41:13 $
 */
public class WriteErrorEvent
    extends IOErrorEvent
{
    /**
     * Create event.
     * 
     * @param transport the transport
     * @param ioe the error
     */
    public WriteErrorEvent( final ChannelTransport transport,
                            final IOException ioe )
    {
        super( transport, ioe );
    }
}
