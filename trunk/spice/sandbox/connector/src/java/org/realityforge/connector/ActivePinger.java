package org.realityforge.connector;

/**
 * A simple Runnable that checks ping for connector
 * in a separate thread.
 */
public class ActivePinger
   implements Runnable
{
   /**
    * The associated connector.
    */
   private final Connector _connector;

   /**
    * The thread that pinger is running in.
    */
   private Thread _thread;

   /**
    * Flag indicating whether the pinger has started.
    */
   private boolean _started;

   /**
    * Create pinger for specified connector.
    *
    * @param connector the connector
    */
   public ActivePinger( final Connector connector )
   {
      if ( null == connector )
      {
         throw new NullPointerException( "connector" );
      }
      _connector = connector;
   }

   /**
    * Deactivate the pinger and wait till
    * it has stopped pinging.
    */
   public synchronized void deactivate()
   {
      while ( isActive() )
      {
         _thread.interrupt();
         try
         {
            wait();
         }
         catch ( final InterruptedException ie )
         {
            //ignore
         }
      }
   }

   /**
    * Return true if pinger has started.
    *
    * @return true if pinger has started.
    */
   public synchronized boolean hasStarted()
   {
      return _started;
   }

   /**
    * Return true if pinger is active.
    *
    * @return true if pinger is active.
    */
   private synchronized boolean isActive()
   {
      return null != _thread;
   }

   /**
    * Main pinging loop.
    */
   public void run()
   {
      synchronized ( this )
      {
         _started = true;
         _thread = Thread.currentThread();
      }

      while ( isActive() )
      {
         final long now = _connector.checkPing();
         try
         {
            Thread.sleep( now );
         }
         catch ( final InterruptedException ie )
         {
            //Ignore and fall to through to isActive
         }
      }

      synchronized ( this )
      {
         _thread = null;
         notifyAll();
      }
   }
}
