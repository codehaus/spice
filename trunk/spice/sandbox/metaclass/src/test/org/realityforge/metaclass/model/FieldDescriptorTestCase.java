/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.model;

import junit.framework.TestCase;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.2 $ $Date: 2003-10-28 13:34:22 $
 */
public class FieldDescriptorTestCase
    extends TestCase
{
    public void testFieldDescriptor()
        throws Exception
    {
        final FieldDescriptor descriptor =
            new FieldDescriptor( "x", "int", Attribute.EMPTY_SET, Attribute.EMPTY_SET );
        assertEquals( "name", "x", descriptor.getName() );
        assertEquals( "type", "int", descriptor.getType() );
    }

    public void testNullNamePassedToCtor()
        throws Exception
    {
        try
        {
            new FieldDescriptor( null, "int", Attribute.EMPTY_SET, Attribute.EMPTY_SET );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "npe.getMessage()", "name", npe.getMessage() );
            return;
        }
        fail( "Expected to fail due to null Name passed into Ctor" );
    }

    public void testNulltypePassedToCtor()
        throws Exception
    {
        try
        {
            new FieldDescriptor( "blah", null, Attribute.EMPTY_SET, Attribute.EMPTY_SET );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "npe.getMessage()", "type", npe.getMessage() );
            return;
        }
        fail( "Expected to fail due to null type passed into Ctor" );
    }
}
