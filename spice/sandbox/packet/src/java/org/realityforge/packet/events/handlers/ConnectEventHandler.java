package org.realityforge.packet.events.handlers;

import java.io.IOException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import org.codehaus.spice.event.AbstractEventHandler;
import org.codehaus.spice.event.EventHandler;
import org.realityforge.packet.events.ConnectEvent;
import org.realityforge.packet.transport.TcpTransport;
import org.realityforge.sca.selector.SelectorEventHandler;
import org.realityforge.sca.selector.SelectorManager;

/**
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2003-12-17 06:10:23 $
 */
public class ConnectEventHandler
    extends AbstractEventHandler
{
    private final static int BUFFER_SIZE = 8 * 1024;

    private final SelectorManager _selectorManager;
    private final SelectorEventHandler _handler;

    public ConnectEventHandler( final SelectorManager selectorManager,
                                final SelectorEventHandler handler )
    {
        _selectorManager = selectorManager;
        _handler = handler;
    }

    /**
     * @see EventHandler#handleEvent(Object)
     */
    public void handleEvent( final Object event )
    {
        final ConnectEvent ce = (ConnectEvent)event;
        final ServerSocketChannel ssChannel = ce.getServerSocketChannel();

        SocketChannel channel = null;
        try
        {
            channel = ssChannel.accept();
            final TcpTransport transport =
                new TcpTransport( channel, BUFFER_SIZE, BUFFER_SIZE );
            transport.register( _selectorManager, _handler );
        }
        catch( final IOException ioe )
        {
            if( null != channel )
            {
                try
                {
                    channel.close();
                }
                catch( final IOException ioe2 )
                {
                }
            }
            //TODO: handle gracefully
            ioe.printStackTrace();
        }
    }

}
