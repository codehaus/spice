package org.jcomponent.netserve.sockets;

import java.nio.channels.SelectableChannel;

public interface IOEventHandler
{
   void accept( SelectableChannel channel, Object userData );
   void receive( SelectableChannel channel, Object userData );
   void transmitted( SelectableChannel channel, Object userData );
}
