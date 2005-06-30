/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.jndikit.test;

import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.spi.DirectoryManager;
import javax.naming.spi.NamingManager;
import javax.naming.spi.StateFactory;


/**
 * Implementation of  {@link StateFactory} for testing purposes.
 *
 * @author Tim Anderson
 * @version $Revision: 1.1 $ $Date: 2005-06-30 04:22:16 $
 */
public class TestStateFactory implements StateFactory
{

    /**
     * Retrieves the state of an object for binding.
     *
     * @param obj         A non-null object whose state is to be retrieved.
     * @param name        The name of this object relative to <code>nameCtx</code>,
     *                    or null if no name is specified.
     * @param nameCtx     The context relative to which the <code>name</code>
     *                    parameter is specified, or null if <code>name</code>
     *                    is relative to the default initial context.
     * @param environment The possibly null environment to be used in the
     *                    creation of the object's state.
     * @return The object's state for binding; null if the factory is not
     *         returning any changes.
     * @throws NamingException if this factory encountered an exception while
     *                         attempting to get the object's state, and no
     *                         other factories are to be tried.
     * @see NamingManager#getStateToBind
     * @see DirectoryManager#getStateToBind
     */
    public Object getStateToBind( Object obj, Name name, Context nameCtx,
                                  Hashtable environment )
        throws NamingException
    {
        Object result = null;
        if( obj instanceof TestData )
        {
            TestData data = ( TestData ) obj;
            TestDataReferenceable ref = new TestDataReferenceable(
                data.getValue() );
            result = ref.getReference();
        }
        return result;
    }
}
