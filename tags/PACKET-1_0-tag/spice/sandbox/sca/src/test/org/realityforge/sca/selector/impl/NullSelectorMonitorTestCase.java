/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.sca.selector.impl;

import junit.framework.TestCase;

/**
 * @author Peter Donald
 * @version $Revision: 1.3 $ $Date: 2003-12-05 05:39:34 $
 */
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
