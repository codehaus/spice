/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.jervlet.impl.pico;

import org.picocontainer.Startable;
import org.codehaus.spice.jervlet.containers.jetty.JettyContainer;
import org.codehaus.spice.jervlet.ContextHandler;

/**
 * @author <a href="mailto:proyal@pace2020.com">peter royal</a>
 */
public class PicoJettyContainer implements Startable
{
    private final JettyContainer m_jettyContainer;

    public PicoJettyContainer( final JettyContainer jettyContainer )
    {
        m_jettyContainer = jettyContainer;
    }

    public void start()
    {
        try
        {
            m_jettyContainer.initialize();
            m_jettyContainer.start();
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
            m_jettyContainer.stop();
        }
        catch( Exception e )
        {
            throw new RuntimeException( e );
        }
    }

    public ContextHandler createContextHandler()
    {
        return m_jettyContainer.createContextHandler();
    }
}