package org.realityforge.connection;

/**
 * A null monitor that consumes all messages.
 */
public class NullMonitor
   implements ConnectorMonitor
{
   /**
    * A constant containing instance of null monitor.
    */
   public static final NullMonitor MONITOR = new NullMonitor();

   /**
    * @see ConnectorMonitor#attemptingConnection
    */
   public void attemptingConnection()
   {
   }

   /**
    * @see ConnectorMonitor#connectionEstablished
    */
   public void connectionEstablished()
   {
   }

   /**
    * @see ConnectorMonitor#errorConnecting
    */
   public void errorConnecting( final Throwable t )
   {
   }

   /**
    * @see ConnectorMonitor#errorDisconnecting
    */
   public void errorDisconnecting( final Throwable t )
   {
   }

   /**
    * @see ConnectorMonitor#attemptingValidation
    */
   public void attemptingValidation()
   {
   }

   /**
    * @see ConnectorMonitor#errorValidatingConnection
    */
   public void errorValidatingConnection( final Throwable t )
   {
   }

   /**
    * @see ConnectorMonitor#skippingConnectionAttempt
    */
   public void skippingConnectionAttempt()
   {
   }
}
