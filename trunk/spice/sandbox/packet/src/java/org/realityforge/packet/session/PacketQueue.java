package org.realityforge.packet.session;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.realityforge.packet.Packet;
import org.realityforge.packet.handlers.Protocol;

/**
 * A queue of packets for session.
 * 
 * @author Peter Donald
 * @version $Revision: 1.2 $ $Date: 2004-01-14 03:03:57 $
 */
public class PacketQueue
{

    /** List of attributes associated with session. */
    private final List _packets = new ArrayList();

    /** List of attributes associated with session. */
    private final List _nacks = new ArrayList();

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
            return (Packet)_packets.get( 0 );
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
            return (Packet)_packets.remove( 0 );
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
        _packets.add( packet );
        Collections.sort( _packets, SequenceComparator.COMPARATOR );
    }

    /**
     * Ack a packet with specified sequence. Involves removing every packet with
     * sequence less than or equal to supplied sequence.
     * 
     * @param sequence the sequence
     * @return true if found packet to ack
     */
    public synchronized boolean ack( final short sequence )
    {
        boolean found = false;

        final Iterator iterator = _packets.iterator();
        while( iterator.hasNext() )
        {
            final Packet packet = (Packet)iterator.next();
            final short seq = packet.getSequence();
            if( seq == sequence )
            {
                found = true;
            }

            if( Protocol.isLessThanOrEqual( seq, sequence ) )
            {
                iterator.remove();
            }
        }

        return found;
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
            final Packet packet = (Packet)_packets.get( i );
            final short seq = packet.getSequence();
            if( seq == sequence )
            {
                return packet;
            }
        }
        return null;
    }

}
