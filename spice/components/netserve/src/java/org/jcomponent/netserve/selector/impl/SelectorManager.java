package org.jcomponent.netserve.selector.impl;

import java.io.IOException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;

import org.jcomponent.netserve.selector.impl.NullSelectorEventHandler;
import org.jcomponent.netserve.selector.impl.NullSelectorMonitor;
import org.jcomponent.netserve.selector.SelectorEventHandler;

/**
 * The SelectorManager makes it easy to start a selector
 * in a thread and receive events on selection.
 */
public class SelectorManager
   implements Runnable
{
   /**
    * The monitor that receives notifications of Connection events
    */
   private SelectorMonitor m_monitor = NullSelectorMonitor.MONITOR;

   /**
    * The handler for selector events.
    */
   private SelectorEventHandler m_handler = NullSelectorEventHandler.HANDLER;

   /**
    * Selector used to monitor for accepts.
    */
   private Selector m_selector;

   /**
    * Flag indicating whether manager is running.
    */
   private boolean m_active;

   /**
    * Timeout on selector.
    */
   private int m_timeout = 500;

   /**
    * Set the SelectorMonitor that receives events when changes occur.
    *
    * @param monitor the SelectorMonitor.
    */
   public void setMonitor( final SelectorMonitor monitor )
   {
      if ( null == monitor )
      {
         throw new NullPointerException( "monitor" );
      }
      m_monitor = monitor;
   }

   /**
    * Set the SelectorEventHandler to handle selection events.
    *
    * @param handler the SelectorEventHandler
    */
   public void setHandler( final SelectorEventHandler handler )
   {
      if ( null == handler )
      {
         throw new NullPointerException( "handler" );
      }
      m_handler = handler;
   }

   /**
    * Set the timeout on the selector.
    *
    * @param timeout the timeout.
    */
   public void setTimeout( final int timeout )
   {
      m_timeout = timeout;
   }

   /**
    * Initialize the selector to monitor accept attempts.
    *
    * @throws java.io.IOException if unable to initialize selector
    */
   public void startup()
      throws IOException
   {
      getMonitor().selectorStartup();
      setSelector( Selector.open() );
      startThread();
   }

   /**
    * Method to shutdown the SelectorManager.
    */
   public void shutdown()
   {
      setInactive();
      shutdownSelector();
      waitForThreadToComplete();
   }

   /**
    * Start the thread to accept connections.
    */
   protected void startThread()
   {
      final Thread thread = new Thread( this, getThreadName() );
      thread.start();
      while ( !isRunning() )
      {
         Thread.yield();
      }
   }

   /**
    * Shutdown the selector and any associated acceptors.
    */
   protected void shutdownSelector()
   {
      synchronized ( getSelectorLock() )
      {
         if ( null != m_selector )
         {
            getMonitor().selectorShutdown();
            try
            {
               m_selector.wakeup();
               m_selector.close();
            }
            catch ( final IOException ioe )
            {
               getMonitor().errorClosingSelector( ioe );
            }
         }
      }
   }

   /**
    * Wait for selector thread to complete.
    */
   protected void waitForThreadToComplete()
   {
      while ( null != m_selector )
      {
         synchronized ( getSelectorLock() )
         {
            try
            {
               wait( 100 );
            }
            catch ( InterruptedException e )
            {
               //Ignore
            }
         }
      }
   }

   /**
    * Set a flag to indicate reactor is inactive.
    * Will eventually cause the selector thread to
    * close. Note that shutdownSelector() should be
    * called after this method to make sure all
    * resources are deallocated.
    */
   public void setInactive()
   {
      setRunning( false );
   }

   /**
    * Return true if the selector is manager is running.
    *
    * @return true if the selector is manager is running.
    */
   public boolean isRunning()
   {
      synchronized ( getSelectorLock() )
      {
         return m_active;
      }
   }

   /**
    * Register a channel with selector.
    *
    * @param channel the channel
    * @param ops the operations to register
    * @return the SelectionKey
    * @throws java.io.IOException if channel can not be registered
    */
   public SelectionKey registerChannel( final SelectableChannel channel,
                                        final int ops )
      throws IOException
   {
      if ( null == channel )
      {
         throw new NullPointerException( "channel" );
      }
      channel.configureBlocking( false );
      synchronized ( getSelectorLock() )
      {
         final Selector selector = getSelector();
         selector.wakeup();
         return channel.register( selector, ops );
      }
   }

   /**
    * This is the main connection accepting loop.
    */
   public void run()
   {
      setRunning( true );

      getMonitor().enteringSelectorLoop();

      // Here's where everything happens. The select method will
      // return when any operations registered above have occurred, the
      // thread has been interrupted, etc.
      while ( isRunning() )
      {
         if ( !performSelect() ||
            !isRunning() )
         {
            synchronized ( getSelectorLock() )
            {
               continue;
            }
         }
         final Set keys = getSelector().selectedKeys();
         final Iterator iterator = keys.iterator();

         // Walk through the ready keys collection and process date requests.
         while ( iterator.hasNext() )
         {
            final SelectionKey key = (SelectionKey) iterator.next();
            iterator.remove();
            // The key indexes into the selector so you
            // can retrieve the socket that's ready for I/O
            getHandler().handleSelectorEvent( key );
         }
      }

      getMonitor().exitingSelectorLoop();
      setSelector( null );
   }

   /**
    * Perform select operation and return true if
    * successful and connections present.
    *
    * @return true if select resulted in keys being present
    */
   private boolean performSelect()
   {
      try
      {
         getMonitor().enteringSelect();
         final int count = getSelector().select( m_timeout );
         getMonitor().selectCompleted( count );
         if ( 0 != count )
         {
            return true;
         }
      }
      catch ( final Exception e )
      {
         //Ignore
      }
      return false;
   }

   /**
    * Return the lock used to synchronize access to selector.
    *
    * @return the lock used to synchronize access to selector.
    */
   protected Object getSelectorLock()
   {
      return this;
   }

   /**
    * Set the selector associated with reactor.
    *
    * @param selector the selector associated with reactor.
    */
   protected void setSelector( final Selector selector )
   {
      synchronized ( getSelectorLock() )
      {
         m_selector = selector;
         notifyAll();
      }
   }

   /**
    * Return the selector associated with reactor.
    *
    * @return the selector associated with reactor.
    */
   protected Selector getSelector()
   {
      synchronized ( getSelectorLock() )
      {
         if ( null == m_selector )
         {
            throw new NullPointerException( "selector" );
         }
         return m_selector;
      }
   }

   /**
    * Return the monitor associated with manager.
    *
    * @return the monitor associated with manager.
    */
   protected SelectorMonitor getMonitor()
   {
      return m_monitor;
   }

   /**
    * Return the handler associated with manager.
    *
    * @return the handler associated with manager.
    */
   SelectorEventHandler getHandler()
   {
      return m_handler;
   }

   /**
    * Set the flag to specify whether th Reactor is running.
    *
    * @param running the flag to specify whether th Reactor is running
    */
   protected void setRunning( final boolean running )
   {
      synchronized ( getSelectorLock() )
      {
         m_active = running;
      }
   }

   /**
    * Return the name of thread that Selector will run in.
    *
    * @return the name of thread that Selector will run in.
    */
   protected String getThreadName()
   {
      return "SelectorManager";
   }
}
