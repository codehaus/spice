package org.realityforge.packet.event.impl;

/**
 * The EventPumpRunner will refresh specified Event pumps until deactivated.
 * This essentially means that the Runner can be used to run a single EventPump
 * or a set of event pumps in series.
 * 
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2003-12-09 05:27:05 $
 */
public class EventPumpRunner
    implements Runnable
{
    /** The set of pumps this runner will refresh. */
    final EventPump[] _eventPumps;

    /** Flag indicating runner has started. */
    boolean _started;

    /** Flag indicating runner has finished. */
    boolean _finished;

    /** Flag indicating runner is active. */
    boolean _active;

    /**
     * Create the runner for specified pumps.
     * 
     * @param eventPumps the pumps
     */
    public EventPumpRunner( final EventPump[] eventPumps )
    {
        if( null == eventPumps )
        {
            throw new NullPointerException( "eventPumps" );
        }
        for( int i = 0; i < eventPumps.length; i++ )
        {
            if( null == eventPumps[ i ] )
            {
                throw new NullPointerException( "eventPumps[" + i + "]" );
            }
        }
        _eventPumps = eventPumps;
    }

    /**
     * Return true if pump has started.
     * 
     * @return true if pump has started.
     */
    public synchronized boolean isStarted()
    {
        return _started;
    }

    /**
     * Wait until pump has started.
     */
    public synchronized void waitUntilStarted()
    {
        while( !isStarted() )
        {
            try
            {
                wait();
            }
            catch( final InterruptedException ie )
            {
            }
        }
    }

    /**
     * Return true if pump is active.
     * 
     * @return true if pump is active.
     */
    public synchronized boolean isActive()
    {
        return _active;
    }

    /**
     * Signal for the pump to shutdown and wait till deactivation occurs.
     */
    public synchronized void deactivate()
    {
        _active = false;
        while( !_finished )
        {
            try
            {
                wait();
            }
            catch( final InterruptedException ie )
            {
            }
        }
    }

    /**
     * Actually run the pumps.
     */
    public void run()
    {
        synchronized( this )
        {
            _started = true;
            _active = true;
            notifyAll();
        }

        while( isActive() )
        {
            for( int i = 0; i < _eventPumps.length; i++ )
            {
                _eventPumps[ i ].refresh();
            }
        }

        synchronized( this )
        {
            _finished = true;
            notifyAll();
        }
    }
}
