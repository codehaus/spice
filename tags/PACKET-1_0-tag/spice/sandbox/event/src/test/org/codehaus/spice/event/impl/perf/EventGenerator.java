package org.codehaus.spice.event.impl.perf;

import java.util.Arrays;
import java.util.List;
import org.codehaus.spice.event.EventSource;

/**
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2003-12-16 02:03:12 $
 */
public class EventGenerator
    implements EventSource
{
    private final String m_prefix;
    private int m_index;

    public EventGenerator( final String prefix )
    {
        m_prefix = prefix;
    }

    public Object[] getEvents( final int count )
    {
        doWait();
        final int size =
            Math.min( count,
                      Math.abs( EventPerfTest.RANDOM.nextInt() % count ) +
                      count / 2 );
        final Object[] events = new Object[ size ];
        for( int i = 0; i < events.length; i++ )
        {
            events[ i ] = createEvent();
        }
        final List list = Arrays.asList( events );
        if( EventPerfTest.DEBUG )
        {
            System.out.println( m_prefix + " generating events " + list );
        }
        return events;
    }

    public Object getEvent()
    {
        doWait();
        return createEvent();
    }

    private Object createEvent()
    {
        return m_prefix + m_index++;
    }

    private void doWait()
    {
        try
        {
            final int time = 50 +
                             Math.abs( EventPerfTest.RANDOM.nextInt() % 1000 );
            Thread.sleep( time );
        }
        catch( final InterruptedException ie )
        {
        }
    }

    public Object getSourceLock()
    {
        return this;
    }
}
