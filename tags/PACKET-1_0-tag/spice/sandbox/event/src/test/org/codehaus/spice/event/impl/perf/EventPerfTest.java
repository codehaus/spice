package org.codehaus.spice.event.impl.perf;

import java.util.Random;
import org.codehaus.spice.event.EventSink;
import org.codehaus.spice.event.impl.DefaultEventQueue;
import org.codehaus.spice.event.impl.EventPump;
import org.codehaus.spice.event.impl.collections.AbstractFifoBuffer;
import org.codehaus.spice.event.impl.collections.BoundedFifoBuffer;
import org.codehaus.spice.event.impl.collections.UnboundedFifoBuffer;

/**
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2003-12-16 02:03:12 $
 */
public class EventPerfTest
{
    static final Random RANDOM = new Random();
    static boolean DEBUG = false;

    public static void main( final String[] args )
    {
        final AbstractFifoBuffer buffer1 = new BoundedFifoBuffer( 20 );
        final AbstractFifoBuffer buffer2 = new BoundedFifoBuffer( 20 );
        final AbstractFifoBuffer buffer3 = new BoundedFifoBuffer( 20 );
        final AbstractFifoBuffer buffer4 = new UnboundedFifoBuffer( 5 );

        final DefaultEventQueue queue1 = new TestQueue( "Q1", buffer1 );
        final DefaultEventQueue queue2 = new TestQueue( "Q2", buffer2 );
        final DefaultEventQueue queue3 = new TestQueue( "Q3", buffer3 );
        final DefaultEventQueue queue4 = new TestQueue( "Q4", buffer4 );

        final EventGenerator generator1 = new EventGenerator( "A" );
        final EventGenerator generator2 = new EventGenerator( "B" );

        final EventTerminator terminator1 = new EventTerminator( "Term1" );
        final EventTerminator terminator2 = new EventTerminator( "Term2" );

        final EventTransformer transformer1 =
            new EventTransformer( "T1", new EventSink[]{queue2, queue4}, "xT1" );

        final EventTransformer pass1 =
            new EventTransformer( "T2", new EventSink[]{queue1} );
        final EventTransformer pass2 =
            new EventTransformer( "T3", new EventSink[]{queue3} );
        final EventTransformer pass3 =
            new EventTransformer( "T4", new EventSink[]{terminator1} );
        final EventTransformer pass4 =
            new EventTransformer( "T5", new EventSink[]{terminator2} );

        final EventPump pump1 = new TestEventPump( generator1, pass1, "Pump1" );
        pump1.setBatchSize( 10 );

        final EventPump pump2 = new TestEventPump( generator2, pass1, "Pump2" );
        pump1.setBatchSize( 10 );

        final EventPump pump3 =
            new TestEventPump( queue1, transformer1, "Pump3" );
        pump3.setBatchSize( 10 );

        final EventPump pump4 = new TestEventPump( queue2, pass2, "Pump4" );
        pump4.setBatchSize( 4 );

        final EventPump pump5 = new TestEventPump( queue3, pass3, "Pump5" );
        pump5.setBatchSize( 2 );

        final EventPump pump6 = new TestEventPump( queue4, pass4, "Pump6" );
        pump6.setBatchSize( 7 );

        final EventPump[] pumps =
            new EventPump[]{pump1, pump2, pump3, pump4, pump5, pump6};
        //final EventPumpRunner runner1 = new EventPumpRunner( pumps );

        for( int i = 0; i < 100; i++ )
        {
            if( DEBUG )
            {
                System.out.println( "Tick Tock ......................" );
            }
            for( int j = 0; j < pumps.length; j++ )
            {
                final EventPump pump = pumps[ j ];
                refreshPump( pump, buffer1, buffer2, buffer3, buffer4 );
            }
        }
    }

    private static void refreshPump( final EventPump pump,
                                     final AbstractFifoBuffer buffer1,
                                     final AbstractFifoBuffer buffer2,
                                     final AbstractFifoBuffer buffer3,
                                     final AbstractFifoBuffer buffer4 )
    {
        pump.refresh();
        if( DEBUG )
        {
            System.out.println( "Q1.Size: " + buffer1.size() );
            System.out.println( "Q2.Size: " + buffer2.size() );
            System.out.println( "Q3.Size: " + buffer3.size() );
            System.out.println( "Q4.Size: " + buffer4.size() );
        }
    }
}
