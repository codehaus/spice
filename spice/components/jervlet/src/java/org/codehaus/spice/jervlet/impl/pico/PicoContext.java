/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.jervlet.impl.pico;

import org.codehaus.spice.jervlet.ContextHandler;
import org.codehaus.spice.jervlet.Instantiator;
import org.codehaus.spice.jervlet.impl.DefaultContext;
import org.picocontainer.Startable;

import java.net.URL;

/**
 * @author <a href="mailto:peter.royal@pobox.com">peter royal</a>
 */
public class PicoContext extends DefaultContext implements Startable
{
    private final ContextHandler m_contextHandler;

    public PicoContext( ContextHandler contextHandler,
                        String context,
                        URL resource,
                        Instantiator instantiator )
    {
        super(context, null, resource, false, instantiator);

        m_contextHandler = contextHandler;

        System.out.println( "Created Context - " + this );
    }

    public void start()
    {
        System.out.println( "Starting Context - " + this );

        m_contextHandler.addContext( this );
        m_contextHandler.startContext( this );
    }

    public void stop()
    {
        m_contextHandler.stopContext( this );
        m_contextHandler.removeContext( this );
    }
}