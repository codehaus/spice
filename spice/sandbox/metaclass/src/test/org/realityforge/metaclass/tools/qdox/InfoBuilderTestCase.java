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
import java.lang.reflect.Modifier;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.3 $ $Date: 2003-08-22 05:56:47 $
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
}
