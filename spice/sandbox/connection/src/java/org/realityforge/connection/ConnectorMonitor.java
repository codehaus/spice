package org.realityforge.connection;

/**
 * The ConnectorMonitor gets notified of events
 * in AbstractConnector.
 */
public interface ConnectorMonitor
{
   boolean attemptingConnection();

   void connectionEstablished();

   void errorConnecting( Throwable t );

   void errorDisconnecting( Throwable t );
}
