package org.realityforge.packet.events;

import org.realityforge.packet.session.Session;

/**
 * Event indicating a packet was acked or nacked.
 * 
 * @author Peter Donald
 * @version $Revision: 1.2 $ $Date: 2004-01-19 04:37:43 $
 */
public abstract class PacketSequenceEvent
    extends SessionEvent
{
    /** The sequence. */
    private final short _sequence;

    /**
     * Create event.
     * 
     * @param session the session
     * @param sequence the sequence
     */
    public PacketSequenceEvent( final Session session,
                                final short sequence )
    {
        super( session );
        _sequence = sequence;
    }

    /**
     * Return the sequence.
     * 
     * @return the sequence.
     */
    public short getSequence()
    {
        return _sequence;
    }

    /**
     * @see SessionEvent#getEventDescription()
     */
    protected String getEventDescription()
    {
        return super.getEventDescription() + " Sequence: " + getSequence();
    }
}
