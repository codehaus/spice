package org.realityforge.packet.event.impl.perf;

import org.realityforge.packet.event.impl.DefaultEventQueue;
import org.realityforge.packet.event.impl.collections.Buffer;

/**
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2003-12-10 02:07:18 $
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
