/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.packet.handlers;

import org.codehaus.spice.event.AbstractEventHandler;
import org.codehaus.spice.event.EventHandler;
import org.realityforge.packet.events.AckEvent;
import org.realityforge.packet.session.Session;

/**
 * Simple handler that handles ack.
 * 
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2004-01-13 06:45:38 $
 */
public class AckEventHandler
    extends AbstractEventHandler
{
    /**
     * @see EventHandler#handleEvent(Object)
     */
    public void handleEvent( final Object event )
    {
        final AckEvent ce = (AckEvent)event;
        final short sequence = ce.getSequence();
        final Session session = ce.getSession();
        session.getMessageQueue().ack( sequence );
    }
}
