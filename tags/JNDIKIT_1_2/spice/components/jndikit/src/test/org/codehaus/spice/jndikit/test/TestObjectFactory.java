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
import javax.naming.Reference;
import javax.naming.StringRefAddr;
import javax.naming.spi.ObjectFactory;


/**
 * Implementation of {@link ObjectFactory}, for testing purposes.
 *
 * @author <a href="mailto:tma@netspace.net.au">Tim Anderson</a>
 * @version $Revision: 1.1 $ $Date: 2005-06-30 04:22:16 $
 * @see TestDataReferenceable
 */
public class TestObjectFactory implements ObjectFactory
{

    /**
     * Creates an object using the location or reference information specified.
     *
     * @param obj         The possibly null object containing location or
     *                    reference information that can be used in creating an
     *                    object.
     * @param name        The name of this object relative to <code>nameCtx</code>,
     *                    or null if no name is specified.
     * @param nameCtx     The context relative to which the <code>name</code>
     *                    parameter is specified, or null if <code>name</code>
     *                    is relative to the default initial context.
     * @param environment The possibly null environment that is used in creating
     *                    the object.
     * @return The object created; null if an object cannot be created.
     * @throws Exception if this object factory encountered an exception while
     *                   attempting to create an object, and no other object
     *                   factories are to be tried.
     */
    public Object getObjectInstance( Object obj, Name name, Context nameCtx,
                                     Hashtable environment ) throws Exception
    {
        Object result = null;
        if( obj instanceof Reference )
        {
            Reference ref = ( Reference ) obj;
            String clazz = ref.getClassName();
            if( clazz.equals( TestDataReferenceable.class.getName() ) )
            {
                String value = null;
                StringRefAddr str = ( StringRefAddr ) ref.get( "value" );
                if( str != null )
                {
                    value = ( String ) str.getContent();
                }
                result = new TestDataReferenceable( value );
            }
            else if( clazz.equals( ExceptionReferenceable.class.getName() ) )
            {
                throw new Exception( "Encountered " + clazz );
            }
        }
        return result;
    }

}
