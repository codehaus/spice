package org.realityforge.sca.connector;

import junit.framework.TestCase;

public class ActivePingerTestCase
    extends TestCase
{
    public void testActivePingerPassedNullInCtor()
        throws Exception
    {
        try
        {
            new ActivePinger( null );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "npe.getMessage()", "connector", npe.getMessage() );
        }
    }

    public void testActivePingerUsingNeverPingPolicy()
        throws Exception
    {
        final long now = System.currentTimeMillis();
        final MockConnector connector = new MockConnector( 0, 0, now );
        final ActivePinger pinger = new ActivePinger( connector );
        final Thread thread = new Thread( pinger );
        thread.start();
        while( !pinger.hasStarted() )
        {
            Thread.sleep( 10 );
        }

        //An extra sleep so that it can actually call ping()
        Thread.sleep( 40 );

        //Join pinger again
        pinger.deactivate();
        thread.join();

        assertEquals( "pingCount", 0, connector.getPingCount() );
    }

    public void testActivePingerUsingPeriodicPingPolicy()
        throws Exception
    {
        final long now = System.currentTimeMillis();
        final MockConnector connector = new MockConnector( 0, 0, now );
        final PeriodicPingPolicy policy = new PeriodicPingPolicy( 2, connector );
        connector.setPingPolicy( policy );
        final ActivePinger pinger = new ActivePinger( connector );
        final Thread thread = new Thread( pinger );
        thread.start();
        while( !pinger.hasStarted() )
        {
            Thread.sleep( 10 );
        }

        //An extra sleep so that it can actually call ping()
        Thread.sleep( 40 );

        //Join pinger again
        pinger.deactivate();
        thread.join();

        assertTrue( "pingCount", 0 < connector.getPingCount() );
    }
}
