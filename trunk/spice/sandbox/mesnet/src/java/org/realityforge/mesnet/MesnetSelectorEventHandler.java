package org.realityforge.mesnet;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.BufferOverflowException;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import org.realityforge.sca.selector.SelectorEventHandler;
import org.realityforge.sca.selector.SelectorManager;

/**
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.2 $ $Date: 2003-11-10 05:54:18 $
 */
public class MesnetSelectorEventHandler
    implements SelectorEventHandler
{
    private static final byte[] MAGIC = new byte[]
    {
        'm', 'a', 'g', 'i', 'c', '0', '1'
    };
    private static final int MAX_CONTROL_SIZE = MAGIC.length + 8 + 8;

    private final ByteBuffer _message =
        ByteBuffer.allocateDirect( MAX_CONTROL_SIZE );

    private final SelectorManager _selectorManager;
    private final SessionManager _sessionManager;

    public MesnetSelectorEventHandler( final SelectorManager selectorManager,
                                    final SessionManager sessionManager )
    {
        _selectorManager = selectorManager;
        _sessionManager = sessionManager;
    }

    public void handleSelectorEvent( final SelectionKey key,
                                     final Object userData )
    {
        if( key.isAcceptable() )
        {
            performAccept( userData, key );
        }
        else
        {
            final TcpTransport transport = (TcpTransport)userData;
            final Session session = transport.getSession();
            if( null == session )
            {
                handleSessionlessEvent( transport );
            }
            else
            {
                handleSessionEvent( transport );
            }
        }
    }

    void handleSessionlessEvent( final TcpTransport transport )
    {
        if( transport.getKey().isReadable() )
        {
            final SocketChannel channel = transport.getChannel();
            try
            {
                final int count = channel.socket().getInputStream().available();
                if( count >= MAX_CONTROL_SIZE )
                {
                    _message.clear();
                    channel.read( _message );
                    for( int i = 0; i < MAGIC.length; i++ )
                    {
                        final byte data = _message.get();
                        if( data != MAGIC[ i ] )
                        {
                            //TODO: Notify about bad magic number
                            sendControlMessage( channel,
                                                MessageUtils.MESSAGE_BAD_MAGIC );
                            transport.close();
                            return;
                        }
                    }
                    final long id = _message.getLong();
                    final long auth = _message.getLong();
                    if( id == -1 )
                    {
                        final Session session = _sessionManager.newSession();
                        session.setStatus( Session.STATUS_CONNECTED );

                        _message.putShort( (short)(9 * -1) ); //Size of message & fact it is control
                        _message.put( MessageUtils.MESSAGE_CONNECTED ); //message
                        _message.putLong( id );
                        _message.putLong( auth );
                        writeMessage( _message, channel );
                    }
                    else
                    {
                        final Session session = _sessionManager.findSession( id );
                        if( null == session )
                        {
                            sendControlMessage( channel,
                                                MessageUtils.MESSAGE_BAD_SESSION );
                            transport.close();
                            return;
                        }

                        final Long sessionAuth = (Long)
                            session.getProperty( MessageUtils.AUTH_KEY );
                        if( null != sessionAuth &&
                            auth != sessionAuth.longValue() )
                        {
                            sendControlMessage( channel,
                                                MessageUtils.MESSAGE_BAD_AUTH );
                            transport.close();
                            return;
                        }
                        else
                        {
                            session.setStatus( Session.STATUS_ESTABLISHED );
                            transport.setSession( session );

                            sendControlMessage( channel,
                                                MessageUtils.MESSAGE_ESTABLISHED );
                        }
                    }
                }
            }
            catch( final BufferOverflowException boe )
            {
                //TODO: Notify about buffer overflow
                transport.close();
            }
            catch( final IOException ioe )
            {
                //TODO: Notify about esablishing connection
                transport.close();
            }
        }
    }

    private void sendControlMessage( final SocketChannel channel,
                                     final byte message )
        throws IOException
    {
        _message.clear();
        _message.putShort( (short)(1 * -1) );
        _message.put( message );
        writeMessage( _message, channel );
    }

    private void writeMessage( final ByteBuffer message,
                               final SocketChannel channel )
        throws IOException
    {
        final int available = message.remaining();
        final int writeCount = channel.write( message );
        if( available != writeCount )
        {
            throw new BufferOverflowException();
        }
    }

    void performAccept( final Object userData,
                        final SelectionKey key )
    {
        final ServerSocketChannel channel = (ServerSocketChannel)key.channel();
        try
        {
            final SocketChannel socketChannel = channel.accept();
            _selectorManager.registerChannel( socketChannel,
                                              SelectionKey.OP_READ |
                                              SelectionKey.OP_WRITE,
                                              this,
                                              userData );
        }
        catch( final IOException e )
        {
            //TODO: Note that problem with accept
        }
    }

    public void handleSessionEvent( final TcpTransport transport )
    {
        final SelectionKey key = transport.getKey();
        if( key.isReadable() )
        {
            performRead( transport );
        }

        if( key.isValid() && key.isWritable() )
        {
            performWrite( transport );
        }
    }

    void performRead( final TcpTransport transport )
    {
        try
        {
            final DataChannel dataChannel =
                transport.getSession().getDataChannel();
            final ByteBuffer buffer = dataChannel.getReadBuffer();
            final int count = transport.getChannel().read( buffer );
            if( -1 == count )
            {
                transport.close();
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
            transport.close();
        }
    }

    void performWrite( final TcpTransport transport )
    {
        final ByteBuffer writeBuffer =
            transport.getSession().getDataChannel().getWriteBuffer();
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
                    transport.close();
                }
            }
        }
    }
}
