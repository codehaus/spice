package org.jcomponent.netserve.sockets.impl;

import java.io.IOException;
import java.nio.channels.Selector;
import java.nio.channels.SelectionKey;
import java.nio.channels.SelectableChannel;
import java.nio.channels.ClosedChannelException;
import java.util.Set;
import java.util.Iterator;

import org.jcomponent.netserve.sockets.SelectorEventHandler;
import org.jcomponent.netserve.sockets.NullSelectorEventHandler;

/**
 * The AbstractNIOReactor offers a base class
 * that makes it easy to write a reactor for NIO
 * channels. The Channels have to be SelectableChannels
 * but have no other requirements.
 */
public abstract class AbstractNIOReactor
   implements Runnable
{
   /**
    * The monitor that receives notifications of Connection events
    */
   private NIOAcceptorMonitor m_monitor = NullNIOAcceptorMonitor.MONITOR;

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
   private boolean m_running;

   /**
    * Timeout on selector.
    */
   private int m_timeout = 500;

   /**
    * Set the NIOAcceptorMonitor that receives events when changes occur.
    *
    * @param monitor the NIOAcceptorMonitor that receives events when
    *        changes occur.
    */
   public void setMonitor( final NIOAcceptorMonitor monitor )
   {
      m_monitor = monitor;
   }

   /**
    * Set the SelectorEventHandler to handle selection events.
    *
    * @param handler the SelectorEventHandler
    */
   public void setMonitor( final SelectorEventHandler handler )
   {
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
    * @throws IOException if unable to initialize selector
    */
   public void startupSelector()
      throws IOException
   {
      synchronized ( getSelectorLock() )
      {
         setSelector( Selector.open() );
      }
      startThread();
   }

   /**
    * Start the thread to accept connections.
    */
   protected void startThread()
   {
      final Thread thread = new Thread( this, "NIOAcceptorManager" );
      thread.start();
      while ( !isRunning() )
      {
         Thread.yield();
      }
   }

   /**
    * Shutdown the selector and any associated acceptors.
    */
   public void shutdownSelector()
   {
      m_monitor.selectorShutdown();
      synchronized ( getSelectorLock() )
      {
         final Selector selector = getSelector();
         if ( null != selector )
         {
            try
            {
               selector.wakeup();
               selector.close();
            }
            catch ( final IOException ioe )
            {
               m_monitor.errorClosingSelector( ioe );
            }
         }
      }
      while ( null != getSelector() )
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
    * Set the flag to specify whether th Reactor is running.
    *
    * @param running the flag to specify whether th Reactor is running
    */
   protected void setRunning( final boolean running )
   {
      synchronized ( getSelectorLock() )
      {
         m_running = running;
      }
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
         return m_running;
      }
   }

   /**
    * This is the main connection accepting loop.
    */
   public void run()
   {
      setRunning( true );
      // Here's where everything happens. The select method will
      // return when any operations registered above have occurred, the
      // thread has been interrupted, etc.
      while ( isRunning() )
      {
         if ( !performSelect() ||
            !isRunning() )
         {
            continue;
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
            m_handler.handleSelectorEvent( key );
         }
      }
      m_monitor.exitingSelectorLoop();
      synchronized ( getSelectorLock() )
      {
         setSelector( null );
         notifyAll();
      }
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
         m_monitor.enteringSelect();
         final int count = getSelector().select( m_timeout );
         m_monitor.selectCompleted( count );
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
      m_selector = selector;
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
    * Return the monitor associated with reactor.
    *
    * @return the monitor associated with reactor.
    */
   protected NIOAcceptorMonitor getMonitor()
   {
      return m_monitor;
   }

   /**
    * Register a channel with selector.
    *
    * @param channel the channel
    * @param ops the operations to register
    * @return the SelectionKey
    * @throws ClosedChannelException if channel is closed
    */
   protected SelectionKey registerChannel( final SelectableChannel channel,
                                           final int ops )
      throws ClosedChannelException
   {
      return channel.register( getSelector(), ops );
   }

   /**
    * Implement method to handle events from selector.
    *
    * @param key the SelectionKey
    */
   protected abstract void handleChannel( SelectionKey key );
}
