package org.realityforge.sca.connector;

/**
 * A Policy that will always attempt to limit
 * the number of connection attempts in a
 * period of time.
 *
 * <p>After N connection attempts it will force a
 * delay of T between successive connection
 * attempts. This is an attempt not to overload
 * the resource being connected to.</p>
 */
public class LimitingReconnectPolicy
   implements ReconnectionPolicy
{
   /**
    * The number of attempts allowed before delay will kick in.
    */
   private final int _attempts;

   /**
    * The delay between successive connection attempts.
    */
   private final long _delay;

   /**
    * Create a policy instance.
    *
    * @param attempts the number of attempts before the delay is enabled.
    * @param delay the delay
    */
   public LimitingReconnectPolicy( final int attempts,
                                   final long delay )
   {
      _attempts = attempts;
      _delay = delay;
   }

   /**
    * @see org.realityforge.sca.connector.ReconnectionPolicy#attemptConnection
    */
   public boolean attemptConnection( final long lastConnectionAttempt,
                                     final int connectionAttempts )
   {
      if( connectionAttempts >= _attempts )
      {
         final long now = System.currentTimeMillis();
         final long nextAttempt = lastConnectionAttempt + _delay;
         if( now < nextAttempt )
         {
            return false;
         }
      }
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
