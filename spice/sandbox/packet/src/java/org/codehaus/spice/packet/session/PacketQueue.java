package org.codehaus.spice.packet.session;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.codehaus.spice.packet.Packet;
import org.codehaus.spice.packet.handlers.Protocol;

/**
 * A queue of packets for session.
 * 
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2004-03-22 01:17:50 $
 */
public class PacketQueue
{
    /**
     * List of attributes associated with session.
     */
    private final List _packets = new ArrayList();

    /**
     * Return the number of packets in queue.
     *
     * @return the number of packets in queue.
     */
    public synchronized int size()
    {
        return _packets.size();
    }

    /**
     * Look at packet at head of queue.
     *
     * @return the packet
     */
    public synchronized Packet peek()
    {
        if( _packets.size() == 0 )
        {
            return null;
        }
        else
        {
            return (Packet) _packets.get( 0 );
        }
    }

    /**
     * Return packet at head of queue and remove from queue.
     *
     * @return the packet
     */
    public synchronized Packet pop()
    {
        if( _packets.size() == 0 )
        {
            return null;
        }
        else
        {
            return (Packet) _packets.remove( 0 );
        }
    }

    /**
     * Add packet to queue.
     * 
     * @param packet the packet
     */
    public synchronized void addPacket( final Packet packet )
    {
        if( null == packet )
        {
            throw new NullPointerException( "packet" );
        }
        if( !_packets.contains( packet ) )
        {
            _packets.add( packet );
            Collections.sort( _packets, SequenceComparator.COMPARATOR );
        }
    }

    /**
     * Ack a packet with specified sequence. Involves removing every packet with
     * sequence less than or equal to supplied sequence.
     * 
     * @param sequence the sequence
     */
    public synchronized List ack( final short sequence )
    {
        final ArrayList result = new ArrayList();
        boolean found = false;

        final Iterator iterator = _packets.iterator();
        while( iterator.hasNext() && !found )
        {
            final Packet packet = (Packet) iterator.next();
            final short seq = packet.getSequence();

            if( Protocol.isLessThanOrEqual( seq, sequence ) )
            {
                if( seq == sequence )
                {
                    found = true;
                }
                result.add( packet );
                iterator.remove();
            }
        }

        return result;
    }

    /**
     * Return the packet with specified sequence.
     * 
     * @param sequence the sequence
     * @return the packet with sequence or null if no such packet
     */
    public synchronized Packet getPacket( final short sequence )
    {
        final int size = _packets.size();
        for( int i = 0; i < size; i++ )
        {
            final Packet packet = (Packet) _packets.get( i );
            final short seq = packet.getSequence();
            if( seq == sequence )
            {
                return packet;
            }
        }
        return null;
    }

    public synchronized List getSequences()
    {
        final ArrayList result = new ArrayList();
        final int size = _packets.size();
        for( int i = 0; i < size; i++ )
        {
            final Packet packet = (Packet) _packets.get( i );
            final short seq = packet.getSequence();
            result.add( new Short( seq ) );
        }
        return result;
    }

    public String toString()
    {
        return "PacketQueue[" + _packets + "]";
    }
}
