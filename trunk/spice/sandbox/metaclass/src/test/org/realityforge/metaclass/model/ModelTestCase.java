/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.model;

import java.util.Properties;
import junit.framework.TestCase;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.7 $ $Date: 2003-10-04 00:47:49 $
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

    public void testClassWithNullAttributes()
    {
        try
        {
            new ClassDescriptor( "name", 0, null, FieldDescriptor.EMPTY_SET, MethodDescriptor.EMPTY_SET );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "npe for attributes", "attributes", npe.getMessage() );
        }
    }

    public void testClassWithAttributesContainingNull()
    {
        try
        {
            new ClassDescriptor( "name", 0, new Attribute[]{null}, FieldDescriptor.EMPTY_SET, MethodDescriptor.EMPTY_SET );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "npe for attributes[0]", "attributes[0]", npe.getMessage() );
        }
    }

    public void testClassWithAttributesNullClassname()
    {
        try
        {
            new ClassDescriptor( null, 0, Attribute.EMPTY_SET, FieldDescriptor.EMPTY_SET, MethodDescriptor.EMPTY_SET );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "npe for classname", "classname", npe.getMessage() );
        }
    }

    public void testClassWithNullFields()
    {
        try
        {
            new ClassDescriptor( "name", 0, Attribute.EMPTY_SET, null, MethodDescriptor.EMPTY_SET );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "npe for fields", "fields", npe.getMessage() );
        }
    }

    public void testClassWithFieldsContainingNull()
    {
        try
        {
            new ClassDescriptor( "name", 0, Attribute.EMPTY_SET, new FieldDescriptor[]{null}, MethodDescriptor.EMPTY_SET );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "npe for fields[0]", "fields[0]", npe.getMessage() );
        }
    }

    public void testClassWithNullMethods()
    {
        try
        {
            new ClassDescriptor( "name", 0, Attribute.EMPTY_SET, FieldDescriptor.EMPTY_SET, null );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "npe for methods", "methods", npe.getMessage() );
        }
    }

    public void testClassWithMethodsContainingNull()
    {
        try
        {
            new ClassDescriptor( "name", 0, Attribute.EMPTY_SET, FieldDescriptor.EMPTY_SET, new MethodDescriptor[]{null} );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "npe for methods[0]", "methods[0]", npe.getMessage() );
        }
    }

    public void testClassDescriptor()
    {
        final ClassDescriptor descriptor =
            new ClassDescriptor( "name",
                                 0,
                                 Attribute.EMPTY_SET,
                                 FieldDescriptor.EMPTY_SET,
                                 MethodDescriptor.EMPTY_SET );
        assertEquals( "descriptor.name", "name", descriptor.getName() );
        assertEquals( "descriptor.modifiers", 0, descriptor.getModifiers() );
        assertEquals( "descriptor.getFields().length", 0, descriptor.getFields().length );
        assertEquals( "descriptor.getMethods().length", 0, descriptor.getMethods().length );
        assertEquals( "descriptor.getAttributes().length", 0, descriptor.getAttributes().length );
    }
}
