package org.realityforge.packet.event.impl.perf;

import java.util.Arrays;
import org.realityforge.packet.event.EventHandler;
import org.realityforge.packet.event.EventSink;

/**
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2003-12-10 02:07:18 $
 */
public class EventTransformer
    implements EventHandler
{
    private final String _name;
    private final EventSink[] _sinks;
    private final String _postfix;

    public EventTransformer( final String name, final EventSink[] sinks )
    {
        this( name, sinks, "" );
    }

    public EventTransformer( final String name,
                             final EventSink[] sinks,
                             final String postfix )
    {
        _name = name;
        _sinks = sinks;
        _postfix = postfix;
    }

    public void handleEvent( final Object event )
    {
        final Object newEvent = transformEvent( event );
        for( int i = 0; i < _sinks.length; i++ )
        {
            final EventSink sink = _sinks[ i ];
            final boolean result = sink.addEvent( newEvent );
            if( result )
            {
                return;
            }
        }
        if( EventPerfTest.DEBUG )
        {
            System.out.println(
                "Dropping Event " + event +
                " at " + _name );
        }
    }

    public void handleEvents( final Object[] events )
    {
        final Object[] newEvents = new Object[ events.length ];
        for( int i = 0; i < newEvents.length; i++ )
        {
            newEvents[ i ] = transformEvent( events[ i ] );
        }

        for( int i = 0; i < _sinks.length; i++ )
        {
            final EventSink sink = _sinks[ i ];
            final boolean result = sink.addEvents( events );
            if( result )
            {
                if( EventPerfTest.DEBUG )
                {
                    final String message =
                        events.length + " events transformed via " + _name +
                        " and passed to sink " + sink;
                    System.out.println( message );
                }
                return;
            }
        }
        if( true || EventPerfTest.DEBUG )
        {
            System.out.println( "Dropping Events " + Arrays.asList( events ) +
                                " at " + _name );
        }
    }

    private Object transformEvent( final Object event )
    {
        return event + _postfix;
    }
}
