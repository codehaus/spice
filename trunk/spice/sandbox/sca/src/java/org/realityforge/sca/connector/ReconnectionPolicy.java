package org.realityforge.sca.connector;

/**
 * Interface representing policy Connector uses to determine when to
 * re-establish the connection.
 */
public interface ReconnectionPolicy
{
    /**
     * Return true to continue with connection attempt.
     * 
     * @param connectionAttempts the number of sequential connection failures
     * @param lastConnectionAttemptTime time at which connection was last
     * attempted
     * @return true to continue with connection attempt.
     */
    boolean attemptConnection( long lastConnectionAttemptTime,
                               int connectionAttempts );

    /**
     * Return true to disconnect connection on specified error.
     * 
     * @param t the error
     * @return true to disconnect connection.
     */
    boolean disconnectOnError( Throwable t );

    /**
     * Return true to reconnect on any forced disconnection.
     * 
     * @return true to reconnect on any forced disconnection.
     */
    boolean reconnectOnDisconnect();
}
