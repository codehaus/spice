/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.salt.lang;

import junit.framework.TestCase;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.2 $ $Date: 2003-06-06 06:33:34 $
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
        assertEquals( "size", 4, enumSet.getCodes().size() );
        assertEquals( "A_VAL", BaseConstants.A_VAL, enumSet.getCodeFor( "A_VAL" ) );
        assertEquals( "B_VAL", BaseConstants.B_VAL, enumSet.getCodeFor( "B_VAL" ) );
        assertEquals( "C_VAL", BaseConstants.C_VAL, enumSet.getCodeFor( "C_VAL" ) );
        assertEquals( "D_VAL", BaseConstants.D_VAL, enumSet.getCodeFor( "D_VAL" ) );
    }

    public void testSubPatternBase()
    {
        final EnumSet enumSet =
            EnumSet.createFrom( BaseConstants.class, "(.*_V)AL", false );
        assertEquals( "size", 4, enumSet.getCodes().size() );
        assertEquals( "A_VAL", BaseConstants.A_VAL, enumSet.getCodeFor( "A_V" ) );
        assertEquals( "B_VAL", BaseConstants.B_VAL, enumSet.getCodeFor( "B_V" ) );
        assertEquals( "C_VAL", BaseConstants.C_VAL, enumSet.getCodeFor( "C_V" ) );
        assertEquals( "D_VAL", BaseConstants.D_VAL, enumSet.getCodeFor( "D_V" ) );
    }

    public void testSubPatternBaseDeep()
    {
        final EnumSet enumSet =
            EnumSet.createFrom( BaseConstants.class, "(.*_V)AL", true );
        assertEquals( "size", 4, enumSet.getCodes().size() );
        assertEquals( "A_VAL", BaseConstants.A_VAL, enumSet.getCodeFor( "A_V" ) );
        assertEquals( "B_VAL", BaseConstants.B_VAL, enumSet.getCodeFor( "B_V" ) );
        assertEquals( "C_VAL", BaseConstants.C_VAL, enumSet.getCodeFor( "C_V" ) );
        assertEquals( "D_VAL", BaseConstants.D_VAL, enumSet.getCodeFor( "D_V" ) );
    }

    public void testInclusiveSuper()
    {
        final EnumSet enumSet =
            EnumSet.createFrom( SuperConstants.class, "(.*)", false );
        assertEquals( "size", 4, enumSet.getCodes().size() );
        assertEquals( "E_VAL", SuperConstants.E_VAL, enumSet.getCodeFor( "E_VAL" ) );
        assertEquals( "F_VAL", SuperConstants.F_VAL, enumSet.getCodeFor( "F_VAL" ) );
        assertEquals( "SOME_CONST", SuperConstants.SOME_CONST, enumSet.getCodeFor( "SOME_CONST" ) );
        assertEquals( "ANOTHER_CONST", SuperConstants.ANOTHER_CONST, enumSet.getCodeFor( "ANOTHER_CONST" ) );
    }

    public void testSubPatternSuper()
    {
        final EnumSet enumSet =
            EnumSet.createFrom( SuperConstants.class, "(.*_V)AL", false );
        assertEquals( "size", 2, enumSet.getCodes().size() );
        assertEquals( "E_VAL", SuperConstants.E_VAL, enumSet.getCodeFor( "E_V" ) );
        assertEquals( "F_VAL", SuperConstants.F_VAL, enumSet.getCodeFor( "F_V" ) );
    }

    public void testSubPatternSuperDeep()
    {
        final EnumSet enumSet =
            EnumSet.createFrom( SuperConstants.class, "(.*_V)AL" );
        assertEquals( "size", 6, enumSet.getCodes().size() );
        assertEquals( "A_VAL", BaseConstants.A_VAL, enumSet.getCodeFor( "A_V" ) );
        assertEquals( "B_VAL", BaseConstants.B_VAL, enumSet.getCodeFor( "B_V" ) );
        assertEquals( "C_VAL", BaseConstants.C_VAL, enumSet.getCodeFor( "C_V" ) );
        assertEquals( "D_VAL", BaseConstants.D_VAL, enumSet.getCodeFor( "D_V" ) );
        assertEquals( "E_VAL", SuperConstants.E_VAL, enumSet.getCodeFor( "E_V" ) );
        assertEquals( "F_VAL", SuperConstants.F_VAL, enumSet.getCodeFor( "F_V" ) );
    }

    public void testSubPatternSuperDeepBasicCreator()
    {
        final EnumSet enumSet =
            EnumSet.createFrom( SuperConstants.class );
        assertEquals( "size", 8, enumSet.getCodes().size() );
        assertEquals( "A_VAL", BaseConstants.A_VAL, enumSet.getCodeFor( "A_VAL" ) );
        assertEquals( "B_VAL", BaseConstants.B_VAL, enumSet.getCodeFor( "B_VAL" ) );
        assertEquals( "C_VAL", BaseConstants.C_VAL, enumSet.getCodeFor( "C_VAL" ) );
        assertEquals( "D_VAL", BaseConstants.D_VAL, enumSet.getCodeFor( "D_VAL" ) );
        assertEquals( "E_VAL", SuperConstants.E_VAL, enumSet.getCodeFor( "E_VAL" ) );
        assertEquals( "F_VAL", SuperConstants.F_VAL, enumSet.getCodeFor( "F_VAL" ) );
        assertEquals( "SOME_CONST", SuperConstants.SOME_CONST, enumSet.getCodeFor( "SOME_CONST" ) );
        assertEquals( "ANOTHER_CONST", SuperConstants.ANOTHER_CONST, enumSet.getCodeFor( "ANOTHER_CONST" ) );
    }

    public void testBadPattern()
    {
        try
        {
            EnumSet.createFrom( SuperConstants.class, "(.*_VAL" );
            fail( "Expected to fail as had malformed pattern");
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
}
