package org.realityforge.mesnet;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.util.LinkedList;
import org.realityforge.sca.selector.SelectorEventHandler;

/**
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-11-05 04:08:36 $
 */
public class MesnetSelectorEventHandler
    implements SelectorEventHandler
{
    public void handleSelectorEvent( final SelectionKey key,
                                     final Object userData )
    {
        final Session session = (Session)userData;
        handleSessionEvent( key, session );
    }

    public void handleSessionEvent( final SelectionKey key,
                                    final Session session )
    {
        if( key.isReadable() )
        {
            performRead( session );
        }

        if( key.isValid() && key.isWritable() )
        {
            performWrite( session );
        }
    }

    void performRead( final Session session )
    {
        try
        {
            final ByteBuffer buffer = session.getReadBuffer();
            final int count = session.getChannel().read( buffer );
            if( -1 == count )
            {
                endConnection( session );
                return;
            }
            //TODO: Notify Monitor that data received
            final long now = System.currentTimeMillis();
            session.setLastReadTime( now );
            final short messageSize = buffer.getShort( 0 );
            final int limit = buffer.limit();
            if( limit + 2 >= messageSize )
            {
                final byte[] data = new byte[ messageSize ];
                buffer.clear();
                buffer.getShort();
                buffer.get( data, 0, messageSize );
                buffer.compact();
                //TODO: Notify Monitor that message received
                final LinkedList queue = session.getReadQueue();
                synchronized( queue )
                {
                    queue.addLast( data );
                }
            }
        }
        catch( final IOException e )
        {
            endConnection( session );
            return;
        }
    }

    void performWrite( final Session session )
    {
        final LinkedList queue = session.getWriteQueue();
        synchronized( queue )
        {
            if( queue.size() > 0 )
            {
                final byte[] data = (byte[])queue.removeFirst();
                final ByteBuffer buffer = session.getWriteBuffer();
                buffer.clear();
                buffer.put( data );
                try
                {
                    //TODO: Notify Monitor that data being written
                    final int count = session.getChannel().write( buffer );
                    final long now = System.currentTimeMillis();
                    session.setLastWriteTime( now );
                    if( count != data.length )
                    {
                        // TODO: Notify monitor as this means the channels
                        // write buffer is not big enough for messages?
                        endConnection( session );
                    }
                }
                catch( final IOException e )
                {
                    queue.addFirst( data );
                    endConnection( session );
                }
            }
        }
    }

    void endConnection( final Session session )
    {
        //TODO: Notify ending connection
        session.getKey().cancel();
        try
        {
            session.getChannel().close();
        }
        catch( IOException e )
        {
            // TODO: Note error closing channel
        }
        session.setKey( null );
        session.setChannel( null );
        //TODO: Attempt to resestablish connection with client
        //or wait till they connect?
    }

}
