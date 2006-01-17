/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.jndikit.memory.test;

import javax.naming.Context;

import org.codehaus.spice.jndikit.DefaultNameParser;
import org.codehaus.spice.jndikit.DefaultNamespace;
import org.codehaus.spice.jndikit.memory.MemoryContext;
import org.codehaus.spice.jndikit.test.TestObjectFactory;
import org.codehaus.spice.jndikit.test.TestStateFactory;

/**
 * Unit test for Memory context, using the {@link DefaultNamespace}.
 *
 * @author Tim Anderson
 * @version $Revision: 1.1 $
 */
public class MemoryContextDefaultNamespaceTestCase
    extends AbstractMemoryContextTestCase
{

    protected Context getRoot() throws Exception
    {
        final DefaultNameParser parser = new DefaultNameParser();
        final DefaultNamespace namespace = new DefaultNamespace( parser );
        namespace.addObjectFactory( new TestObjectFactory() );
        namespace.addStateFactory( new TestStateFactory() );
        return new MemoryContext( namespace, null, null );
    }
}
