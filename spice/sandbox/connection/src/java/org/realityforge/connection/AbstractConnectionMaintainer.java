package org.realityforge.connection;

public abstract class AbstractConnectionMaintainer
{
   /**
    * A Flag indicating whether the connection
    * is "active".
    */
   private boolean _active;

   private long _lastTxTime;
   private Object _lastTxMessage;

   private long _lastRxTime;
   private Object _lastRxMessage;

}
