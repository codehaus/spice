package org.codehaus.spice.timeevent.source;

import org.codehaus.spice.timeevent.triggers.TimeTrigger;

/**
 * The key that the trigger is registered under.
 *
 * @author Peter Donald
 * @version $Revision: 1.3 $ $Date: 2004-02-23 03:52:09 $
 */
public class SchedulingKey
   implements Comparable
{
   /**
    * The TimeEventSource where trigger is registered.
    */
   private final TimeEventSource _source;

   /**
    * The trigger that determines when fireing occurs.
    */
   private final TimeTrigger _trigger;

   /**
    * the associated userData.
    */
   private final Object _userData;

   /**
    * Cached time at which trigger will next fire.
    */
   private long _nextTime;

   /**
    * Create SchedulingKey.
    *
    * @param source the TimeEventSource
    * @param trigger the TimeTrigger
    * @param userData the associated userData
    */
   public SchedulingKey( final TimeEventSource source,
                         final TimeTrigger trigger,
                         final Object userData )
   {
      if( null == source )
      {
         throw new NullPointerException( "source" );
      }
      if( null == trigger )
      {
         throw new NullPointerException( "trigger" );
      }
      _source = source;
      _trigger = trigger;
      _userData = userData;
   }

   /**
    * Update expected next time after specified moment.
    *
    * @param moment the moment
    */
   public void updateNextTime( final long moment )
   {
      _nextTime = _trigger.getTimeAfter( moment );
   }

   /**
    * Return the associated userData.
    *
    * @return the associated userData.
    */
   public Object getUserData()
   {
      return _userData;
   }

   /**
    * Return the next time that the trigger expects to fire.
    *
    * @return the next time that the trigger expects to fire.
    */
   public long getNextTime()
   {
      return _nextTime;
   }

   /**
    * Cancle the time trigger.
    */
   public void cancel()
   {
      _source.removeKey( this );
      _nextTime = -1;
   }

   /**
    * @see Comparable#compareTo(Object)
    */
   public int compareTo( final Object object )
   {
      final SchedulingKey other = (SchedulingKey)object;
      return (int)(_nextTime - other._nextTime);
   }

   /**
    * @see Object#toString()
    */
   public String toString()
   {
      return "SchedulingKey[" +
             "ID=" + System.identityHashCode( this ) +
             ", UserData=" + _userData + "]";
   }
}
