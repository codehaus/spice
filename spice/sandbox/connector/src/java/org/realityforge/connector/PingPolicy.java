package org.realityforge.connector;

/**
 * Callback used to determine whether
 * to ping a Connection.
 */
public interface PingPolicy
{
   /**
    * Return true if need to ping connection.
    *
    * @param connector the associated Connector
    * @return true if need to ping connection.
    */
   boolean checkPingConnection( Connector connector );

   /**
    * Return the time at which the next ping should occur.
    *
    * @param lastPingTime the time of last ping
    * @return the time that ping should be checked
    */
   long nextPingCheck( long lastPingTime );
}
