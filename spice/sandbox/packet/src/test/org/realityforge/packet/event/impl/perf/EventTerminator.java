package org.realityforge.packet.event.impl.perf;

import java.util.Arrays;
import java.util.List;
import org.realityforge.packet.event.EventSink;

/**
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2003-12-10 02:07:18 $
 */
public class EventTerminator
    implements EventSink
{
    private final String m_name;

    public EventTerminator( final String name )
    {
        m_name = name;
    }

    public boolean addEvent( final Object event )
    {
        return addEvents( new Object[]{event} );
    }

    public boolean addEvents( final Object[] events )
    {
        final boolean drop =
            EventPerfTest.RANDOM.nextBoolean() &&
            EventPerfTest.RANDOM.nextBoolean() &&
            EventPerfTest.RANDOM.nextBoolean() &&
            EventPerfTest.RANDOM.nextBoolean() &&
            EventPerfTest.RANDOM.nextBoolean();
        final List list = Arrays.asList( events );
        if( drop )
        {
            if( EventPerfTest.DEBUG )
            {
                System.out.println( m_name + " rejecting Events: " + list );
            }
            return false;
        }
        else
        {
            if( EventPerfTest.DEBUG )
            {
                System.out.println( m_name + " terminating Events: " + list );
            }
            return true;
        }
    }

    public Object getSinkLock()
    {
        return this;
    }

    public String toString()
    {
        return m_name;
    }
}
