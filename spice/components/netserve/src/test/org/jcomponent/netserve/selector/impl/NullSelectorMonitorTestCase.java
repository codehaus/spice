package org.jcomponent.netserve.selector.impl;

import junit.framework.TestCase;

public class NullSelectorMonitorTestCase
    extends TestCase
{
    public void testNullSelectorEventHandler()
        throws Exception
    {
        final NullSelectorMonitor monitor = new NullSelectorMonitor();
        monitor.enteringSelect();
        monitor.enteringSelectorLoop();
        monitor.errorClosingSelector( null );
        monitor.exitingSelectorLoop();
        monitor.handlingSelectEvent( null );
        monitor.selectorStartup();
        monitor.selectorShutdown();
        monitor.selectCompleted( 2 );
        monitor.invalidAttachment( null );
    }
}
