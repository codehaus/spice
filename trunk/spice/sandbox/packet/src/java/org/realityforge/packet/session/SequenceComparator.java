package org.realityforge.packet.session;

import java.util.Comparator;
import org.realityforge.packet.Packet;

/**
 * Comparator for sorting packets by sequence number.
 * 
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2004-01-14 01:29:02 $
 */
class SequenceComparator
    implements Comparator
{
    static final SequenceComparator COMPARATOR = new SequenceComparator();

    /**
     * @see Comparator#compare(Object, Object)
     */
    public int compare( final Object o1, final Object o2 )
    {
        final Packet p1 = (Packet)o1;
        final Packet p2 = (Packet)o2;

        return p2.getSequence() - p1.getSequence();
    }
}
