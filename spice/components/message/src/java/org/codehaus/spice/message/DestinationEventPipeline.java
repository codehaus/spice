/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.message;

import org.d_haven.event.EventHandler;
import org.d_haven.event.Pipe;
import org.d_haven.event.Sink;
import org.d_haven.event.Source;
import org.d_haven.event.command.EventPipeline;
import org.d_haven.event.impl.DefaultPipe;

/**
 * EventPipeline that manages a Destination, only allowing value messages to be enqueued.
 *
 * @author <a href="mailto:peter.royal@pobox.com">peter royal</a>
 */
class DestinationEventPipeline implements EventPipeline
{
    private final Pipe m_pipe;
    private final Source[] m_sources;
    private final EventHandler m_eventHandler;

    public DestinationEventPipeline( final Destination destination )
    {

        m_pipe = new DefaultPipe( new ValidMessageEnqueuePredicate( destination ) );
        m_sources = new Source[]{m_pipe};
        m_eventHandler = new DestinationEventHandler( destination );
    }

    Sink getSink()
    {
        return m_pipe;
    }

    public EventHandler getEventHandler()
    {
        return m_eventHandler;
    }

    public Source[] getSources()
    {
        return m_sources;
    }
}