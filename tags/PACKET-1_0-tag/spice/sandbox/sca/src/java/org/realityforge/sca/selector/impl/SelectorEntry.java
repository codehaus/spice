/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.sca.selector.impl;

import org.realityforge.sca.selector.SelectorEventHandler;

/**
 * A simple class that contains data relating to a specific Selector
 * registration.
 *
 * @author Peter Donald
 * @version $Revision: 1.3 $ $Date: 2003-12-05 05:39:33 $
 */
class SelectorEntry
{
    /** The associated handler that is passed events about channel. */
    private final SelectorEventHandler m_handler;

    /** The user specified data that is passed to the handler. */
    private final Object m_userData;

    /**
     * Create an Entry for Selector registration.
     *
     * @param handler the handler
     * @param userData the user specified data
     */
    SelectorEntry( final SelectorEventHandler handler,
                   final Object userData )
    {
        if( null == handler )
        {
            throw new NullPointerException( "handler" );
        }
        m_handler = handler;
        m_userData = userData;
    }

    /**
     * Return the handler for channel.
     *
     * @return the handler for channel.
     */
    SelectorEventHandler getHandler()
    {
        return m_handler;
    }

    /**
     * Return the userData passed to handler.
     *
     * @return the userData passed to handler.
     */
    Object getUserData()
    {
        return m_userData;
    }
}
