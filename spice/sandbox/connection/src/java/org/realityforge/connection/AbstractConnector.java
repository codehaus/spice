package org.realityforge.connection;



/**
 * @mx.component
 */
public abstract class AbstractConnector
{
   /**
    * A flag indicating whether the connection
    * is "active".
    */
   private boolean _active;

   /**
    * A flag indicating whether the connection
    * is "connected".
    */
   private boolean _connected;

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
    * The time the last connection attempt started.
    */
   private long _lastConnectionTime;

   /**
    * Number of sequential failed connection
    * attempts.
    */
   private int _connectionAttempts;

   /**
    * The reason the last conenction attempt failed.
    */
   private String _connectionFailureReason;

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

   /**
    * Return the time at which last transmission occured.
    *
    * @return the time at which last transmission occured.
    * @mx.attribute
    */
   public long getLastTxTime()
   {
      return _lastTxTime;
   }

   /**
    * Return the last message transmitted.
    *
    * @return the last message transmitted.
    * @mx.attribute
    */
   public Object getLastTxMessage()
   {
      return _lastTxMessage;
   }

   /**
    * Return the time at which last receive occured.
    *
    * @return the time at which last receive occured.
    * @mx.attribute
    */
   public long getLastRxTime()
   {
      return _lastRxTime;
   }

   /**
    * Return the last message received.
    *
    * @return the last message received.
    * @mx.attribute
    */
   public Object getLastRxMessage()
   {
      return _lastRxMessage;
   }

   /**
    * Return true if connector is active.
    *
    * @return true if connector is active.
    * @mx.attribute
    */
   public boolean isActive()
   {
      return _active;
   }

   /**
    * Set the flag to indicate if the connector is active or inactive.
    *
    * @param active the flag to indicate if the connector is active or inactive.
    * @mx.attribute
    */
   public void setActive( final boolean active )
   {
      _active = active;
   }

   /**
    * Return true if Connector connected.
    *
    * @return true if Connector connected.
    * @mx.attribute
    */
   public boolean isConnected()
   {
      return _connected;
   }

   /**
    * Set the connected state.
    *
    * @param connected the connected state.
    * @mx.attribute
    */
   protected void setConnected( final boolean connected )
   {
      _connected = connected;
   }

   /**
    * Return the time the last connection attempt started.
    *
    * @return the time the last connection attempt started.
    * @mx.attribute
    */
   public long getLastConnectionTime()
   {
      return _lastConnectionTime;
   }

   /**
    * Return the number of sequential failed connection attempts.
    *
    * @return the number of sequential failed connection attempts.
    * @mx.attribute
    */
   public int getConnectionAttempts()
   {
      return _connectionAttempts;
   }

   /**
    * Return the reason the last conenction attempt failed.
    *
    * @return the reason the last conenction attempt failed.
    */
   public String getConnectionFailureReason()
   {
      return _connectionFailureReason;
   }

   /**
    * Code so that connector will actually establish connection.
    * @mx.operation
    */
   public void connect()
   {
      final long now = System.currentTimeMillis();

      synchronized ( getSyncLock() )
      {
         if( isConnected() )
         {
            disconnect();
         }

         while ( !isConnected() && isActive() )
         {
            //TODO: Starting connnection event - is that ok? else return
            try
            {
               _lastConnectionTime = now;
               doConnect();
               _connectionAttempts = 0;
               _connectionFailureReason = null;
               //TODO: Connnection started event
            }
            catch ( final Throwable t )
            {
               _connectionAttempts++;
               _connectionFailureReason = t.toString();
               //TODO: Connnection start error event - continue connecting or return?
            }
         }
      }
   }

   public void disconnect()
   {

   }

   /**
    * Attempt to verify Connector is connected.
    * If not connected then the connector will attempt
    * to establish a connection.
    *
    * @return true if connected
    */
   public boolean verifyConnected()
   {
      synchronized ( getSyncLock() )
      {
         if ( !isConnected() )
         {
            connect();
         }
         return isConnected();
      }
   }

   /**
    * Return the object that will be used to
    * synchronization connection/disconnection.
    *
    * @return the sync lock
    */
   protected Object getSyncLock()
   {
      return this;
   }

   /**
    * Method to implement to actually start connection.
    *
    * @throws Exception if unable to connect
    */
   protected abstract void doConnect()
      throws Exception;
}
