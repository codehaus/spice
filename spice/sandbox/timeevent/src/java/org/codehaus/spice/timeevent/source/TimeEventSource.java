package org.codehaus.spice.timeevent.source;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import org.codehaus.spice.event.EventJoin;
import org.codehaus.spice.event.impl.AbstractEventSource;
import org.codehaus.spice.timeevent.events.TimeEvent;
import org.codehaus.spice.timeevent.triggers.TimeTrigger;

/**
 * An EventSource that generates events relating to the passage of time.
 *
 * @author Peter Donald
 * @version $Revision: 1.3 $ $Date: 2004-03-18 03:36:57 $
 */
public class TimeEventSource
    extends AbstractEventSource
{
    /**
     * The list of SchedulingKey objects in order from earliest to latest.
     */
    private final LinkedList _events = new LinkedList();

    /**
     * The list of events that need to be rescheduled.
     */
    private final ArrayList _eventsTriggered = new ArrayList();

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
            new SchedulingKey( this, trigger, userData );
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
        final ListIterator iterator = _events.listIterator();
        while( iterator.hasNext() )
        {
            final SchedulingKey other = (SchedulingKey)iterator.next();
            if( key.compareTo( other ) < 0 )
            {
                iterator.add( key );
                return;
            }
        }
        _events.addLast( key );
    }

    /**
     * Remove specified key from source.
     *
     * @param key the key
     */
    synchronized void removeKey( final SchedulingKey key )
    {
        final Iterator iterator = _events.iterator();
        while( iterator.hasNext() )
        {
            final SchedulingKey other = (SchedulingKey)iterator.next();
            if( key == other )
            {
                iterator.remove();
                return;
            }
        }
    }

    /**
     * @see AbstractEventSource#refresh()
     */
    protected synchronized void refresh()
    {
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
        //_eventsTriggered used so as to avoid creation of new list
        //every refresh.
        _eventsTriggered.clear();

        //Rather than using an iterator, access events via get()
        //as it results in less object allocation
        final int size = _events.size();
        for( int i = 0; i < size; i++ )
        {
            final SchedulingKey key = (SchedulingKey)_events.get( i );
            if( key.getNextTime() <= now )
            {
                key.updateNextTime( now );
                _eventsTriggered.add( key );
                getJoin().addEvent( new TimeEvent( key ) );
            }
        }

        final int count = _eventsTriggered.size();
        for( int i = 0; i < count; i++ )
        {
            final SchedulingKey key =
                (SchedulingKey)_eventsTriggered.get( i );
            _events.remove( key );
            if( -1 != key.getNextTime() )
            {
                schedule( key );
            }
        }
    }
}
