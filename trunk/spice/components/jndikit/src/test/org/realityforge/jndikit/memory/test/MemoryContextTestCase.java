/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.jndikit.memory.test;

import java.util.Hashtable;
import javax.naming.Context;
import org.realityforge.jndikit.memory.MemoryInitialContextFactory;
import org.realityforge.jndikit.test.AbstractContextTestCase;

public class MemoryContextTestCase
    extends AbstractContextTestCase
{
    /**
     * Contter used to create contexts for tests.
     */
    private static int c_id = 0;

    /**
     * The root context.
     */
    private Context m_root;

    public MemoryContextTestCase( final String name )
    {
        super( name );
    }

    protected void setUp() throws Exception
    {
        final MemoryInitialContextFactory factory = new MemoryInitialContextFactory();
        m_root = factory.getInitialContext( new Hashtable() );
        m_context = m_root.createSubcontext( "test" + c_id++ );
    }

    protected void tearDown() throws Exception
    {
        super.tearDown();
        m_root.close();
    }
}
