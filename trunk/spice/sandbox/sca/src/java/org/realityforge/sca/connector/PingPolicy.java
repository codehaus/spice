package org.realityforge.sca.connector;

/**
 * Callback used to determine whether
 * to ping a Connection.
 */
public interface PingPolicy
{
   /**
    * Return true if need to ping connection.
    *
    * @return true if need to ping connection.
    */
   boolean checkPingConnection();

   /**
    * Return the time at which the next ping should occur.
    * If the value {@link Long#MAX_VALUE} is returned
    * then no pinging will ever occur.
    *
    * @return the time that ping should be checked
    */
   long nextPingCheck();
}
