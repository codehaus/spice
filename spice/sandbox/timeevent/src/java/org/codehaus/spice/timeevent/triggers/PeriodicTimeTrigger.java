package org.codehaus.spice.timeevent.triggers;

/**
 * Class to schedule trigger firing at a specified time interval. The time
 * trigger may be delayed by specified offset. If the offset is -1 then no
 * offset will will
 */
public class PeriodicTimeTrigger
    implements TimeTrigger
{
    /**
     * The offset in milliseconds.
     */
    private final long _offset;

    /**
     * The period at which to fire trigger.
     */
    private final long _period;

    /**
     * The initial trigger time.
     */
    private final long _triggerTime;

    /**
     * Create trigger.
     *
     * @param offset the offset. Must be > 0.
     * @param period the period at which to repeat. -1 indicates fire once.
     */
    public PeriodicTimeTrigger( final int offset, final int period )
    {
        if( offset < 0 )
        {
            throw new IllegalArgumentException( "offset < 0" );
        }
        _offset = offset;
        _period = period;
        _triggerTime = System.currentTimeMillis() + _offset;
    }

    /**
     * @see TimeTrigger#getTimeAfter(long)
     */
    public long getTimeAfter( final long moment )
    {
        if( moment <= _triggerTime )
        {
            return _triggerTime;
        }
        else
        {
            if( -1 == _period )
            {
                return -1;
            }
            else
            {
                final long over = moment - _triggerTime;
                final long remainder = over % _period;
                return moment + (_period - remainder);
            }
        }
    }
}