/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.jndikit.memory.test;

import java.util.Hashtable;
import javax.naming.Context;
import org.codehaus.spice.jndikit.memory.MemoryInitialContextFactory;
import org.codehaus.spice.jndikit.test.AbstractContextTestCase;

/**
 * Unit test for Memory context
 *
 * @author Peter Donald
 * @version $Revision: 1.1 $
 */
 public class MemoryContextTestCase
    extends AbstractContextTestCase
{
    /**
     * Contter used to create contexts for tests.
     */
    private static int c_id = 0;

    protected void setUp() throws Exception
    {
        final MemoryInitialContextFactory factory = new MemoryInitialContextFactory();
        final Context root = factory.getInitialContext( new Hashtable() );
        setRoot( root );
        setContext( root.createSubcontext( "test" + c_id++ ) );
    }
}
