/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.salt.config;

import junit.framework.TestCase;

public class DefaultParametersTestCase
    extends TestCase
{
    public void testGetParameter()
        throws Exception
    {
        final DefaultParameters parameters = new DefaultParameters();
        final String name = "key";
        final String value = "value";
        parameters.setParameter( name, value );
        assertEquals( "parameters.isParameter( name )",
                      true,
                      parameters.isParameter( name ) );
        assertEquals( "parameters.getParameter( name, 'blah' )",
                      value,
                      parameters.getParameter( name, "blah" ) );
        assertEquals( "parameters.getParameter( name )",
                      value,
                      parameters.getParameter( name ) );
    }

    public void testGetMissingParameter()
        throws Exception
    {
        final DefaultParameters parameters = new DefaultParameters();
        final String name = "key";
        assertEquals( "parameters.isParameter( name )",
                      false,
                      parameters.isParameter( name ) );
        assertEquals( "parameters.getParameter( name, 'blah' )",
                      "blah",
                      parameters.getParameter( name, "blah" ) );
        try
        {
            parameters.getParameter( name );
            fail( "Expected getParameter on non existent parameter to fail" );
        }
        catch( ParameterException e )
        {

        }
    }

    public void testGetParameterNames()
        throws Exception
    {
        final DefaultParameters parameters = new DefaultParameters();
        final String name = "key";
        final String value = "value";
        parameters.setParameter( name, value );
        final String[] names = parameters.getParameterNames();

        assertEquals( "names.length", 1, names.length );
        assertEquals( "names[0]", name, names[ 0 ] );
    }

    public void testGetParameterWithNullName()
        throws Exception
    {
        final DefaultParameters parameters = new DefaultParameters();
        try
        {
            parameters.getParameter( null );
        }
        catch( NullPointerException npe )
        {
            assertEquals( "name", npe.getMessage() );
            return;
        }
        fail( "Expected getParameter(null) to fail due to passing null " );
    }

    public void testGetParameterWDefaultWithNullName()
        throws Exception
    {
        final DefaultParameters parameters = new DefaultParameters();
        try
        {
            parameters.getParameter( null, "blah" );
        }
        catch( NullPointerException npe )
        {
            assertEquals( "name", npe.getMessage() );
            return;
        }
        fail(
            "Expected getParameter(null,'blah') to fail due to passing null " );
    }

    public void testsetParameterWithNullName()
        throws Exception
    {
        final DefaultParameters parameters = new DefaultParameters();
        try
        {
            parameters.setParameter( null, "blah" );
        }
        catch( NullPointerException npe )
        {
            assertEquals( "name", npe.getMessage() );
            return;
        }
        fail(
            "Expected setParameter(null,'blah') to fail due to passing null " );
    }

    public void testsetParameterWithNullValue()
        throws Exception
    {
        final DefaultParameters parameters = new DefaultParameters();
        try
        {
            parameters.setParameter( "blah", null );
        }
        catch( NullPointerException npe )
        {
            assertEquals( "value", npe.getMessage() );
            return;
        }
        fail(
            "Expected setParameter('blah',null) to fail due to passing null " );
    }

    public void testNullPassedIntoCtor()
        throws Exception
    {
        try
        {
            new DefaultParameters( null );
        }
        catch( NullPointerException npe )
        {
            assertEquals( "prefix", npe.getMessage() );
            return;
        }
        fail( "Expected to fail due to passing null " );
    }

    public void testGetParameterAsBoolean()
        throws Exception
    {
        final DefaultParameters parameters = new DefaultParameters();
        final String name = "key";
        final String value = "true";
        parameters.setParameter( name, value );
        assertEquals( "parameters.getParameterAsBoolean( name, false )",
                      true,
                      parameters.getParameterAsBoolean( name, false ) );
        assertEquals( "parameters.getParameterAsBoolean( name )",
                      true,
                      parameters.getParameterAsBoolean( name ) );
    }

    public void testGetParameterAsInteger()
        throws Exception
    {
        final DefaultParameters parameters = new DefaultParameters();
        final String name = "key";
        final String value = "1";
        parameters.setParameter( name, value );
        assertEquals( "parameters.getParameterAsInteger( name, 3 )",
                      1,
                      parameters.getParameterAsInteger( name, 3 ) );
        assertEquals( "parameters.getParameterAsInteger( name )",
                      1,
                      parameters.getParameterAsInteger( name ) );
    }

    public void testGetParameterAsIntegerWithMalformedValue()
        throws Exception
    {
        final DefaultParameters parameters = new DefaultParameters();
        final String name = "key";
        final String value = "a";
        parameters.setParameter( name, value );
        assertEquals( "parameters.getParameterAsInteger( name, 3 )",
                      3,
                      parameters.getParameterAsInteger( name, 3 ) );
        try
        {
            parameters.getParameterAsInteger( name );
        }
        catch( ParameterException e )
        {
            return;
        }
        fail( "Expected to fail as parameter is malformed" );
    }

    public void testGetParameterAsLong()
        throws Exception
    {
        final DefaultParameters parameters = new DefaultParameters();
        final String name = "key";
        final String value = "1";
        parameters.setParameter( name, value );
        assertEquals( "parameters.getParameterAsLong( name, 3 )",
                      1,
                      parameters.getParameterAsLong( name, 3 ) );
        assertEquals( "parameters.getParameterAsLong( name )",
                      1,
                      parameters.getParameterAsLong( name ) );
    }

    public void testGetParameterAsLongWithMalformedValue()
        throws Exception
    {
        final DefaultParameters parameters = new DefaultParameters();
        final String name = "key";
        final String value = "a";
        parameters.setParameter( name, value );
        assertEquals( "parameters.getParameterAsInteger( name, 3 )",
                      3,
                      parameters.getParameterAsLong( name, 3 ) );
        try
        {
            parameters.getParameterAsLong( name );
        }
        catch( ParameterException e )
        {
            return;
        }
        fail( "Expected to fail as parameter is malformed" );
    }

    public void testGetParameterAsFloat()
        throws Exception
    {
        final DefaultParameters parameters = new DefaultParameters();
        final String name = "key";
        final String value = "1.0";
        parameters.setParameter( name, value );
        assertTrue( "parameters.getParameterAsFloat( name, 3 )",
                    1.0 == parameters.getParameterAsFloat( name, 3 ) );
        assertTrue( "parameters.getParameterAsFloat( name )",
                    1.0 == parameters.getParameterAsFloat( name ) );
    }

    public void testGetParameterAsFloatWithMalformedValue()
        throws Exception
    {
        final DefaultParameters parameters = new DefaultParameters();
        final String name = "key";
        final String value = "a";
        parameters.setParameter( name, value );
        assertTrue( "parameters.getParameterAsFloat( name, 3 )",
                    3.0 == parameters.getParameterAsFloat( name, 3 ) );
        try
        {
            parameters.getParameterAsFloat( name );
        }
        catch( ParameterException e )
        {
            return;
        }
        fail( "Expected to fail as parameter is malformed" );
    }

    public void testGetChildParametersWithZeroEntries()
        throws Exception
    {
        final DefaultParameters parameters = new DefaultParameters();
        final String name = "key";
        final String value = "a";
        parameters.setParameter( name, value );
        final Parameters childParameters =
            parameters.getChildParameters( "noexist" );

        assertEquals( "childParameters.length",
                      0, childParameters.getParameterNames().length );
    }

    public void testGetChildParametersWithSplitEntries()
        throws Exception
    {
        final DefaultParameters parameters = new DefaultParameters();
        parameters.setParameter( "var", "a" );
        parameters.setParameter( "var.s", "b" );
        final DefaultParameters childParameters =
            (DefaultParameters)parameters.getChildParameters( "var" );

        final String[] names = childParameters.getParameterNames();
        assertEquals( "childParameters.length", 1, names.length );
        assertEquals( "names[0]", "s", names[ 0 ] );
        assertEquals( "childParameters.getPrefix()",
                      "var", childParameters.getPrefix() );

    }

    public void testGetChildParametersWithSplitEntriesAcrossMultipleChildren()
        throws Exception
    {
        final DefaultParameters parameters = new DefaultParameters();
        parameters.setParameter( "var", "a" );
        parameters.setParameter( "var.t.s", "b" );
        final DefaultParameters childParameters =
            (DefaultParameters)parameters.getChildParameters( "var" );
        final DefaultParameters childChildParameters =
            (DefaultParameters)childParameters.getChildParameters( "t" );

        final String[] names = childChildParameters.getParameterNames();
        assertEquals( "childParameters.length", 1, names.length );
        assertEquals( "names[0]", "s", names[ 0 ] );
        assertEquals( "childParameters.getPrefix()",
                      "var", childParameters.getPrefix() );

    }

    public void testMakeReadOnlyMakesChildrenReadOnly()
        throws Exception
    {
        final DefaultParameters parameters = new DefaultParameters();
        parameters.setParameter( "var", "a" );
        parameters.setParameter( "var.t.s", "b" );
        final DefaultParameters childParameters =
            (DefaultParameters)parameters.getChildParameters( "var" );
        final DefaultParameters childChildParameters =
            (DefaultParameters)childParameters.getChildParameters( "t" );
        parameters.makeReadOnly();

        assertEquals( "child1.isReadOnly",
                      true,
                      childParameters.isReadOnly() );
        assertEquals( "child2.isReadOnly",
                      true,
                      childChildParameters.isReadOnly() );
    }

    public void testGetChildParametersWithNullPrefix()
        throws Exception
    {
        final DefaultParameters parameters = new DefaultParameters();
        try
        {
            parameters.getChildParameters( null );
        }
        catch( NullPointerException npe )
        {
            assertEquals( "prefix", npe.getMessage() );
            return;
        }
        fail( "Expected getChildParameters to fail when passing null prefix" );
    }

    public void testIsParameterWithNullName()
        throws Exception
    {
        final DefaultParameters parameters = new DefaultParameters();
        try
        {
            parameters.isParameter( null );
        }
        catch( NullPointerException npe )
        {
            assertEquals( "name", npe.getMessage() );
            return;
        }
        fail( "Expected to fail as name is null" );
    }

    public void testGetParameterAsBooleanWithDefault()
        throws Exception
    {
        final DefaultParameters parameters = new DefaultParameters();
        assertEquals( true, parameters.getParameterAsBoolean( "blah", true ) );
    }

    public void testGetParameterAsIntegerWithDefault()
        throws Exception
    {
        final DefaultParameters parameters = new DefaultParameters();
        assertEquals( 1, parameters.getParameterAsInteger( "blah", 1 ) );
    }

    public void testGetParameterAsLongWithDefault()
        throws Exception
    {
        final DefaultParameters parameters = new DefaultParameters();
        assertEquals( 1, parameters.getParameterAsLong( "blah", 1 ) );
    }

    public void testGetParameterAsFloatWithDefault()
        throws Exception
    {
        final DefaultParameters parameters = new DefaultParameters();
        assertTrue( 1.0f == parameters.getParameterAsFloat( "blah", 1.0f ) );
    }

    public void testMakeReadOnlyWithNonFreezableChildParameters()
        throws Exception
    {
        final DefaultParameters parameters = new DefaultParameters();
        parameters.getChildren().add( new NoopParameters() );
        parameters.makeReadOnly();
        assertEquals( true, parameters.isReadOnly() );
    }

}
