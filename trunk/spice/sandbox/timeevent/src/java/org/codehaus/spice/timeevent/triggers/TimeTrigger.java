package org.codehaus.spice.timeevent.triggers;

/**
 * Interface used by all time-based triggers.
 */
public interface TimeTrigger
{
    /**
     * Return the time after specified moment that trigger will fire. Returning
     * -1 indicates that the trigger will no longer fire.
     *
     * @param moment the specified moment in time
     * @return the time after moment that trigger will fire.
     */
    long getTimeAfter( long moment );
}
