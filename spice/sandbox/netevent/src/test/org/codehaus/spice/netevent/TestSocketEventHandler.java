package org.codehaus.spice.netevent;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import org.codehaus.spice.event.AbstractEventHandler;
import org.codehaus.spice.event.EventHandler;
import org.codehaus.spice.netevent.events.ChannelClosedEvent;
import org.codehaus.spice.netevent.events.ConnectEvent;
import org.codehaus.spice.netevent.transport.ChannelTransport;
import org.codehaus.spice.netevent.transport.TransportOutputStream;

/**
 * @author Peter Donald
 * @version $Revision: 1.8 $ $Date: 2004-01-15 06:12:25 $
 */
class TestSocketEventHandler
    extends AbstractEventHandler
{
    /**
     * @see EventHandler#handleEvent(Object)
     */
    public void handleEvent( final Object event )
    {
        //System.out.println( "TTTTTTTTTTTTT: " + event );
        if( event instanceof ChannelClosedEvent )
        {
            final ChannelClosedEvent ce = (ChannelClosedEvent)event;
            final ChannelTransport transport = ce.getTransport();
            final String message =
                "Received " + transport.getInputStream().available() +
                "B via " + transport.getUserData();
            System.out.println( message );
        }
        else if( event instanceof ConnectEvent )
        {
            final ConnectEvent ce = (ConnectEvent)event;
            final ChannelTransport transport = ce.getTransport();
            final SocketChannel channel = (SocketChannel)transport.getChannel();
            final int port = channel.socket().getPort();
            transport.setUserData( new Integer( port ) );
            final TransportOutputStream outputStream =
                transport.getOutputStream();
            try
            {
                outputStream.write( 'H' );
                outputStream.write( 'E' );
                outputStream.write( 'L' );
                outputStream.write( 'O' );
                outputStream.flush();
            }
            catch( final IOException ioe )
            {
                ioe.printStackTrace();
            }
        }
    }
}
