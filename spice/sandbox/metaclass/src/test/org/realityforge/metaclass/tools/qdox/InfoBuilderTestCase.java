/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.tools.qdox;

import junit.framework.TestCase;
import java.util.Properties;
import java.util.ArrayList;
import java.lang.reflect.Modifier;
import com.thoughtworks.qdox.model.DocletTag;
import com.thoughtworks.qdox.model.JavaField;
import com.thoughtworks.qdox.model.Type;
import com.thoughtworks.qdox.model.JavaParameter;
import com.thoughtworks.qdox.model.JavaMethod;
import org.realityforge.metaclass.model.Attribute;
import org.realityforge.metaclass.model.FieldDescriptor;
import org.realityforge.metaclass.model.ParameterDescriptor;
import org.realityforge.metaclass.model.MethodDescriptor;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.7 $ $Date: 2003-08-22 06:11:58 $
 */
public class InfoBuilderTestCase
    extends TestCase
{
    public void testParseValueIntoParametersWithSimpleString()
        throws Exception
    {
        final String value = "key1";
        final QDoxDescriptorParser parser = new QDoxDescriptorParser();
        final Properties parameters = parser.parseValueIntoParameters( value );
        assertNull( "parameters", parameters );
    }

    public void testParseValueIntoParametersWithSimpleStringAndEquals()
        throws Exception
    {
        final String value = "key1=";
        final QDoxDescriptorParser parser = new QDoxDescriptorParser();
        final Properties parameters = parser.parseValueIntoParameters( value );
        assertNull( "parameters", parameters );
    }

    public void testParseValueIntoParametersWithSimpleStringAndValueStart()
        throws Exception
    {
        final String value = "key1=\"";
        final QDoxDescriptorParser parser = new QDoxDescriptorParser();
        final Properties parameters = parser.parseValueIntoParameters( value );
        assertNull( "parameters", parameters );
    }

    public void testParseValueIntoParametersWithSimpleStringAndValueUnEnded()
        throws Exception
    {
        final String value = "key1=\"value1";
        final QDoxDescriptorParser parser = new QDoxDescriptorParser();
        final Properties parameters = parser.parseValueIntoParameters( value );
        assertNull( "parameters", parameters );
    }

    public void testParseValueIntoParametersWithSimpleStringAndValidStart()
        throws Exception
    {
        final String value = "key1=\"value1\" key2";
        final QDoxDescriptorParser parser = new QDoxDescriptorParser();
        final Properties parameters = parser.parseValueIntoParameters( value );
        assertNull( "parameters", parameters );
    }

    public void testParseValueIntoParametersWithSimpleStringAndEqualsAndValidStart()
        throws Exception
    {
        final String value = "key1=\"value1\" key2=";
        final QDoxDescriptorParser parser = new QDoxDescriptorParser();
        final Properties parameters = parser.parseValueIntoParameters( value );
        assertNull( "parameters", parameters );
    }

    public void testParseValueIntoParametersWithSimpleStringAndValueStartAndValidStart()
        throws Exception
    {
        final String value = "key1=\"value1\" key2=\"";
        final QDoxDescriptorParser parser = new QDoxDescriptorParser();
        final Properties parameters = parser.parseValueIntoParameters( value );
        assertNull( "parameters", parameters );
    }

    public void testParseValueIntoParametersWithSimpleStringAndValueUnEndedAndValidStart()
        throws Exception
    {
        final String value = "key1=\"value1\" key2=\"value1";
        final QDoxDescriptorParser parser = new QDoxDescriptorParser();
        final Properties parameters = parser.parseValueIntoParameters( value );
        assertNull( "parameters", parameters );
    }

    public void testParseValueIntoParametersWithSingleKey()
        throws Exception
    {
        final String value = "key1=\"value1\"";
        final QDoxDescriptorParser parser = new QDoxDescriptorParser();
        final Properties parameters = parser.parseValueIntoParameters( value );
        assertNotNull( "parameters", parameters );
        assertEquals( "parameters.lenght", 1, parameters.size() );
        assertEquals( "parameters.get('key')",
                      "value1",
                      parameters.getProperty( "key1" ) );
    }

    public void testParseValueIntoParametersWithSingleKeySurroundedByWhitespace()
        throws Exception
    {
        final String value = " \t \n key1=\"value1\"   \n\t";
        final QDoxDescriptorParser parser = new QDoxDescriptorParser();
        final Properties parameters = parser.parseValueIntoParameters( value );
        assertNotNull( "parameters", parameters );
        assertEquals( "parameters.lenght", 1, parameters.size() );
        assertEquals( "parameters.get('key')",
                      "value1",
                      parameters.getProperty( "key1" ) );
    }

    public void testParseValueIntoParametersWithMultipleKey()
        throws Exception
    {
        final String value = "key1=\"value1\" key2=\"value2\"";
        final QDoxDescriptorParser parser = new QDoxDescriptorParser();
        final Properties parameters = parser.parseValueIntoParameters( value );
        assertNotNull( "parameters", parameters );
        assertEquals( "parameters.lenght", 2, parameters.size() );
        assertEquals( "parameters.get('key1')",
                      "value1",
                      parameters.getProperty( "key1" ) );
        assertEquals( "parameters.get('key2')",
                      "value2",
                      parameters.getProperty( "key2" ) );
    }

    public void testParseValueIntoParametersWithMultipleKeySurroundedByWhitespace()
        throws Exception
    {
        final String value = " \t \n key1=\"value1\"   \n key2=\"value2\"\t";
        final QDoxDescriptorParser parser = new QDoxDescriptorParser();
        final Properties parameters = parser.parseValueIntoParameters( value );
        assertNotNull( "parameters", parameters );
        assertEquals( "parameters.lenght", 2, parameters.size() );
        assertEquals( "parameters.get('key1')",
                      "value1",
                      parameters.getProperty( "key1" ) );
        assertEquals( "parameters.get('key2')",
                      "value2",
                      parameters.getProperty( "key2" ) );
    }

    public void testParseValueIntoParametersWithMultipleKeySurroundedByZeroWhitespace()
        throws Exception
    {
        final String value = "key1=\"value1\"key2=\"value2\"";
        final QDoxDescriptorParser parser = new QDoxDescriptorParser();
        final Properties parameters = parser.parseValueIntoParameters( value );
        assertNull( "parameters", parameters );
    }

    public void testParseEmptyModifiers()
        throws Exception
    {
        final String[] qualifiers = new String[]{};
        final QDoxDescriptorParser parser = new QDoxDescriptorParser();
        final int modifiers = parser.parseModifiers( qualifiers );
        assertEquals( "modifiers", 0, modifiers );
    }

    public void testParseModifier_public()
        throws Exception
    {
        final String[] qualifiers = new String[]{"public"};
        final QDoxDescriptorParser parser = new QDoxDescriptorParser();
        final int modifiers = parser.parseModifiers( qualifiers );
        assertEquals( "modifiers", Modifier.PUBLIC, modifiers );
    }

    public void testParseModifier_protected()
        throws Exception
    {
        final String[] qualifiers = new String[]{"protected"};
        final QDoxDescriptorParser parser = new QDoxDescriptorParser();
        final int modifiers = parser.parseModifiers( qualifiers );
        assertEquals( "modifiers", Modifier.PROTECTED, modifiers );
    }

    public void testParseModifier_private()
        throws Exception
    {
        final String[] qualifiers = new String[]{"private"};
        final QDoxDescriptorParser parser = new QDoxDescriptorParser();
        final int modifiers = parser.parseModifiers( qualifiers );
        assertEquals( "modifiers", Modifier.PRIVATE, modifiers );
    }

    public void testParseModifier_static()
        throws Exception
    {
        final String[] qualifiers = new String[]{"static"};
        final QDoxDescriptorParser parser = new QDoxDescriptorParser();
        final int modifiers = parser.parseModifiers( qualifiers );
        assertEquals( "modifiers", Modifier.STATIC, modifiers );
    }

    public void testParseModifier_strict()
        throws Exception
    {
        final String[] qualifiers = new String[]{"strictfp"};
        final QDoxDescriptorParser parser = new QDoxDescriptorParser();
        final int modifiers = parser.parseModifiers( qualifiers );
        assertEquals( "modifiers", Modifier.STRICT, modifiers );
    }

    public void testParseModifier_interface()
        throws Exception
    {
        final String[] qualifiers = new String[]{"interface"};
        final QDoxDescriptorParser parser = new QDoxDescriptorParser();
        final int modifiers = parser.parseModifiers( qualifiers );
        assertEquals( "modifiers", Modifier.INTERFACE, modifiers );
    }

    public void testParseModifier_synchronized()
        throws Exception
    {
        final String[] qualifiers = new String[]{"synchronized"};
        final QDoxDescriptorParser parser = new QDoxDescriptorParser();
        final int modifiers = parser.parseModifiers( qualifiers );
        assertEquals( "modifiers", Modifier.SYNCHRONIZED, modifiers );
    }

    public void testParseModifier_native()
        throws Exception
    {
        final String[] qualifiers = new String[]{"native"};
        final QDoxDescriptorParser parser = new QDoxDescriptorParser();
        final int modifiers = parser.parseModifiers( qualifiers );
        assertEquals( "modifiers", Modifier.NATIVE, modifiers );
    }

    public void testParseModifier_transient()
        throws Exception
    {
        final String[] qualifiers = new String[]{"transient"};
        final QDoxDescriptorParser parser = new QDoxDescriptorParser();
        final int modifiers = parser.parseModifiers( qualifiers );
        assertEquals( "modifiers", Modifier.TRANSIENT, modifiers );
    }

    public void testParseModifier_volatile()
        throws Exception
    {
        final String[] qualifiers = new String[]{"volatile"};
        final QDoxDescriptorParser parser = new QDoxDescriptorParser();
        final int modifiers = parser.parseModifiers( qualifiers );
        assertEquals( "modifiers", Modifier.VOLATILE, modifiers );
    }

    public void testParseModifier_volatile_public()
        throws Exception
    {
        final String[] qualifiers = new String[]{"volatile", "public"};
        final QDoxDescriptorParser parser = new QDoxDescriptorParser();
        final int modifiers = parser.parseModifiers( qualifiers );
        assertEquals( "modifiers", Modifier.VOLATILE | Modifier.PUBLIC, modifiers );
    }

    public void testBuildAttributeWithNullQDoxValue()
        throws Exception
    {
        final String name = "myTag";
        final String value = null;
        final DocletTag tag = new DocletTag( name, null );
        final QDoxDescriptorParser parser = new QDoxDescriptorParser();
        final Attribute attribute = parser.buildAttribute( tag );
        assertNotNull( "attribute", attribute );
        assertEquals( "attribute.name", name, attribute.getName() );
        assertEquals( "attribute.value", value, attribute.getValue() );
        assertEquals( "attribute.parameterCount",
                      0, attribute.getParameterCount() );
    }

    public void testBuildAttributeWithEmptyQDoxValue()
        throws Exception
    {
        final String name = "myTag";
        final String value = null;
        final DocletTag tag = new DocletTag( name, "" );
        final QDoxDescriptorParser parser = new QDoxDescriptorParser();
        final Attribute attribute = parser.buildAttribute( tag );
        assertNotNull( "attribute", attribute );
        assertEquals( "attribute.name", name, attribute.getName() );
        assertEquals( "attribute.value", value, attribute.getValue() );
        assertEquals( "attribute.parameterCount",
                      0, attribute.getParameterCount() );
    }

    public void testBuildAttributeWithValue()
        throws Exception
    {
        final String name = "myTag";
        final String value = "Here is some text";
        final DocletTag tag = new DocletTag( name, value );
        final QDoxDescriptorParser parser = new QDoxDescriptorParser();
        final Attribute attribute = parser.buildAttribute( tag );
        assertNotNull( "attribute", attribute );
        assertEquals( "attribute.name", name, attribute.getName() );
        assertEquals( "attribute.value", value, attribute.getValue() );
        assertEquals( "attribute.parameterCount",
                      0, attribute.getParameterCount() );
    }

    public void testBuildAttributeWithParameters()
        throws Exception
    {
        final String name = "myTag";
        final String value = null;
        final DocletTag tag = new DocletTag( name, "key=\"value\"" );
        final QDoxDescriptorParser parser = new QDoxDescriptorParser();
        final Attribute attribute = parser.buildAttribute( tag );
        assertNotNull( "attribute", attribute );
        assertEquals( "attribute.name", name, attribute.getName() );
        assertEquals( "attribute.value", value, attribute.getValue() );
        assertEquals( "attribute.parameterCount",
                      1, attribute.getParameterCount() );
        assertEquals( "attribute.parameter('key')",
                      "value", attribute.getParameter( "key" ) );
    }

    public void testBuildField()
        throws Exception
    {
        final String name = "myField";
        final String type = "int";
        final JavaField javaField = new JavaField();
        javaField.setType( new Type( type ) );
        javaField.setTags( new ArrayList() );
        javaField.setModifiers( new String[]{"public"} );
        javaField.setName( name );
        final QDoxDescriptorParser parser = new QDoxDescriptorParser();
        final FieldDescriptor field = parser.buildField( javaField );
        assertNotNull( "field", field );
        assertEquals( "field.name", name, field.getName() );
        assertEquals( "field.type", type, field.getType() );
        assertEquals( "field.modifiers", Modifier.PUBLIC, field.getModifiers() );
        assertEquals( "field.attributes.length", 0, field.getAttributes().length );
    }

    public void testBuildParameter()
        throws Exception
    {
        final String name = "myField";
        final String type = "int";
        final JavaParameter javaParameter = new JavaParameter( new Type( type ), name );
        final QDoxDescriptorParser parser = new QDoxDescriptorParser();
        final ParameterDescriptor parameter = parser.buildParameter( javaParameter );
        assertNotNull( "parameter", parameter );
        assertEquals( "parameter.name", name, parameter.getName() );
        assertEquals( "parameter.type", type, parameter.getType() );
    }

    public void testBuildMethod()
        throws Exception
    {
        final String name = "myField";
        final String type = "int";
        final JavaMethod javaMethod = new JavaMethod();
        javaMethod.setName( name );
        javaMethod.setReturns( new Type( type ) );
        javaMethod.setConstructor( false );
        javaMethod.setExceptions( new Type[ 0 ] );
        javaMethod.setParameters( new JavaParameter[ 0 ] );
        javaMethod.setModifiers( new String[]{"public"} );
        javaMethod.setTags( new ArrayList() );
        final QDoxDescriptorParser parser = new QDoxDescriptorParser();
        final MethodDescriptor method = parser.buildMethod( javaMethod );
        assertNotNull( "method", method );
        assertEquals( "method.name", name, method.getName() );
        assertEquals( "method.type", type, method.getReturnType() );
        assertEquals( "method.modifiers", Modifier.PUBLIC, method.getModifiers() );
        assertEquals( "method.parameters.length", 0, method.getParameters().length );
    }
}
