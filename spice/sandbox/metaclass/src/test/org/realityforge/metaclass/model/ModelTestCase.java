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
 * @version $Revision: 1.12 $ $Date: 2003-10-28 11:38:06 $
 */
public class ModelTestCase
    extends TestCase
{
    public void testMethodWithNullName()
    {
        try
        {
            new MethodDescriptor( null, "type", ParameterDescriptor.EMPTY_SET, Attribute.EMPTY_SET );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "npe for name", "name", npe.getMessage() );
        }
    }

    public void testMethodWithNullType()
    {
        try
        {
            new MethodDescriptor( "name", null, ParameterDescriptor.EMPTY_SET, Attribute.EMPTY_SET );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "npe for returnType", "returnType", npe.getMessage() );
        }
    }

    public void testMethodWithNullParameters()
    {
        try
        {
            new MethodDescriptor( "name", "type", null, Attribute.EMPTY_SET );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "npe for parameters", "parameters", npe.getMessage() );
        }
    }

    public void testMethodWithNullInParameters()
    {
        try
        {
            new MethodDescriptor( "name", "type", new ParameterDescriptor[]{null}, Attribute.EMPTY_SET );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "npe for parameters[0]", "parameters[0]", npe.getMessage() );
        }
    }

    public void testMethodWithNullAttributes()
    {
        try
        {
            new MethodDescriptor( "name", "type", ParameterDescriptor.EMPTY_SET, null );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "npe for attributes", "attributes", npe.getMessage() );
        }
    }

    public void testMethodWithAttributesContainingNull()
    {
        try
        {
            new MethodDescriptor( "name", "type", ParameterDescriptor.EMPTY_SET, new Attribute[]{null} );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "npe for attributes[0]", "attributes[0]", npe.getMessage() );
        }
    }

    public void testMethod()
    {
        final MethodDescriptor method =
            new MethodDescriptor( "name", "type", ParameterDescriptor.EMPTY_SET, Attribute.EMPTY_SET );
        assertEquals( "method.name", "name", method.getName() );
        assertEquals( "method.type", "type", method.getReturnType() );
        assertEquals( "method.getParameters().length", 0, method.getParameters().length );
        assertEquals( "method.getAttributes().length", 0, method.getAttributes().length );
    }
}
