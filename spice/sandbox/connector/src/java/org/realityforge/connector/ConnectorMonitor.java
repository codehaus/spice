package org.realityforge.connector;

/**
 * The ConnectorMonitor gets notified of events
 * in Connector.
 */
public interface ConnectorMonitor
{
   /**
    * Notify that connection attempt about to start.
    */
   void attemptingConnection();

   /**
    * Notify monitor that connection has been established.
    */
   void connectionEstablished();

   /**
    * Notify monitor that there was an error
    * connecting.
    *
    * @param t the error
    */
   void errorConnecting( Throwable t );

   /**
    * Notify monitor that there was an error
    * disconnecting.
    *
    * @param t the error
    */
   void errorDisconnecting( Throwable t );

   /**
    * Notify monito that attempting to
    * validate connection.
    */
   void attemptingValidation();

   /**
    * Notify monitor that there was an error
    * validating connection. After this method
    * is called the connection will be disconnected.
    */
   void errorValidatingConnection( Throwable t );

   /**
    * Notify that the policy has indicated that
    * a connection attempt should not be made at
    * this point in time.
    */
   void skippingConnectionAttempt();

   /**
    * Notify monitor that Connection is being
    * disconnected.
    */
   void attemptingDisconnection();
}
