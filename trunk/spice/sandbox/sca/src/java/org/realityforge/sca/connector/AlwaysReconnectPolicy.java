package org.realityforge.sca.connector;

/**
 * A Policy that will always attempt to reconnect
 * regardless of how many failures or errors.
 */
public class AlwaysReconnectPolicy
   implements ReconnectionPolicy
{
   /**
    * Constant containing instance of policy.
    */
   public static final AlwaysReconnectPolicy POLICY = new AlwaysReconnectPolicy();

   /**
    * @see org.realityforge.sca.connector.ReconnectionPolicy#attemptConnection
    */
   public boolean attemptConnection( final long lastConnectionAttempt,
                                     final int connectionAttempts )
   {
      return true;
   }

   /**
    * @see org.realityforge.sca.connector.ReconnectionPolicy#disconnectOnError
    */
   public boolean disconnectOnError( final Throwable t )
   {
      return true;
   }

   /**
    * @see org.realityforge.sca.connector.ReconnectionPolicy#reconnectOnDisconnect
    */
   public boolean reconnectOnDisconnect()
   {
      return true;
   }
}
