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
public class SelectorEntryTestCase
    extends TestCase
{
    public void testSelectorEntry()
        throws Exception
    {
        final HelloSelectorEventHandler handler = new HelloSelectorEventHandler();
        final Object userData = new Object();
        final SelectorEntry entry =
            new SelectorEntry( handler, userData );
        assertEquals( "handler", handler, entry.getHandler() );
        assertEquals( "userData", userData, entry.getUserData() );
    }

    public void testNullHandlerInSelectorEntryCtor()
        throws Exception
    {
        try
        {
            new SelectorEntry( null, null );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "npe.getMessage()", "handler", npe.getMessage() );
            return;
        }
        fail( "Expected to fail due to passing null handler into ctor" );
    }
}
