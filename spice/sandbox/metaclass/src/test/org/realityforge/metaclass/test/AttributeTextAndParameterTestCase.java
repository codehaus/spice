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
import junit.textui.TestRunner;
import org.realityforge.metaclass.model.Attribute;

import java.util.Properties;

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

    /**
     * Runs the test case.
     */
    public static void main( String args[] )
    {
        TestRunner.run( suite() );
    }

    public void testCreateAttributeWithTextAndParameters()
    {
        final Properties parameters = new Properties();
        parameters.put( "foolish", "yes" );
        try
        {
            new Attribute( "homer", "a text value", parameters );
        }
        catch ( IllegalArgumentException e )
        {
            return;
        }
        fail( "Attributes should be invalid if they contain both text and parameters." );
    }
}
