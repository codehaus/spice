package org.codehaus.spice.timeevent.source;

import org.codehaus.spice.timeevent.triggers.TimeTrigger;

/**
 * The key that the trigger is registered under.
 *
 * @author Peter Donald
 * @version $Revision: 1.4 $ $Date: 2004-03-21 23:32:36 $
 */
public class SchedulingKey
    implements Comparable
{
    /**
     * The trigger that determines when fireing occurs.
     */
    private final TimeTrigger m_trigger;

    /**
     * the associated userData.
     */
    private final Object m_userData;

    /**
     * Cached time at which trigger will next fire.
     */
    private long m_nextTime;

    /**
     * Flag indicating whether key is valid.
     */
    private boolean m_valid;

    /**
     * Create SchedulingKey.
     *
     * @param trigger the TimeTrigger
     * @param userData the associated userData
     */
    public SchedulingKey( final TimeTrigger trigger,
                          final Object userData )
    {
        if( null == trigger )
        {
            throw new NullPointerException( "trigger" );
        }
        m_trigger = trigger;
        m_userData = userData;
    }

    /**
     * Update expected next time after specified moment.
     *
     * @param moment the moment
     */
    public void updateNextTime( final long moment )
    {
        if( isValid() )
        {
            m_nextTime = m_trigger.getTimeAfter( moment );
        }
    }

    /**
     * Return the associated userData.
     *
     * @return the associated userData.
     */
    public Object getUserData()
    {
        return m_userData;
    }

    /**
     * Return the next time that the trigger expects to fire.
     *
     * @return the next time that the trigger expects to fire.
     */
    public long getNextTime()
    {
        return m_nextTime;
    }

    /**
     * Cancle the time trigger.
     */
    public synchronized void cancel()
    {
        if( isValid() )
        {
            m_valid = false;
            m_nextTime = -1;
        }
    }

    /**
     * Return true if key is valid.
     *
     * @return true if key is valid.
     */
    public synchronized boolean isValid()
    {
        return m_valid;
    }

    /**
     * @see Comparable#compareTo(Object)
     */
    public int compareTo( final Object object )
    {
        final SchedulingKey other = (SchedulingKey)object;
        return (int)( m_nextTime - other.m_nextTime );
    }

    /**
     * @see Object#toString()
     */
    public String toString()
    {
        return "SchedulingKey[" +
               "ID=" + System.identityHashCode( this ) +
               ", IsValid=" + m_valid + "]" +
               ", UserData=" + m_userData + "]";
    }
}
