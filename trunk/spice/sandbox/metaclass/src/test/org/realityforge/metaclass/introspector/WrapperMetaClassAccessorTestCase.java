/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.introspector;

import junit.framework.TestCase;
import com.mockobjects.dynamic.Mock;
import com.mockobjects.dynamic.C;
import com.mockobjects.dynamic.ConstraintMatcher;
import org.realityforge.metaclass.model.ClassDescriptor;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-10-28 10:43:28 $
 */
public class WrapperMetaClassAccessorTestCase
    extends TestCase
{
    public void testNullAccessorPassedToCtor()
        throws Exception
    {
        try
        {
            new WrapperMetaClassAccessor( null );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "npe.getMessage()", "accessor", npe.getMessage() );
            return;
        }
        fail( "Expected to fail due to null Accessor passed into Ctor" );
    }

    public void testWrapperMetaClassAccessor()
        throws Exception
    {
        final Mock mock = new Mock( MetaClassAccessor.class );
        final String classname = "X";
        final ClassLoader classLoader = getClass().getClassLoader();
        final ConstraintMatcher matcher =
            C.args( C.eq( classname ), C.eq( classLoader ), C.IS_NULL );
        mock.expectAndReturn( "getClassDescriptor", matcher, null );

        final MetaClassAccessor accessor = (MetaClassAccessor)mock.proxy();

        final WrapperMetaClassAccessor wrapper = new WrapperMetaClassAccessor( accessor );
        final ClassDescriptor result =
            wrapper.getClassDescriptor( classname, classLoader, null );
        assertEquals( "result", null, result );

        mock.verify();
    }
}
