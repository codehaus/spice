/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.test;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AttributeTextAndParameterTestCase
    extends TestCase
{
    public AttributeTextAndParameterTestCase()
    {
        super( "AttributeTextAndParameters" );
    }

    public static Test suite()
    {
        return new TestSuite( AttributeTextAndParameterTestCase.class );
    }

    public void testCreateAttributeWithTextAndParameters()
    {
    }
}
