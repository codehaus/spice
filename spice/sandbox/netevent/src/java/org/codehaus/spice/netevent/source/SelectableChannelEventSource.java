package org.codehaus.spice.netevent.source;

import java.io.IOException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;
import org.codehaus.spice.event.EventJoin;
import org.codehaus.spice.event.impl.AbstractEventSource;
import org.codehaus.spice.netevent.events.AcceptPossibleEvent;
import org.codehaus.spice.netevent.events.ConnectPossibleEvent;
import org.codehaus.spice.netevent.events.ReadPossibleEvent;
import org.codehaus.spice.netevent.events.WritePossibleEvent;
import org.codehaus.spice.netevent.transport.ChannelTransport;

/**
 * An event source that generates events based from SelectableChannels.
 *
 * @author Peter Donald
 * @version $Revision: 1.7 $ $Date: 2004-03-21 23:32:36 $
 */
public class SelectableChannelEventSource
    extends AbstractEventSource
{
    /**
     * The source used to schedule events.
     */
    private Selector _selector;

    /**
     * The amount of time to wait in select.
     */
    private long _selectTimeout = -1;

    /**
     * Create source.
     *
     * @param join the join
     * @throws IOException if error creating source.
     */
    public SelectableChannelEventSource( final EventJoin join )
        throws IOException
    {
        super( join );
        open();
    }

    public void setSelectTimeout( final long selectTimeout )
    {
        _selectTimeout = selectTimeout;
    }

    /**
     * Register channel with source.
     *
     * @param channel the channel
     * @param ops the operations interested in
     * @param userData the associated userdata (if any)
     * @return the SelectionKey
     * @throws IOException if unable to register with source
     */
    public synchronized SelectionKey
        registerChannel( final SelectableChannel channel,
                         final int ops,
                         final Object userData )
        throws IOException
    {
        if( null == _selector )
        {
            throw new IOException( "_selector == null" );
        }
        channel.configureBlocking( false );
        _selector.wakeup();
        return channel.register( _selector, ops, userData );
    }

    /**
     * @see AbstractEventSource#open()
     */
    public synchronized void open()
        throws IOException
    {
        _selector = Selector.open();
    }

    /**
     * @see AbstractEventSource#close()
     */
    public synchronized void close()
        throws IOException
    {
        _selector.wakeup();
        _selector.close();
        _selector = null;
    }

    /**
     * @see AbstractEventSource#refresh()
     */
    protected void refresh()
    {
        System.out.println("Refresh chan .... in " + Thread.currentThread().getName() );

        final Selector selector;
        synchronized( this )
        {
            selector = _selector;
        }
        if( null == selector )
        {
            return;
        }
        try
        {
            selector.selectedKeys().clear();
            if( 0 > _selectTimeout )
            {
                selector.selectNow();
            }
            else
            {
                selector.select( _selectTimeout );
            }
        }
        catch( final IOException ioe )
        {
            return;
        }
        final Set keys = selector.selectedKeys();
        final Iterator iterator = keys.iterator();

        // Walk through the ready keys collection and process date requests.
        while( iterator.hasNext() )
        {
            final SelectionKey key = (SelectionKey)iterator.next();
            iterator.remove();
            handleSelectorEvent( key );
        }
    }

    /**
     * Handle event on specified SelectionKey.
     *
     * @param key the SelectionKey.
     */
    private void handleSelectorEvent( final SelectionKey key )
    {
        if( !key.isValid() )
        {
            return;
        }
        final Object userData = key.attachment();

        if( key.isAcceptable() )
        {
            final ServerSocketChannel channel =
                (ServerSocketChannel)key.channel();
            final AcceptPossibleEvent event =
                new AcceptPossibleEvent( channel, userData );
            getJoin().addEvent( event );
        }
        if( key.isWritable() )
        {
            final ChannelTransport transport = (ChannelTransport)userData;
            final WritePossibleEvent event =
                new WritePossibleEvent( transport );
            getJoin().addEvent( event );
        }
        if( key.isReadable() )
        {
            final ChannelTransport transport = (ChannelTransport)userData;
            final ReadPossibleEvent event =
                new ReadPossibleEvent( transport );
            getJoin().addEvent( event );
        }
        if( key.isConnectable() )
        {
            final ConnectPossibleEvent event =
                new ConnectPossibleEvent( key.channel(), userData, key );
            getJoin().addEvent( event );
        }
    }
}
