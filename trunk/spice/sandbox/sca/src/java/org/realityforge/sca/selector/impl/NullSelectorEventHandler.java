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
 * Basic implementation of SelectorEventHandler that does nothing.
 *
 * @author Peter Donald
 * @version $Revision: 1.3 $ $Date: 2003-12-05 05:39:33 $
 */
public class NullSelectorEventHandler
    implements SelectorEventHandler
{
    /** Constant containing instance of NullSelectorEventHandler. */
    public static final NullSelectorEventHandler HANDLER = new NullSelectorEventHandler();

    /**
     * @see SelectorEventHandler#handleSelectorEvent(SelectionKey, Object)
     */
    public void handleSelectorEvent( final SelectionKey key,
                                     final Object userData )
    {
    }
}
