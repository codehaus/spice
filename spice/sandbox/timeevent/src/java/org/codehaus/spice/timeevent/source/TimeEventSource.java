package org.codehaus.spice.timeevent.source;

import org.codehaus.spice.event.EventJoin;
import org.codehaus.spice.event.impl.AbstractEventSource;
import org.codehaus.spice.timeevent.events.TimeEvent;
import org.codehaus.spice.timeevent.triggers.TimeTrigger;

/**
 * An EventSource that generates events relating to the passage of time.
 *
 * @author Peter Donald
 * @version $Revision: 1.5 $ $Date: 2004-03-26 02:23:02 $
 */
public class TimeEventSource
    extends AbstractEventSource
{
    /**
     * The PriorityQueue of SchedulingKey objects.
     */
    private final BinaryHeap m_queue = new BinaryHeap( 13, BinaryHeap.MIN_COMPARATOR );

    /**
     * Create EventSource.
     *
     * @param join the join
     */
    public TimeEventSource( final EventJoin join )
    {
        super( join );
    }

    /**
     * Schedule a time trigger to generate events.
     *
     * @param trigger the trigger
     * @param userData the associated user data
     * @return the SchedulingKey
     */
    public SchedulingKey addTrigger( final TimeTrigger trigger,
                                     final Object userData )
    {
        final SchedulingKey key =
            new SchedulingKey( trigger, userData );
        key.updateNextTime( System.currentTimeMillis() );
        schedule( key );
        return key;
    }

    /**
     * Add the specified key into linked list in order.
     *
     * @param key the key
     */
    protected synchronized void schedule( final SchedulingKey key )
    {
        if( -1 != key.getNextTime() )
        {
            m_queue.insert( key );
        }
        notifyAll();
    }

    /**
     * @see AbstractEventSource#refresh()
     */
    protected synchronized void refresh()
    {
        System.out.println("Refresh time .... in " + Thread.currentThread().getName() );
        final long now = System.currentTimeMillis();
        refreshAt( now );
    }

    /**
     * Refresh source and generate events for specified time.
     *
     * @param now the time
     */
    synchronized void refreshAt( final long now )
    {
        if( m_queue.isEmpty() )
        {
           try
           {
              wait( 0 );
           }
           catch( final InterruptedException ie )
           {
           }
           return;
        }
        else
        {
            SchedulingKey key = (SchedulingKey)m_queue.peek();
            final long diff = key.getNextTime() - now;
            if( diff > 0 )
            {
                try
                {
                    Thread.sleep( diff );
                }
                catch( final InterruptedException ie )
                {
                    //Ignore
                }
            }
            while( key.getNextTime() <= now )
            {
                m_queue.pop();
                key.updateNextTime( now );
                getJoin().addEvent( new TimeEvent( key ) );
                if( m_queue.isEmpty() )
                {
                    break;
                }
                else
                {
                    key = (SchedulingKey)m_queue.peek();
                }
            }
        }
    }
}
