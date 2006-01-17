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

import org.codehaus.spice.jndikit.DefaultNameParser;
import org.codehaus.spice.jndikit.Namespace;
import org.codehaus.spice.jndikit.StandardNamespace;
import org.codehaus.spice.jndikit.memory.MemoryContext;
import org.codehaus.spice.jndikit.test.TestStateFactory;

/**
 * Unit test for Memory context, using the {@link StandardNamespace}. ,
 *
 * @author Peter Donald
 * @author Tim Anderson
 * @version $Revision: 1.2 $
 */
public class MemoryContextTestCase
    extends AbstractMemoryContextTestCase
{

    protected Context getRoot() throws Exception
    {
        final DefaultNameParser parser = new DefaultNameParser();
        final Namespace namespace = new StandardNamespace( parser );

        Hashtable environment = new Hashtable();
        environment.put( Context.STATE_FACTORIES,
                         TestStateFactory.class.getName() );

        return new MemoryContext( namespace, environment, null );
    }
}
