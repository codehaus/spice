package org.codehaus.spice.event.impl;

import org.codehaus.spice.event.EventSink;

/**
 * A BlockingEventSink will wait until notified prior to attempting to retrieve
 * events from the underlying EventSource.
 *
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2004-03-26 00:38:31 $
 */
public class BlockingEventSink
   implements EventSink
{
   /**
    * The underlying EventSink to send events to.
    */
   private final EventSink _sink;

   /**
    * Create an instance.
    *
    * @param sink the sink to wrap.
    */
   public BlockingEventSink( final EventSink sink )
   {
      if( null == sink )
      {
         throw new NullPointerException( "sink" );
      }
      _sink = sink;
   }

   /**
    * @see EventSink#addEvent(Object)
    */
   public boolean addEvent( final Object event )
   {
      final Object lock = getSinkLock();
      boolean result = false;
      while( false == result )
      {
         synchronized( lock )
         {
            result = _sink.addEvent( event );
         }
      }
      return true;
   }

   /**
    * @see EventSink#addEvents(Object[])
    */
   public boolean addEvents( final Object[] events )
   {
      final Object lock = getSinkLock();
      boolean result = false;
      while( false == result )
      {
         synchronized( lock )
         {
            result = _sink.addEvents( events );
         }
      }
      return true;
   }

   /**
    * @see EventSink#getSinkLock()
    */
   public Object getSinkLock()
   {
      return _sink.getSinkLock();
   }
}
