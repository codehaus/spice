package org.jcomponent.netserve.selector;

import java.nio.channels.SelectionKey;
import java.nio.channels.SelectableChannel;
import java.io.IOException;

/**
 * Service enabling management of Non-blocking IO Channels.
 * The service will call back the specified handler
 * when one of the specified operations occurs on the
 * channel.
 */
public interface SelectorManager
{
   /**
    * Register a channel with selector.
    * Note the user MUST NOT modify the returned
    * SelectionKeys attachment.
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
