package org.codehaus.spice.netevent.selector;

import java.io.IOException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;
import org.codehaus.spice.event.EventSink;
import org.codehaus.spice.event.EventSource;
import org.codehaus.spice.event.impl.DefaultEventQueue;
import org.codehaus.spice.netevent.events.AcceptPossibleEvent;
import org.codehaus.spice.netevent.events.ConnectPossibleEvent;
import org.codehaus.spice.netevent.events.ReadPossibleEvent;
import org.codehaus.spice.netevent.events.WritePossibleEvent;
import org.codehaus.spice.netevent.transport.ChannelTransport;

/**
 * @author Peter Donald
 * @version $Revision: 1.4 $ $Date: 2004-01-21 23:44:58 $
 */
public class SocketEventSource
    implements EventSource
{
    private final EventSource _source;
    private EventSink _sink;
    private Selector _selector;

    public SocketEventSource( final DefaultEventQueue queue )
        throws IOException
    {
        _source = queue;
        _sink = queue;
        open();
    }

    public Object getEvent()
    {
        refresh();
        return _source.getEvent();
    }

    public Object[] getEvents( final int count )
    {
        refresh();
        return _source.getEvents( count );
    }

    public Object getSourceLock()
    {
        return _source.getSourceLock();
    }

    public SelectionKey registerChannel( final SelectableChannel channel,
                                         final int ops,
                                         final Object userData )
        throws IOException
    {
        channel.configureBlocking( false );
        return channel.register( _selector, ops, userData );
    }

    public void open()
        throws IOException
    {
        _selector = Selector.open();
    }

    public void close()
        throws IOException
    {
        _selector.wakeup();
        _selector.close();
    }

    void refresh()
    {
        try
        {
            _selector.selectNow();
        }
        catch( final IOException ioe )
        {
            return;
        }
        final Set keys = _selector.selectedKeys();
        final Iterator iterator = keys.iterator();

        // Walk through the ready keys collection and process date requests.
        while( iterator.hasNext() )
        {
            final SelectionKey key = (SelectionKey)iterator.next();
            iterator.remove();
            handleSelectorEvent( key );
        }
    }

    private void handleSelectorEvent( final SelectionKey key )
    {
        final Object userData = key.attachment();

        if( key.isAcceptable() )
        {
            final ServerSocketChannel channel =
                (ServerSocketChannel)key.channel();
            final AcceptPossibleEvent event =
                new AcceptPossibleEvent( channel, userData );
            _sink.addEvent( event );
        }
        if( key.isWritable() )
        {
            final ChannelTransport transport = (ChannelTransport)userData;
            final WritePossibleEvent event =
                new WritePossibleEvent( transport );
            _sink.addEvent( event );
        }
        if( key.isReadable() )
        {
            final ChannelTransport transport = (ChannelTransport)userData;
            final ReadPossibleEvent event =
                new ReadPossibleEvent( transport );
            _sink.addEvent( event );
        }
        if( key.isConnectable() )
        {
            final ConnectPossibleEvent event =
                new ConnectPossibleEvent( key.channel(), userData );
            _sink.addEvent( event );
        }
    }
}
