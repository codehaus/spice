/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.salt.lang;

import junit.framework.TestCase;

/**
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2003-12-02 02:15:05 $
 */
public class EnumSetTestCase
    extends TestCase
{
    static interface BaseConstants
    {
        String STRING_CONSTANT = "There is no spoon.";
        int A_VAL = 2;
        int B_VAL = 3;
        int C_VAL = 4;
        int D_VAL = 5;
    }

    static interface SuperConstants
        extends BaseConstants
    {
        String ANOTHER_STRING_CONSTANT = "There is no spoon.";
        int E_VAL = 6;
        int F_VAL = 7;
        int SOME_CONST = 8;
        int ANOTHER_CONST = 9;
    }

    public EnumSetTestCase( final String name )
    {
        super( name );
    }

    public void testInclusiveBase()
    {
        final EnumSet enumSet =
            EnumSet.createFrom( BaseConstants.class, "(.*)", false );
        assertEquals( "codes.size", 4, enumSet.getCodes().size() );
        assertEquals( "names.size", 4, enumSet.getNames().size() );
        assertEquals( "A_VAL",
                      BaseConstants.A_VAL,
                      enumSet.getCodeFor( "A_VAL" ) );
        assertEquals( "B_VAL",
                      BaseConstants.B_VAL,
                      enumSet.getCodeFor( "B_VAL" ) );
        assertEquals( "C_VAL",
                      BaseConstants.C_VAL,
                      enumSet.getCodeFor( "C_VAL" ) );
        assertEquals( "D_VAL",
                      BaseConstants.D_VAL,
                      enumSet.getCodeFor( "D_VAL" ) );
        assertEquals( "A_VAL Str",
                      "A_VAL",
                      enumSet.getNameFor( BaseConstants.A_VAL ) );
        assertEquals( "B_VAL Str",
                      "B_VAL",
                      enumSet.getNameFor( BaseConstants.B_VAL ) );
        assertEquals( "C_VAL Str",
                      "C_VAL",
                      enumSet.getNameFor( BaseConstants.C_VAL ) );
        assertEquals( "D_VAL Str",
                      "D_VAL",
                      enumSet.getNameFor( BaseConstants.D_VAL ) );
    }

    public void testSubPatternBase()
    {
        final EnumSet enumSet =
            EnumSet.createFrom( BaseConstants.class, "(.*_V)AL", false );
        assertEquals( "codes.size", 4, enumSet.getCodes().size() );
        assertEquals( "names.size", 4, enumSet.getNames().size() );
        assertEquals( "A_VAL",
                      BaseConstants.A_VAL,
                      enumSet.getCodeFor( "A_V" ) );
        assertEquals( "B_VAL",
                      BaseConstants.B_VAL,
                      enumSet.getCodeFor( "B_V" ) );
        assertEquals( "C_VAL",
                      BaseConstants.C_VAL,
                      enumSet.getCodeFor( "C_V" ) );
        assertEquals( "D_VAL",
                      BaseConstants.D_VAL,
                      enumSet.getCodeFor( "D_V" ) );
        assertEquals( "A_VAL Str",
                      "A_V",
                      enumSet.getNameFor( BaseConstants.A_VAL ) );
        assertEquals( "B_VAL Str",
                      "B_V",
                      enumSet.getNameFor( BaseConstants.B_VAL ) );
        assertEquals( "C_VAL Str",
                      "C_V",
                      enumSet.getNameFor( BaseConstants.C_VAL ) );
        assertEquals( "D_VAL Str",
                      "D_V",
                      enumSet.getNameFor( BaseConstants.D_VAL ) );
    }

    public void testSubPatternBaseDeep()
    {
        final EnumSet enumSet =
            EnumSet.createFrom( BaseConstants.class, "(.*_V)AL", true );
        assertEquals( "codes.size", 4, enumSet.getCodes().size() );
        assertEquals( "names.size", 4, enumSet.getNames().size() );
        assertEquals( "A_VAL",
                      BaseConstants.A_VAL,
                      enumSet.getCodeFor( "A_V" ) );
        assertEquals( "B_VAL",
                      BaseConstants.B_VAL,
                      enumSet.getCodeFor( "B_V" ) );
        assertEquals( "C_VAL",
                      BaseConstants.C_VAL,
                      enumSet.getCodeFor( "C_V" ) );
        assertEquals( "D_VAL",
                      BaseConstants.D_VAL,
                      enumSet.getCodeFor( "D_V" ) );
        assertEquals( "A_VAL Str",
                      "A_V",
                      enumSet.getNameFor( BaseConstants.A_VAL ) );
        assertEquals( "B_VAL Str",
                      "B_V",
                      enumSet.getNameFor( BaseConstants.B_VAL ) );
        assertEquals( "C_VAL Str",
                      "C_V",
                      enumSet.getNameFor( BaseConstants.C_VAL ) );
        assertEquals( "D_VAL Str",
                      "D_V",
                      enumSet.getNameFor( BaseConstants.D_VAL ) );
    }

    public void testInclusiveSuper()
    {
        final EnumSet enumSet =
            EnumSet.createFrom( SuperConstants.class, "(.*)", false );
        assertEquals( "codes.size", 4, enumSet.getCodes().size() );
        assertEquals( "names.size", 4, enumSet.getNames().size() );
        assertEquals( "E_VAL",
                      SuperConstants.E_VAL,
                      enumSet.getCodeFor( "E_VAL" ) );
        assertEquals( "F_VAL",
                      SuperConstants.F_VAL,
                      enumSet.getCodeFor( "F_VAL" ) );
        assertEquals( "SOME_CONST",
                      SuperConstants.SOME_CONST,
                      enumSet.getCodeFor( "SOME_CONST" ) );
        assertEquals( "ANOTHER_CONST",
                      SuperConstants.ANOTHER_CONST,
                      enumSet.getCodeFor( "ANOTHER_CONST" ) );
        assertEquals( "E_VAL Str",
                      "E_VAL",
                      enumSet.getNameFor( SuperConstants.E_VAL ) );
        assertEquals( "F_VAL Str",
                      "F_VAL",
                      enumSet.getNameFor( SuperConstants.F_VAL ) );
        assertEquals( "SOME_CONST Str",
                      "SOME_CONST",
                      enumSet.getNameFor( SuperConstants.SOME_CONST ) );
        assertEquals( "ANOTHER_CONST Str",
                      "ANOTHER_CONST",
                      enumSet.getNameFor( SuperConstants.ANOTHER_CONST ) );
    }

    public void testSubPatternSuper()
    {
        final EnumSet enumSet =
            EnumSet.createFrom( SuperConstants.class, "(.*_V)AL", false );
        assertEquals( "codes.size", 2, enumSet.getCodes().size() );
        assertEquals( "names.size", 2, enumSet.getNames().size() );
        assertEquals( "E_VAL",
                      SuperConstants.E_VAL,
                      enumSet.getCodeFor( "E_V" ) );
        assertEquals( "F_VAL",
                      SuperConstants.F_VAL,
                      enumSet.getCodeFor( "F_V" ) );
        assertEquals( "E_VAL Str",
                      "E_V",
                      enumSet.getNameFor( SuperConstants.E_VAL ) );
        assertEquals( "F_VAL Str",
                      "F_V",
                      enumSet.getNameFor( SuperConstants.F_VAL ) );
    }

    public void testSubPatternSuperDeep()
    {
        final EnumSet enumSet =
            EnumSet.createFrom( SuperConstants.class, "(.*_V)AL" );
        assertEquals( "codes.size", 6, enumSet.getCodes().size() );
        assertEquals( "names.size", 6, enumSet.getNames().size() );
        assertEquals( "A_VAL",
                      BaseConstants.A_VAL,
                      enumSet.getCodeFor( "A_V" ) );
        assertEquals( "B_VAL",
                      BaseConstants.B_VAL,
                      enumSet.getCodeFor( "B_V" ) );
        assertEquals( "C_VAL",
                      BaseConstants.C_VAL,
                      enumSet.getCodeFor( "C_V" ) );
        assertEquals( "D_VAL",
                      BaseConstants.D_VAL,
                      enumSet.getCodeFor( "D_V" ) );
        assertEquals( "E_VAL",
                      SuperConstants.E_VAL,
                      enumSet.getCodeFor( "E_V" ) );
        assertEquals( "F_VAL",
                      SuperConstants.F_VAL,
                      enumSet.getCodeFor( "F_V" ) );
        assertEquals( "A_VAL Str",
                      "A_V",
                      enumSet.getNameFor( BaseConstants.A_VAL ) );
        assertEquals( "B_VAL Str",
                      "B_V",
                      enumSet.getNameFor( BaseConstants.B_VAL ) );
        assertEquals( "C_VAL Str",
                      "C_V",
                      enumSet.getNameFor( BaseConstants.C_VAL ) );
        assertEquals( "D_VAL Str",
                      "D_V",
                      enumSet.getNameFor( BaseConstants.D_VAL ) );
        assertEquals( "E_VAL Str",
                      "E_V",
                      enumSet.getNameFor( SuperConstants.E_VAL ) );
        assertEquals( "F_VAL Str",
                      "F_V",
                      enumSet.getNameFor( SuperConstants.F_VAL ) );
    }

    public void testSubPatternSuperDeepBasicCreator()
    {
        final EnumSet enumSet =
            EnumSet.createFrom( SuperConstants.class );
        assertEquals( "codes.size", 8, enumSet.getCodes().size() );
        assertEquals( "names.size", 8, enumSet.getNames().size() );
        assertEquals( "A_VAL",
                      BaseConstants.A_VAL,
                      enumSet.getCodeFor( "A_VAL" ) );
        assertEquals( "B_VAL",
                      BaseConstants.B_VAL,
                      enumSet.getCodeFor( "B_VAL" ) );
        assertEquals( "C_VAL",
                      BaseConstants.C_VAL,
                      enumSet.getCodeFor( "C_VAL" ) );
        assertEquals( "D_VAL",
                      BaseConstants.D_VAL,
                      enumSet.getCodeFor( "D_VAL" ) );
        assertEquals( "E_VAL",
                      SuperConstants.E_VAL,
                      enumSet.getCodeFor( "E_VAL" ) );
        assertEquals( "F_VAL",
                      SuperConstants.F_VAL,
                      enumSet.getCodeFor( "F_VAL" ) );
        assertEquals( "SOME_CONST",
                      SuperConstants.SOME_CONST,
                      enumSet.getCodeFor( "SOME_CONST" ) );
        assertEquals( "ANOTHER_CONST",
                      SuperConstants.ANOTHER_CONST,
                      enumSet.getCodeFor( "ANOTHER_CONST" ) );
        assertEquals( "A_VAL Str",
                      "A_VAL",
                      enumSet.getNameFor( BaseConstants.A_VAL ) );
        assertEquals( "B_VAL Str",
                      "B_VAL",
                      enumSet.getNameFor( BaseConstants.B_VAL ) );
        assertEquals( "C_VAL Str",
                      "C_VAL",
                      enumSet.getNameFor( BaseConstants.C_VAL ) );
        assertEquals( "D_VAL Str",
                      "D_VAL",
                      enumSet.getNameFor( BaseConstants.D_VAL ) );
        assertEquals( "E_VAL Str",
                      "E_VAL",
                      enumSet.getNameFor( SuperConstants.E_VAL ) );
        assertEquals( "F_VAL Str",
                      "F_VAL",
                      enumSet.getNameFor( SuperConstants.F_VAL ) );
        assertEquals( "SOME_CONST Str",
                      "SOME_CONST",
                      enumSet.getNameFor( SuperConstants.SOME_CONST ) );
        assertEquals( "ANOTHER_CONST Str",
                      "ANOTHER_CONST",
                      enumSet.getNameFor( SuperConstants.ANOTHER_CONST ) );
    }

    public void testBadPattern()
    {
        try
        {
            EnumSet.createFrom( SuperConstants.class, "(.*_VAL" );
            fail( "Expected to fail as had malformed pattern" );
        }
        catch( final IllegalArgumentException iae )
        {
        }
    }

    public void testPatternHasMultipleGroups()
    {
        try
        {
            EnumSet.createFrom( SuperConstants.class, "(.*)(_VAL)" );
            fail( "Expected to fail as had multiple groups in pattern" );
        }
        catch( final IllegalArgumentException iae )
        {
        }
    }

    public void testGetCodeForNonExistent()
    {
        final EnumSet enumSet =
            EnumSet.createFrom( SuperConstants.class );
        try
        {
            enumSet.getCodeFor( "I_NO_EXIST" );
            fail( "Expected to fail as getting code for non existent Enum" );
        }
        catch( final IllegalArgumentException iae )
        {
        }
    }

    public void testGetNameForNonExistent()
    {
        final EnumSet enumSet =
            EnumSet.createFrom( SuperConstants.class );
        try
        {
            enumSet.getNameFor( 1977 );
            fail( "Expected to fail as getting name for non existent Enum" );
        }
        catch( final IllegalArgumentException iae )
        {
        }
    }
}
