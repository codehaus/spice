package org.codehaus.spice.event.impl.perf;

import org.codehaus.spice.event.impl.DefaultEventQueue;
import org.codehaus.spice.event.impl.collections.Buffer;

/**
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2003-12-16 02:03:12 $
 */
public class TestQueue
    extends DefaultEventQueue
{
    private final String _name;

    public TestQueue( final String name,
                      final Buffer buffer )
    {
        super( buffer );
        _name = name;
    }

    public String toString()
    {
        return _name;
    }
}
