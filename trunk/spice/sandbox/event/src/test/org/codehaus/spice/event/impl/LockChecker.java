package org.codehaus.spice.event.impl;

/**
 * @author Peter Donald
 * @version $Revision: 1.2 $ $Date: 2004-05-12 07:22:48 $
 */
class LockChecker
    implements Runnable
{
    private final Object _lock;
    private boolean _unlocked;
    private boolean _started;

    LockChecker( final Object lock )
    {
        _lock = lock;
        final Thread thread = new Thread( this );
        thread.start();
        while( !_started )
        {
            Thread.yield();
        }
    }

    public void run()
    {
        synchronized( _lock )
        {
            _started = true;
            try
            {
                _lock.wait();
                _unlocked = true;
            }
            catch( InterruptedException e )
            {
            }
        }
    }

    boolean isUnlocked()
    {
        try
        {
            Thread.sleep( 1 );
        }
        catch( InterruptedException e )
        {
        }
        return _unlocked;
    }
}
