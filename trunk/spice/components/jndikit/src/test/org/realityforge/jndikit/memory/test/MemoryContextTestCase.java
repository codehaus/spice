/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 * This product includes software developed by the
�*�Apache Software Foundation (http://www.apache.org/).
 */
package org.realityforge.jndikit.memory.test;

import java.util.Hashtable;
import javax.naming.Context;
import org.realityforge.jndikit.memory.MemoryInitialContextFactory;
import org.realityforge.jndikit.test.AbstractContextTestCase;

/**
 * Unit testing for Memory system
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.3 $
 */
public class MemoryContextTestCase extends AbstractContextTestCase
{
    private int m_id = 0;
    private Context m_rootContext;

    public MemoryContextTestCase( String name )
    {
        super( name );
    }

    protected void setUp()
    {
        try
        {
            final MemoryInitialContextFactory factory = new MemoryInitialContextFactory();
            m_rootContext = factory.getInitialContext( new Hashtable() );
            m_context = m_rootContext.createSubcontext( "test" + m_id++ );
        }
        catch( Exception e )
        {
        }
    }

    protected void tearDown()
    {
        try
        {
            m_context.close();
            m_context = null;
            m_rootContext.close();
        }
        catch( Exception e )
        {
        }
    }
}
