package org.codehaus.spice.timeevent;

import org.codehaus.spice.event.impl.DefaultEventQueue;
import org.codehaus.spice.event.impl.EventPump;
import org.codehaus.spice.event.impl.collections.UnboundedFifoBuffer;
import org.codehaus.spice.timeevent.source.TimeEventSource;
import org.codehaus.spice.timeevent.triggers.PeriodicTimeTrigger;

/**
 * @author Peter Donald
 * @version $Revision: 1.2 $ $Date: 2004-02-23 04:48:38 $
 */
public class TestServer
{
    private static boolean c_done;
    private static TimeEventSource c_source;

    public static void main( final String[] args )
        throws Exception
    {
        final EventPump pump = createPumps();
        final Runnable runnable = new Runnable()
        {
            public void run()
            {
                doPump( pump );
            }
        };
        final Thread thread = new Thread( runnable );
        thread.start();

        c_source.addTrigger( new PeriodicTimeTrigger( 0, 30 ),
                             "Meep!" );
        while( !c_done )
        {
            Thread.sleep( 50 );
        }
        System.exit( 1 );
    }

    private static EventPump createPumps()
    {
        final DefaultEventQueue queue1 =
            new DefaultEventQueue( new UnboundedFifoBuffer( 15 ) );

        c_source = new TimeEventSource( queue1 );
        final TestEventHandler handler = new TestEventHandler();

        return new EventPump( c_source, handler );
    }

    private static void doPump( final EventPump pump )
    {
        for( int i = 0; i < 1000; i++ )
        {
            pump.refresh();
            try
            {
                Thread.sleep( 2 );
            }
            catch( InterruptedException e )
            {
                e.printStackTrace();
            }
        }
        c_done = true;
    }
}
