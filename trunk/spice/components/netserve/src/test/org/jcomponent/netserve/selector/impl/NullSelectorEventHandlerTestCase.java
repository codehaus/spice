package org.jcomponent.netserve.selector.impl;

import junit.framework.TestCase;

public class NullSelectorEventHandlerTestCase
    extends TestCase
{
    public void testNullSelectorEventHandler()
        throws Exception
    {
        NullSelectorEventHandler.HANDLER.handleSelectorEvent( null, null );
    }
}