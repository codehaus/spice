/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.sca.selector;

import java.io.IOException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;

/**
 * Service enabling management of Non-blocking IO Channels. The service will
 * call back the specified handler when one of the specified operations occurs
 * on the channel.
 *
 * @author Peter Donald
 * @version $Revision: 1.3 $ $Date: 2003-12-05 05:39:33 $
 */
public interface SelectorManager
{
    /**
     * Register a channel with selector. Note the user MUST NOT modify the
     * returned SelectionKeys attachment.
     *
     * @param channel the channel
     * @param ops the operations to register
     * @param handler the handler that will be notified on event
     * @param userData the data passed back into the handler
     * @return the SelectionKey
     * @throws IOException if channel can not be registered
     */
    SelectionKey registerChannel( SelectableChannel channel,
                                  int ops,
                                  SelectorEventHandler handler,
                                  Object userData )
        throws IOException;
}
