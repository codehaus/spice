package org.realityforge.sca.selector.impl;

import junit.framework.TestCase;
import org.realityforge.sca.selector.impl.NullSelectorEventHandler;

public class NullSelectorEventHandlerTestCase
    extends TestCase
{
    public void testNullSelectorEventHandler()
        throws Exception
    {
        NullSelectorEventHandler.HANDLER.handleSelectorEvent( null, null );
    }
}
