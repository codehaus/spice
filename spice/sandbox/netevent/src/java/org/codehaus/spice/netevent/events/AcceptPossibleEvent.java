/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.netevent.events;

import java.nio.channels.ServerSocketChannel;

/**
 * An Event indicating that ServerSocket can accept a connection.
 * 
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2004-01-08 03:41:13 $
 */
public class AcceptPossibleEvent
    extends ServerSocketEvent
{
    /**
     * Create event.
     * 
     * @param channel the channel.
     */
    public AcceptPossibleEvent( final ServerSocketChannel channel )
    {
        super( channel );
    }
}
