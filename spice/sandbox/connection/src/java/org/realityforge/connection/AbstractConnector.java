package org.realityforge.connection;

/**
 * The AbstractConnector is a base class for connectors.
 * Connectors establish a connection to a resource
 * and attempt to maintain the connection and reconnect
 * when the connection fails.
 *
 * @mx.component
 */
public abstract class AbstractConnector
{
   /**
    * The associated reconnection policy for connector.
    */
   private ReconnectionPolicy _policy;

   /**
    * The associated monitor that receives
    * events about connector.
    */
   private ConnectorMonitor _monitor;

   /**
    * The underlying connection.
    */
   private ConnectorConnection _connection;

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
   private String _connectionError;

   /**
    * Specify the reconnection policy that connector will use.
    *
    * @param policy the policy
    */
   public void setConnection( final ReconnectionPolicy policy )
   {
      if ( null == policy )
      {
         throw new NullPointerException( "policy" );
      }
      _policy = policy;
   }

   /**
    * Specify the connection that connector will manage.
    *
    * @param connection the connection
    */
   public void setConnection( final ConnectorConnection connection )
   {
      if ( null == connection )
      {
         throw new NullPointerException( "connection" );
      }
      _connection = connection;
   }

   /**
    * Specify the monitor to receive events from connector.
    *
    * @param monitor the monitor
    */
   public void setMonitor( final ConnectorMonitor monitor )
   {
      if ( null == monitor )
      {
         throw new NullPointerException( "monitor" );
      }
      _monitor = monitor;
   }

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
    * Return the last connection error.
    * The error could be caused either by connection
    * failure or failure during operation.
    *
    * @return the last connection error
    */
   public String getConnectionError()
   {
      return _connectionError;
   }

   /**
    * Method to make connector establish connection.
    *
    * @mx.operation
    */
   public void connect()
   {
      final long now = System.currentTimeMillis();

      synchronized ( getSyncLock() )
      {
         if ( isConnected() )
         {
            disconnect();
         }

         while ( !isConnected() && isActive() )
         {
            if ( !_policy.attemptConnection( _connectionAttempts ) )
            {
               _monitor.skippingConnectionAttempt();
               return;
            }
            _monitor.attemptingConnection();
            try
            {
               _lastConnectionTime = now;
               _connection.connect();
               _connectionAttempts = 0;
               _connectionError = null;
               _monitor.connectionEstablished();
            }
            catch ( final Throwable t )
            {
               _connectionAttempts++;
               _connectionError = t.toString();
               _monitor.errorConnecting( t );
            }
         }
      }
   }

   /**
    * Method to disconect Connector.
    *
    * @mx.operation
    */
   public void disconnect()
   {
      synchronized ( getSyncLock() )
      {
         if ( isConnected() )
         {
            _connected = false;
            try
            {
               _connection.disconnect();
            }
            catch ( final Throwable t )
            {
               _monitor.errorDisconnecting( t );
            }
         }
      }
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
    * Attempt to verify Connector is connected.
    * If not connected then the connector will attempt
    * to establish a connection.
    *
    * @return true if connected
    */
   public boolean validateConnection()
   {
      synchronized ( getSyncLock() )
      {
         _monitor.attemptingValidation();
         if ( !verifyConnected() )
         {
            return false;
         }
         else
         {
            try
            {
               _connection.validateConnection();
            }
            catch ( final Throwable t )
            {
               _connectionError = t.toString();
               _monitor.errorValidatingConnection( t );
               disconnect();
               if ( _policy.reconnectOnDisconnect() )
               {
                  connect();
               }
            }
            return isConnected();
         }
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
}
