/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.test.data;

/**
 * This is the test class that {@link org.realityforge.metaclass.test.EmptyClassTestCase}
 * is run on.
 * Please update all relevant test compare data (constants within tests) if you change this class.
 * @test-attribute1 true
 * @test-attribute2 thisIsATestString
 * @test-attribute3 satan="17.5"
 */
public class BasicClass
{
    /**
     * @haha this is javadoc for a field
     */
    public static final String A_CONSTANT_STRING = "a constant string";

    /**
     * @hoho parameters="true"
     */
    public int _aPublicInt;
    protected double _aProtectedDouble;
    private String _aPrivateString;

    /**
     * This is the constructor.
     */
    public BasicClass()
    {
        setPrivateString( "jimbo" );
        aPrivateMethod();
    }

    /**
     * Getter method.
     * @return the private string
     */
    public String getPrivateString()
    {
        return _aPrivateString;
    }

    /**
     * Setter method.
     * @param aPrivateString
     */
    protected void setPrivateString( final String aPrivateString )
    {
        _aPrivateString = aPrivateString;
    }

    /**
     * This is a private method.
     * @stuff a1="2"
     */
    private void aPrivateMethod()
    {
        // do nothing
    }
}
