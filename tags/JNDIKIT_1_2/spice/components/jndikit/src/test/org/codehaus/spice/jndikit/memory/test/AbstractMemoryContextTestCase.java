/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.jndikit.memory.test;

import javax.naming.NamingException;
import javax.naming.OperationNotSupportedException;

import junit.framework.AssertionFailedError;
import org.codehaus.spice.jndikit.StandardNamespace;
import org.codehaus.spice.jndikit.test.AbstractContextTestCase;

/**
 * Unit test for Memory context, using the {@link StandardNamespace}. ,
 *
 * @author Tim Anderson
 * @version $Revision: 1.1 $
 */
public abstract class AbstractMemoryContextTestCase
    extends AbstractContextTestCase
{

    public void testGetNameInNamespace() throws AssertionFailedError
    {
        try
        {
            String name = m_context.getNameInNamespace();
            fail( "Expected getNameInNamespace to throw OperationNotSupportedException but returned "
                  + name );
        }
        catch( final OperationNotSupportedException expected )
        {
        }
        catch( final NamingException ne )
        {
            throw new AssertionFailedError( ne.getMessage() );
        }
    }
}
