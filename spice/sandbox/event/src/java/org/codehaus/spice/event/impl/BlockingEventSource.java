package org.codehaus.spice.event.impl;

import org.codehaus.spice.event.EventSource;

/**
 * A BlockingEventSource will wait until notified prior to attempting to
 * retrieve events from the underlying EventSource.
 *
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2004-03-11 04:40:14 $
 */
public class BlockingEventSource
   implements EventSource
{
   /**
    * The underlying EventSource to retrieve events from.
    */
   private final EventSource _source;

   /**
    * Create an instance.
    *
    * @param source the source to wrap.
    */
   public BlockingEventSource( final EventSource source )
   {
      if( null == source )
      {
         throw new NullPointerException( "source" );
      }
      _source = source;
   }

   /**
    * @see EventSource#getEvent()
    */
   public Object getEvent()
   {
      final Object lock = getSourceLock();
      Object result = null;
      while( null == result )
      {
         synchronized( lock )
         {
            result = _source.getEvent();
         }
      }
      return result;
   }

   /**
    * @see EventSource#getEvents(int)
    */
   public Object[] getEvents( final int count )
   {
      final Object lock = getSourceLock();
      Object[] results = null;
      while( null == results || 0 == results.length )
      {
         synchronized( lock )
         {
            results = _source.getEvents( count );
         }
      }
      return results;
   }

   /**
    * @see EventSource#getSourceLock()
    */
   public Object getSourceLock()
   {
      return _source.getSourceLock();
   }
}
