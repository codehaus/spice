package org.realityforge.connection;

public abstract class AbstractConnector
{
   /**
    * A flag indicating whether the connection
    * is "active".
    */
   private boolean _active;

   /**
    * Time at which last transmission occured.
    */
   private long _lastTxTime;

   /**
    * The last message transmitted. May be null.
    * Simply used to display status on the web page.
    */
   private Object _lastTxMessage;

   /**
    * Time at which last receive occured.
    */
   private long _lastRxTime;

   /**
    * The last message received. May be null.
    * Simply used to display status on the web page.
    */
   private Object _lastRxMessage;

   /**
    * Method called to indicate transmission occured.
    *
    * @param message the message
    */
   public void transmissionOccured( final Object message )
   {
      _lastTxTime = System.currentTimeMillis();
      _lastTxMessage = message;
   }

   /**
    * Method called to indicate receive occured.
    *
    * @param message the message
    */
   public void receiveOccured( final Object message )
   {
      _lastRxTime = System.currentTimeMillis();
      _lastRxMessage = message;
   }

   /**
    * Method called to indicate bidirectional communication occured.
    *
    * @param message the message
    */
   public void commOccured( final Object message )
   {
      final long now = System.currentTimeMillis();
      _lastTxTime = now;
      _lastRxTime = now;
      _lastRxMessage = message;
      _lastTxMessage = message;
   }
}
