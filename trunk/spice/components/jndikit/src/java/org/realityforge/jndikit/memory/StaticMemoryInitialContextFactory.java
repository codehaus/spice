/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.jndikit.memory;

import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;
import org.realityforge.jndikit.DefaultNameParser;
import org.realityforge.jndikit.DefaultNamespace;

/**
 * Initial context factory for memorycontext. This factory will
 * retrieve the {@link MemoryContext} from a static variable.
 * Thus this factory will always return the same instance of
 * memory context.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.2 $
 */
public class StaticMemoryInitialContextFactory
    implements InitialContextFactory
{
    private static final MemoryContext MEMORY_CONTEXT = createMemoryContext();

    public Context getInitialContext( final Hashtable environment )
        throws NamingException
    {
        return MEMORY_CONTEXT;
    }

    /**
     * Method to create the inital {@link MemoryContext}.
     *
     * @return the new {@link MemoryContext}.
     */
    private static final MemoryContext createMemoryContext()
    {
        final DefaultNamespace namespace = new DefaultNamespace( new DefaultNameParser() );
        return new MemoryContext( namespace,
                                  new Hashtable(),
                                  null );
    }
}

