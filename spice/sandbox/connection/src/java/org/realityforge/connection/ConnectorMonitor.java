package org.realityforge.connection;

/**
 * The ConnectorMonitor gets notified of events
 * in AbstractConnector.
 */
public interface ConnectorMonitor
{
   /**
    * Notify that connection attempt about to start.
    * The ability to "veto" the event is given
    * by returning false.
    *
    * @return true to continue to attempt connection
    */
   boolean attemptingConnection();

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
    * validating connection.
    *
    * @param t the error
    */
   boolean errorValidatingConnection( Throwable t );
}
