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
import org.codehaus.spice.netevent.transport.MultiBufferInputStream;

/**
 * @author Peter Donald
 * @version $Revision: 1.5 $ $Date: 2004-05-17 06:21:39 $
 */
class TestEventHandler
    extends AbstractEventHandler
{
    private static final byte[] DATA = new byte[]{'B', 'E', 'E', 'R', ' '};

    private static final Random RANDOM = new Random();

    private final String _header;
    private final long _transmitCount;
    private final long _receiveCount;
    private final boolean _closeOnReceive;
    private int m_connectCount;
    private int m_disconnectCount;

    TestEventHandler( final String header,
                      final long transmitCount,
                      final long receiveCount,
                      final boolean closeOnReceive )
    {
        _header = header;
        _transmitCount = transmitCount;
        _receiveCount = receiveCount;
        _closeOnReceive = closeOnReceive;
    }

    int getConnectCount()
    {
        return m_connectCount;
    }

    int getDisconnectCount()
    {
        return m_disconnectCount;
    }

    /**
     * @see EventHandler#handleEvent(Object)
     */
    public void handleEvent( final Object event )
    {
        if( event instanceof InputDataPresentEvent )
        {
            final InputDataPresentEvent e = (InputDataPresentEvent) event;
            final ChannelTransport transport = e.getTransport();
            final int available = transport.getInputStream().available();
            if( available == _receiveCount && _closeOnReceive )
            {
                transport.getOutputStream().close();
            }
        }
        else if( event instanceof ChannelClosedEvent )
        {
            m_disconnectCount++;
            final ChannelClosedEvent ce = (ChannelClosedEvent) event;
            final ChannelTransport transport = ce.getTransport();
            status( transport, "Channel Diconnected." );
            receiveData( transport );
        }
        else if( event instanceof ConnectEvent )
        {
            m_connectCount++;
            final ConnectEvent ce = (ConnectEvent) event;
            final ChannelTransport transport = ce.getTransport();
            setupUserData( transport );

            status( transport, "Channel Connected." );
            transmitData( transport );
        }
    }

    private void status( final ChannelTransport transport, final String msg )
    {
        final int connected = m_connectCount - m_disconnectCount;
        final String message = _header + " (" + transport.getUserData() + "): " +
                               ( msg + " Current=" + connected + " Connected= " + m_connectCount + " Disconnected=" + m_disconnectCount );
        System.out.println( message );
    }

    private void receiveData( final ChannelTransport transport )
    {
        final MultiBufferInputStream in = transport.getInputStream();
        final int available = in.available();

        final int count = Math.min( 15, available );
        final StringBuffer sb = new StringBuffer();
        try
        {
            for( int i = 0; i < count; i++ )
            {
                sb.append( (char) in.read() );
            }
        }
        catch( IOException e )
        {
            e.printStackTrace();
        }

        status( transport, "Received " + available + " Sample: " + sb );
    }

    private void transmitData( final ChannelTransport transport )
    {
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
                final byte ch = DATA[i % DATA.length];
                outputStream.write( ch );
            }
            outputStream.flush();
            status( transport, "Transmitting " + transmitCount );
        }
        catch( final IOException ioe )
        {
            ioe.printStackTrace();
        }
    }

    private void setupUserData( final ChannelTransport transport )
    {
        final SocketChannel channel = (SocketChannel) transport.getChannel();
        final Socket socket = channel.socket();
        final String conn = socket.getLocalPort() + "<->" + socket.getPort();
        transport.setUserData( conn );
    }

}
