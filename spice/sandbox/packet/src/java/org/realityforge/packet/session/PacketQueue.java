package org.realityforge.packet.session;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.realityforge.packet.Packet;

/**
 * A queue of packets for session.
 * 
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2004-01-13 07:00:02 $
 */
public class PacketQueue
{
    /**
     * Max difference between successive sequence numbers to test for wrap
     * around.
     */
    private static final short MAX_DIFF = Short.MAX_VALUE / 2;

    /** List of attributes associated with session. */
    private final List _packets = new LinkedList();

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

            if( isLessThanOrEqual( seq, sequence ) )
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

    /**
     * Return true if seq1 is less than or equal seq2 accounting for
     * wraparound.
     * 
     * @param seq1 a sequence
     * @param seq2 a sequence
     * @return true if seq1 is less than or equal seq2
     */
    private boolean isLessThanOrEqual( final short seq1,
                                       final short seq2 )
    {
        return seq1 <= seq2 || seq1 - MAX_DIFF > seq2;
    }

}
