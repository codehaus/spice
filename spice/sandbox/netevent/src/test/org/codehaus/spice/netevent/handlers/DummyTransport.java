package org.codehaus.spice.netevent.handlers;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import org.codehaus.spice.netevent.transport.TcpTransport;

/**
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2004-01-07 05:06:31 $
 */
class DummyTransport
    extends TcpTransport
{
    private boolean _closed;

    DummyTransport()
        throws IOException
    {
        super( SocketChannel.open(), 1, 1 );
    }

    public void close()
    {
        _closed = true;
    }

    boolean isClosed()
    {
        return _closed;
    }
}
