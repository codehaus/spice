package org.realityforge.packet.event.impl.perf;

import org.realityforge.packet.event.EventHandler;
import org.realityforge.packet.event.EventSource;
import org.realityforge.packet.event.impl.EventPump;

/**
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2003-12-10 02:07:18 $
 */
public class TestEventPump
    extends EventPump
{
    private final String _name;

    public TestEventPump( final EventSource source,
                          final EventHandler handler,
                          final String name )
    {
        super( source, handler );
        _name = name;
    }

    public String toString()
    {
        return _name;
    }

    public void refresh()
    {
        if( EventPerfTest.DEBUG )
        {
            System.out.println( "Refreshing: " + _name );
        }
        super.refresh();
    }
}
