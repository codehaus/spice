/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.test;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

public class RunAllTests
{
    public static Test suite()
    {
        final TestSuite suite = new TestSuite();
        suite.addTest( AttributeTextAndParameterTestCase.suite() );
        suite.addTest( EmptyClassTestCase.suite() );
        suite.addTest( BasicClassTestCase.suite() );
        suite.addTest( BasicClassAttributesTestCase.suite() );
        suite.addTest( BasicFieldTestCase.suite() );
        suite.addTest( BasicFieldAttributesTestCase.suite() );
        suite.addTest( BasicMethodTestCase.suite() );
        suite.addTest( BasicMethodAttributesTestCase.suite() );
        suite.addTest( BasicClassLoaderTestCase.suite() );
        return suite;
    }

    public static void main( String args[] )
    {
        TestRunner.run( suite() );
    }
}