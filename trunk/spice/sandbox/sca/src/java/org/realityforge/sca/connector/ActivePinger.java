package org.realityforge.sca.connector;

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
    * Flag indicating whether the pinger has ended.
    */
   private boolean _ended;

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
      final Thread thread = _thread;
      _thread = null;
      while ( !_ended )
      {
         thread.interrupt();
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
    * Main pinging loop.
    */
   public void run()
   {
      synchronized ( this )
      {
         _started = true;
         _thread = Thread.currentThread();
      }

      while ( null != _thread )
      {
         final long now = System.currentTimeMillis();
         final long then = _connector.checkPing();
         final long sleep = then - now;
         try
         {
            Thread.sleep( sleep );
         }
         catch ( final InterruptedException ie )
         {
            //Ignore and fall to through to isActive
         }
      }

      synchronized ( this )
      {
         _ended = true;
         notifyAll();
      }
   }
}
