/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.test;

import java.lang.reflect.Modifier;
import org.realityforge.metaclass.model.Attribute;
import org.realityforge.metaclass.model.ParameterDescriptor;

/**
 * Constants for BasicClass TestCases
 */
public interface BasicClassTestDataConstants
{
    String CLASS_NAME = "org.realityforge.metaclass.test.data.BasicClass";

    int CLASS_MODIFIER = Modifier.PUBLIC;

    // ------------------------------------------------------------ Class Constants
    AttributeProperties CLASS_TAG_2_PARAMETERS =
        new AttributeProperties( new String[][]{{"satan", "17.5"}} );

    String CLASS_TAG_0_NAME = "test-attribute1";
    Attribute CLASS_TAG_0 =
        new Attribute( CLASS_TAG_0_NAME, "true" );
    Attribute CLASS_TAG_1 =
        new Attribute( "test-attribute2", "thisIsATestString" );
    Attribute CLASS_TAG_2 =
        new Attribute( "test-attribute3", CLASS_TAG_2_PARAMETERS.getProperties() );

    Attribute[] CLASS_ATTRIBUTES = new Attribute[]
    {
        CLASS_TAG_0,
        CLASS_TAG_1,
        CLASS_TAG_2
    };

    Attribute[] CLASS_ATTRIBUTES_NAMED_TAG_0 = new Attribute[]
    {
        CLASS_TAG_0
    };

    // ------------------------------------------------------------ Field Constants

    String[] FIELD_NAMES =
        {
            "A_CONSTANT_STRING",
            "_aPublicInt",
            "_aProtectedDouble",
            "_aPrivateString"
        };

    String[] FIELD_TYPES = new String[]
    {
        "java.lang.String",
        "int",
        "double",
        "java.lang.String"
    };

    int[] FIELD_MODIFIERS = new int[]
    {
        Modifier.PUBLIC + Modifier.STATIC + Modifier.FINAL,
        Modifier.PUBLIC,
        Modifier.PROTECTED,
        Modifier.PRIVATE
    };

    AttributeProperties FIELD_TAG_1_PARAMETERS =
        new AttributeProperties( new String[][]{{"parameters", "true"}} );

    String FIELD_0_TAG_0_NAME = "haha";
    Attribute FIELD_0_TAG_0 = new Attribute( FIELD_0_TAG_0_NAME,
                                             "this is javadoc for a field" );
    String FIELD_1_TAG_0_NAME = "hoho";
    Attribute FIELD_1_TAG_0 = new Attribute( FIELD_1_TAG_0_NAME,
                                             FIELD_TAG_1_PARAMETERS.getProperties() );

    Attribute[][] FIELD_ATTRIBUTES = new Attribute[][]
    {
        new Attribute[]{FIELD_0_TAG_0},
        new Attribute[]{FIELD_1_TAG_0},
        new Attribute[]{},
        new Attribute[]{}
    };

    Attribute[] FIELD_0_ATTRIBUTES_NAMED_TAG_0 = new Attribute[]
    {
        FIELD_0_TAG_0
    };

    // ------------------------------------------------------------ Method Constants

    String[] METHOD_NAMES = new String[]
    {
        "BasicClass",
        "getPrivateString",
        "setPrivateString",
        "aPrivateMethod"
    };

    int[] METHOD_MODIFIERS =
        {
            Modifier.PUBLIC,
            Modifier.PUBLIC,
            Modifier.PROTECTED,
            Modifier.PRIVATE
        };

    String[] METHOD_RETURN_TYPES =
        {
            "",
            "java.lang.String",
            "void",
            "void"
        };

    ParameterDescriptor[][] METHOD_PARAMETERS = new ParameterDescriptor[][]
    {
        new ParameterDescriptor[]{},
        new ParameterDescriptor[]{},
        new ParameterDescriptor[]{
            new ParameterDescriptor( "aPrivateString", "java.lang.String" )
        },
        new ParameterDescriptor[]{}
    };

    AttributeProperties METHOD_TAG_3_PARAMETERS =
        new AttributeProperties( new String[][]{{"1", "2"}} );

    String METHOD_1_TAG_0_NAME = "return";
    Attribute METHOD_1_TAG_0 = new Attribute( METHOD_1_TAG_0_NAME,
                                              "the private string" );
    Attribute METHOD_2_TAG_0 = new Attribute( "param", "aPrivateString" );
    String METHOD_3_TAG_0_NAME = "stuff";
    Attribute METHOD_3_TAG_0 = new Attribute( METHOD_3_TAG_0_NAME,
                                              METHOD_TAG_3_PARAMETERS.getProperties() );

    Attribute[][] METHOD_ATTRIBUTES = new Attribute[][]
    {
        new Attribute[]{},
        new Attribute[]
        {
            METHOD_1_TAG_0
        },
        new Attribute[]
        {
            METHOD_2_TAG_0
        },
        new Attribute[]
        {
            METHOD_3_TAG_0
        }
    };

    Attribute[] METHOD_1_ATTRIBUTES_NAMED_TAG_0 = new Attribute[]
    {
        METHOD_1_TAG_0
    };

    Attribute[] METHOD_3_ATTRIBUTES_NAMED_TAG_0 = new Attribute[]
    {
        METHOD_3_TAG_0
    };
}
