/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.sca.selector.impl;

import java.nio.channels.SelectionKey;
import org.realityforge.sca.selector.SelectorEventHandler;

/**
 * @author Peter Donald
 * @version $Revision: 1.3 $ $Date: 2003-12-05 05:39:34 $
 */
class DelayingSelectorEventHandler
    implements SelectorEventHandler
{
    private final long m_delay;

    DelayingSelectorEventHandler( long delay )
    {
        m_delay = delay;
    }

    public void handleSelectorEvent( SelectionKey key, Object userData )
    {
        while( true )
        {
            try
            {
                Thread.sleep( m_delay );
                System.out.println( "Exiting delay loop" );
                return;
            }
            catch( InterruptedException e )
            {
            }
        }
    }
}
