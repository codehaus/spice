/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.tools.qdox;

import junit.framework.TestCase;
import org.realityforge.metaclass.model.Attribute;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-10-29 08:27:02 $
 */
public class NonNamespaceAttributeRemovingInterceptorTestCase
    extends TestCase
{
    public void testProcessAttributeWithNamespacedAttribute()
        throws Exception
    {
        final NonNamespaceAttributeRemovingInterceptor interceptor =
            new NonNamespaceAttributeRemovingInterceptor();
        final Attribute attribute = new Attribute( "foo.baz" );
        final Attribute result = interceptor.processAttribute( attribute );
        assertEquals( "attribute", attribute, result );
    }

    public void testProcessAttributeWithNonNamespacedAttribute()
        throws Exception
    {
        final NonNamespaceAttributeRemovingInterceptor interceptor =
            new NonNamespaceAttributeRemovingInterceptor();
        final Attribute attribute = new Attribute( "baz" );
        final Attribute result = interceptor.processAttribute( attribute );
        assertEquals( "attribute", null, result );
    }

    public void testProcessAttributeWithNamespaceSeparatorAtStart()
        throws Exception
    {
        final NonNamespaceAttributeRemovingInterceptor interceptor =
            new NonNamespaceAttributeRemovingInterceptor();
        final Attribute attribute = new Attribute( ".baz" );
        final Attribute result = interceptor.processAttribute( attribute );
        assertEquals( "attribute", null, result );
    }

    public void testProcessAttributeWithNamespaceSeparatorAtEnd()
        throws Exception
    {
        final NonNamespaceAttributeRemovingInterceptor interceptor =
            new NonNamespaceAttributeRemovingInterceptor();
        final Attribute attribute = new Attribute( "baz." );
        final Attribute result = interceptor.processAttribute( attribute );
        assertEquals( "attribute", null, result );
    }
}
