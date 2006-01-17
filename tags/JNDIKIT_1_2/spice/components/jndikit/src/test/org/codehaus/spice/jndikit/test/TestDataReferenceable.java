/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.jndikit.test;

import java.io.IOException;
import javax.naming.Reference;
import javax.naming.Referenceable;
import javax.naming.StringRefAddr;


/**
 * Implementation of {@link Referenceable}, for testing purposes.
 *
 * @author <a href="mailto:tma@netspace.net.au">Tim Anderson</a>
 * @version $Revision: 1.1 $ $Date: 2005-06-30 04:22:16 $
 * @see TestObjectFactory
 */
public class TestDataReferenceable extends TestData implements Referenceable
{


    /**
     * Default ctor for serialization.
     */
    public TestDataReferenceable()
    {
    }

    /**
     * Construct a new <code>TestDataReferenceable</code>.
     *
     * @param value test data
     */
    public TestDataReferenceable( String value )
    {
        super( value );
    }

    /**
     * Retrieves the Reference of this object.
     *
     * @return the non-null Reference of this object.
     */
    public Reference getReference()
    {
        return new Reference( getClass().getName(),
                              new StringRefAddr( "value", getValue() ),
                              TestObjectFactory.class.getName(),
                              null );
    }

    private void writeObject( java.io.ObjectOutputStream out )
        throws IOException
    {
        throw new IOException( "Cannot serialize. Use getReference() instead" );
    }

    private void readObject( java.io.ObjectInputStream in )
        throws IOException
    {
        throw new IOException( "Cannot serialize. Use getReference() instead" );
    }

}
