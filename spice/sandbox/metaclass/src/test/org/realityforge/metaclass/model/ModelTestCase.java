/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.model;

import junit.framework.TestCase;
import java.util.Properties;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.3 $ $Date: 2003-08-16 00:44:42 $
 */
public class ModelTestCase
    extends TestCase
{
    public void testAttributeOnlyWithName()
    {
        final Attribute attribute = new Attribute( "dna.service" );
        assertEquals( "attribute.getName() == dna.service",
                      "dna.service", attribute.getName() );
        assertEquals( "attribute.getValue() == null",
                      null, attribute.getValue() );
        assertEquals( "attribute.getParameterCount() == 0",
                      0, attribute.getParameterCount() );
        assertEquals( "attribute.getParameterNames().length == 0",
                      0, attribute.getParameterNames().length );
        assertEquals( "attribute.getParameter('key') == null",
                      null, attribute.getParameter( "key" ) );
        assertEquals( "attribute.getParameter('dummy','foo') == foo",
                      "foo", attribute.getParameter( "key", "foo" ) );
    }

    public void testAttributeWithNameAndValue()
    {
        final Attribute attribute = new Attribute( "dna.service", "blah" );
        assertEquals( "attribute.getName() == dna.service",
                      "dna.service", attribute.getName() );
        assertEquals( "attribute.getValue() == blah",
                      "blah", attribute.getValue() );
        assertEquals( "attribute.getParameterCount() == 0",
                      0, attribute.getParameterCount() );
        assertEquals( "attribute.getParameterNames().length == 0",
                      0, attribute.getParameterNames().length );
        assertEquals( "attribute.getParameter('key') == null",
                      null, attribute.getParameter( "key" ) );
        assertEquals( "attribute.getParameter('dummy','foo') == foo",
                      "foo", attribute.getParameter( "key", "foo" ) );
    }

    public void testAttributeWithNameAndParameters()
    {
        final Properties parameters = new Properties();
        parameters.setProperty( "key", "value" );
        final Attribute attribute = new Attribute( "dna.service", parameters );
        assertEquals( "attribute.getName() == dna.service",
                      "dna.service", attribute.getName() );
        assertEquals( "attribute.getValue() == null",
                      null, attribute.getValue() );
        assertEquals( "attribute.getParameterCount() == 1",
                      1, attribute.getParameterCount() );
        assertEquals( "attribute.getParameterNames().length == 1",
                      1, attribute.getParameterNames().length );
        assertEquals( "attribute.getParameter('key') == value",
                      "value", attribute.getParameter( "key" ) );
        assertEquals( "attribute.getParameter('dummy','foo') == value",
                      "value", attribute.getParameter( "key", "foo" ) );
    }

    public void testAttributeWithNullName()
    {
        try
        {
            new Attribute( null );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "npe for name", "name", npe.getMessage() );
        }
    }

    public void testParameterWithNullName()
    {
        try
        {
            new ParameterDescriptor( null, "type" );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "npe for name", "name", npe.getMessage() );
        }
    }

    public void testParameterWithNullType()
    {
        try
        {
            new ParameterDescriptor( "name", null );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "npe for type", "type", npe.getMessage() );
        }
    }

    public void testParameter()
    {
        final ParameterDescriptor param = new ParameterDescriptor( "paramName", "paramType" );
        assertEquals( "param.getName() == paramName",
                      "paramName", param.getName() );
        assertEquals( "param.getType() == paramType",
                      "paramType", param.getType() );
    }

    public void testFieldWithNullName()
    {
        try
        {
            new FieldDescriptor( null, "type", 0, Attribute.EMPTY_SET );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "npe for name", "name", npe.getMessage() );
        }
    }

    public void testFieldWithNullType()
    {
        try
        {
            new FieldDescriptor( "name", null, 0, Attribute.EMPTY_SET );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "npe for type", "type", npe.getMessage() );
        }
    }

    public void testFieldWithNullAttributes()
    {
        try
        {
            new FieldDescriptor( "name", "type", 0, null );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "npe for attributes", "attributes", npe.getMessage() );
        }
    }

    public void testFieldWithAttributesContainingNull()
    {
        try
        {
            new FieldDescriptor( "name", "type", 0, new Attribute[]{null} );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "npe for attributes[0]", "attributes[0]", npe.getMessage() );
        }
    }

    public void testField()
    {
        final FieldDescriptor field =
            new FieldDescriptor( "name", "type", 0, Attribute.EMPTY_SET );
        assertEquals( "field.name", "name", field.getName() );
        assertEquals( "field.type", "type", field.getType() );
        assertEquals( "field.modifiers", 0, field.getModifiers() );
        assertEquals( "field.getAttributes().length", 0, field.getAttributes().length );
    }


    public void testMethodWithNullName()
    {
        try
        {
            new MethodDescriptor( null, "type", 0, ParameterDescriptor.EMPTY_SET, Attribute.EMPTY_SET );
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
            new MethodDescriptor( "name", null, 0, ParameterDescriptor.EMPTY_SET, Attribute.EMPTY_SET );
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
            new MethodDescriptor( "name", "type", 0, null, Attribute.EMPTY_SET );
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
            new MethodDescriptor( "name", "type", 0, new ParameterDescriptor[]{null}, Attribute.EMPTY_SET );
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
            new MethodDescriptor( "name", "type", 0, ParameterDescriptor.EMPTY_SET, null );
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
            new MethodDescriptor( "name", "type", 0, ParameterDescriptor.EMPTY_SET, new Attribute[]{null} );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "npe for attributes[0]", "attributes[0]", npe.getMessage() );
        }
    }

    public void testMethod()
    {
        final MethodDescriptor method =
            new MethodDescriptor( "name", "type", 0, ParameterDescriptor.EMPTY_SET, Attribute.EMPTY_SET );
        assertEquals( "method.name", "name", method.getName() );
        assertEquals( "method.type", "type", method.getReturnType() );
        assertEquals( "method.modifiers", 0, method.getModifiers() );
        assertEquals( "method.getParameters().length", 0, method.getParameters().length );
        assertEquals( "method.getAttributes().length", 0, method.getAttributes().length );
    }
}
