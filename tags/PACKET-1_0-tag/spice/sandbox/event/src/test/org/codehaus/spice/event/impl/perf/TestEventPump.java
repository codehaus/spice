package org.codehaus.spice.event.impl.perf;

import org.codehaus.spice.event.EventHandler;
import org.codehaus.spice.event.EventSource;
import org.codehaus.spice.event.impl.EventPump;

/**
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2003-12-16 02:03:12 $
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
