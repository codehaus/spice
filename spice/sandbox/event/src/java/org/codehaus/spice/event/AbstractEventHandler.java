/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.event;

/**
 * Abstract class to sublclass to implement EventHandler. The handleEvents
 * method is already implemented and simply calls handleEvent() by iterating
 * over passed arguments.
 * 
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2003-12-17 04:57:20 $
 */
public abstract class AbstractEventHandler
    implements EventHandler
{
    /**
     * @see EventHandler#handleEvents(Object[])
     */
    public void handleEvents( final Object[] events )
    {
        for( int i = 0; i < events.length; i++ )
        {
            handleEvent( events[ i ] );
        }
    }
}
