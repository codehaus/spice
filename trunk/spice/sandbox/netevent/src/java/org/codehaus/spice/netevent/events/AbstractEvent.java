package org.codehaus.spice.netevent.events;

/**
 * An abstract base class for net events.
 * 
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2004-01-08 03:41:13 $
 */
public abstract class AbstractEvent
{
    /**
     * @see Object#toString()
     */
    public String toString()
    {
        final String shortName = getShortName();
        return shortName + "[" + getEventDescription() + "]";
    }

    /**
     * Return the description of event.
     * 
     * @return the description of event.
     */
    protected abstract String getEventDescription();

    /**
     * Return the "Short" name of class sans package name.
     * 
     * @return the short name of class
     */
    String getShortName()
    {
        final String name = getClass().getName();
        final int index = name.lastIndexOf( '.' ) + 1;
        return name.substring( index );
    }
}
