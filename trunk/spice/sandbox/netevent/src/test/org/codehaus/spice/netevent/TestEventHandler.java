package org.codehaus.spice.netevent;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.util.Random;
import org.codehaus.spice.event.AbstractEventHandler;
import org.codehaus.spice.event.EventHandler;
import org.codehaus.spice.netevent.events.ChannelClosedEvent;
import org.codehaus.spice.netevent.events.ConnectEvent;
import org.codehaus.spice.netevent.events.InputDataPresentEvent;
import org.codehaus.spice.netevent.transport.ChannelTransport;

/**
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2004-01-16 03:19:09 $
 */
class TestEventHandler
    extends AbstractEventHandler
{
    private static final byte[] DATA = new byte[]{'B', 'E', 'E', 'R'};

    private static final Random RANDOM = new Random();

    private final String _header;
    private final long _transmitCount;
    private final long _receiveCount;
    private final boolean _closeOnReceive;

    public TestEventHandler( final String header,
                             final long transmitCount,
                             final long receiveCount,
                             final boolean closeOnReceive )
    {
        _header = header;
        _transmitCount = transmitCount;
        _receiveCount = receiveCount;
        _closeOnReceive = closeOnReceive;
    }

    /**
     * @see EventHandler#handleEvent(Object)
     */
    public void handleEvent( final Object event )
    {
        //System.out.println( "event = " + event );
        if( event instanceof InputDataPresentEvent )
        {
            final InputDataPresentEvent e = (InputDataPresentEvent)event;
            final ChannelTransport transport = e.getTransport();
            final int available = transport.getInputStream().available();
            if( available == _receiveCount && _closeOnReceive )
            {
                transport.getOutputStream().close();
            }
        }
        else if( event instanceof ChannelClosedEvent )
        {
            final ChannelClosedEvent ce = (ChannelClosedEvent)event;
            final ChannelTransport transport = ce.getTransport();
            final int available = transport.getInputStream().available();
            output( transport, "Received " + available );
        }
        else if( event instanceof ConnectEvent )
        {
            final ConnectEvent ce = (ConnectEvent)event;
            final ChannelTransport transport = ce.getTransport();
            final SocketChannel channel = (SocketChannel)transport.getChannel();
            final Socket socket = channel.socket();
            final String conn =
                socket.getLocalPort() + "<->" + socket.getPort();
            transport.setUserData( conn );
            final OutputStream outputStream = transport.getOutputStream();
            try
            {
                long transmitCount = _transmitCount;
                if( -1 == transmitCount )
                {
                    transmitCount = Math.abs( RANDOM.nextInt() % 16 * 1024 );
                }
                for( int i = 0; i < transmitCount; i++ )
                {
                    final byte ch = DATA[ i % DATA.length ];
                    outputStream.write( ch );
                }
                outputStream.flush();
                output( transport, "Transmitted " + transmitCount );
            }
            catch( final IOException ioe )
            {
                ioe.printStackTrace();
            }
        }
    }

    private void output( final ChannelTransport transport, final String text )
    {
        final String message =
            _header + " (" + transport.getUserData() + "): " + text;
        System.out.println( message );
    }
}
