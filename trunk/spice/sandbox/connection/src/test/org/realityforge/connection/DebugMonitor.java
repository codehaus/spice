package org.realityforge.connection;

/**
 * A debug monitor that prints all messages to standard out.
 */
class DebugMonitor
   implements ConnectorMonitor
{
   /**
    * A constant containing instance of null monitor.
    */
   public static final DebugMonitor MONITOR = new DebugMonitor();

   /**
    * @see ConnectorMonitor#attemptingConnection
    */
   public void attemptingConnection()
   {
      System.out.println( "attemptingConnection" );
   }

   /**
    * @see ConnectorMonitor#connectionEstablished
    */
   public void connectionEstablished()
   {
      System.out.println( "connectionEstablished" );
   }

   /**
    * @see ConnectorMonitor#errorConnecting
    */
   public void errorConnecting( final Throwable t )
   {
      System.out.println( "errorConnecting(" + t + ")" );
   }

   /**
    * @see ConnectorMonitor#errorDisconnecting
    */
   public void errorDisconnecting( final Throwable t )
   {
      System.out.println( "errorDisconnecting(" + t + ")" );
   }

   /**
    * @see ConnectorMonitor#attemptingValidation
    */
   public void attemptingValidation()
   {
      System.out.println( "attemptingValidation" );
   }

   /**
    * @see ConnectorMonitor#errorValidatingConnection
    */
   public void errorValidatingConnection( final Throwable t )
   {
      System.out.println( "errorValidatingConnection(" + t + ")" );
   }

   /**
    * @see ConnectorMonitor#skippingConnectionAttempt
    */
   public void skippingConnectionAttempt()
   {
      System.out.println( "skippingConnectionAttempt" );
   }
}
