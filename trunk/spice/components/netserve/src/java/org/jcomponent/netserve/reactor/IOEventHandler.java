package org.jcomponent.netserve.reactor;

import java.nio.channels.SelectableChannel;

/**
 * Interface that receives events
 * that occur on an IO channel.
 */
public interface IOEventHandler
{
   /**
    * Method called when an accept is ready to occur on a channel.
    *
    * @param channel the channel
    * @param userData the data associated with channel by user
    */
   void accept( SelectableChannel channel, Object userData );

   /**
    * Method called when an connect succeded on the channel.
    *
    * @param channel the channel
    * @param userData the data associated with channel by user
    */
   void connect( SelectableChannel channel, Object userData );

   /**
    * Method called when data has been received on a channel.
    *
    * @param channel the channel
    * @param userData the data associated with channel by user
    */
   void receive( SelectableChannel channel, Object userData );

   /**
    * Method called when data has been transmitted on a channel.
    *
    * @param channel the channel
    * @param userData the data associated with channel by user
    */
   void transmitted( SelectableChannel channel, Object userData );
}
