/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.tools.tasks;

import junit.framework.TestCase;
import com.thoughtworks.qdox.model.JavaClass;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-10-04 01:19:17 $
 */
public class MulticastJavaClassFilterTestCase
    extends TestCase
{
    public void testBasicFiltering()
        throws Exception
    {
        final MulticastJavaClassFilter baseFilter =
            new MulticastJavaClassFilter( new JavaClassFilter[ 0 ] );
        final MulticastJavaClassFilter filter =
            new MulticastJavaClassFilter( new JavaClassFilter[]{baseFilter} );
        final JavaClass result1 = filter.filterClass( null );
        final JavaClass javaClass = new JavaClass();
        final JavaClass result2 = filter.filterClass( javaClass );
        assertEquals( "filter(null)", null, result1 );
        assertEquals( "filter(javaClass)", javaClass, result2 );
    }

    public void testCtorWithNullArg()
        throws Exception
    {
        try
        {
            new MulticastJavaClassFilter( null );
        }
        catch( NullPointerException npe )
        {
            assertEquals( "npe.message", "filters", npe.getMessage() );
            return;
        }
        fail( "Expected to fail with NullPOinter in ctor" );
    }

    public void testCtorWithNullContainedInArg()
        throws Exception
    {
        try
        {
            new MulticastJavaClassFilter( new JavaClassFilter[]{null} );
        }
        catch( NullPointerException npe )
        {
            assertEquals( "npe.message", "filters[0]", npe.getMessage() );
            return;
        }
        fail( "Expected to fail with NullPOinter in ctor" );
    }
}
