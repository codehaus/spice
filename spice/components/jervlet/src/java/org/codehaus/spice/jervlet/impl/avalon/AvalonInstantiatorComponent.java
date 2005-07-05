/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */

package org.codehaus.spice.jervlet.impl.avalon;

import org.codehaus.spice.jervlet.Instantiator;
import org.apache.avalon.framework.logger.LogEnabled;
import org.apache.avalon.framework.logger.Logger;
import org.apache.avalon.framework.context.Contextualizable;
import org.apache.avalon.framework.context.Context;
import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.service.Serviceable;
import org.apache.avalon.framework.service.ServiceManager;

/**
 * Avalon component wrapping an <code>AvalonInstantiator</code>.
 *
 * @author Johan Sjoberg
 * @author Ben Hogan
 *
 * @dna.component
 * @dna.service type="Instantiator"
 */
public class AvalonInstantiatorComponent
    implements Instantiator, LogEnabled, Serviceable, Contextualizable, Initializable
{
    /** Internal AvalonInstantiator */
    AvalonInstantiator m_avalonInstantiator;

    /** Avalon logger */
    Logger m_logger;

    /** Avalon service manager */
    ServiceManager m_serviceManager;

    /** Avalon context */
    Context m_context;

    /**
     * Instantiate a servlet class. The calls are rerouted
     * to an internal <code>AvalonInstantiator</code>.
     *
     * @param clazz The class to instantiate
     * @return The instatiated class
     * @throws InstantiationException Rethrown from internal AvalonInstantiator
     * @throws IllegalAccessException Rethrown from internal AvalonInstantiator
     */
    public Object instantiate( Class clazz )
        throws InstantiationException, IllegalAccessException
    {
        return m_avalonInstantiator.instantiate( clazz );
    }

    /**
     * Get the <code>Logger</code>.
     *
     * @param logger The logger
     * @dna.logger
     */
    public void enableLogging( Logger logger )
    {
        m_logger = logger;
    }

    /**
     * Get the <code>ServiceManager</code>.
     *
     * @param serviceManager The service manager
     */
    public void service( ServiceManager serviceManager )
    {
        m_serviceManager = serviceManager;
    }

    /**
     * Get the <code>Context</code>
     *
     * @param context The context
     */
    public void contextualize( Context context )
    {
        m_context = context;
    }

    /**
     * Initialize: create a new <code>AvalonInstantiator</code>.
     */
    public void initialize()
    {
        m_avalonInstantiator = new AvalonInstantiator(
          m_context, m_serviceManager, m_logger );
    }
}
