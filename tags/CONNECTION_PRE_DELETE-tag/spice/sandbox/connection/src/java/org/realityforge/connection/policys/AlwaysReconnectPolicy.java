package org.realityforge.connection.policys;

import org.realityforge.connection.ReconnectionPolicy;

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
    * @see ReconnectionPolicy#attemptConnection
    */
   public boolean attemptConnection( final long lastConnectionAttempt,
                                     final int connectionAttempts )
   {
      return true;
   }

   /**
    * @see ReconnectionPolicy#disconnectOnError
    */
   public boolean disconnectOnError( final Throwable t )
   {
      return true;
   }

   /**
    * @see ReconnectionPolicy#reconnectOnDisconnect
    */
   public boolean reconnectOnDisconnect()
   {
      return true;
   }
}
