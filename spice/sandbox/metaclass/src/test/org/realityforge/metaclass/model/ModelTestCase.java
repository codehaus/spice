/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.model;

import junit.framework.TestCase;
import java.util.Properties;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-08-15 08:50:15 $
 */
public class ModelTestCase
    extends TestCase
{
    public void testAttributeOnlyWithName()
    {
        final Attribute attribute = new Attribute( "dna.service" );
        assertEquals( "attribute.getName() == dna.service",
                      "dna.service", attribute.getName() );
        assertEquals( "attribute.getValue() == null",
                      null, attribute.getValue() );
        assertEquals( "attribute.getParameterCount() == 0",
                      0, attribute.getParameterCount() );
        assertEquals( "attribute.getParameterNames().length == 0",
                      0, attribute.getParameterNames().length );
        assertEquals( "attribute.getParameter('key') == null",
                      null, attribute.getParameter( "key" ) );
        assertEquals( "attribute.getParameter('dummy','foo') == foo",
                      "foo", attribute.getParameter( "key", "foo" ) );
    }

    public void testAttributeWithNameAndValue()
    {
        final Attribute attribute = new Attribute( "dna.service", "blah" );
        assertEquals( "attribute.getName() == dna.service",
                      "dna.service", attribute.getName() );
        assertEquals( "attribute.getValue() == blah",
                      "blah", attribute.getValue() );
        assertEquals( "attribute.getParameterCount() == 0",
                      0, attribute.getParameterCount() );
        assertEquals( "attribute.getParameterNames().length == 0",
                      0, attribute.getParameterNames().length );
        assertEquals( "attribute.getParameter('key') == null",
                      null, attribute.getParameter( "key" ) );
        assertEquals( "attribute.getParameter('dummy','foo') == foo",
                      "foo", attribute.getParameter( "key", "foo" ) );
    }

    public void testAttributeWithNameAndParameters()
    {
        final Properties parameters = new Properties();
        parameters.setProperty( "key", "value" );
        final Attribute attribute = new Attribute( "dna.service", parameters );
        assertEquals( "attribute.getName() == dna.service",
                      "dna.service", attribute.getName() );
        assertEquals( "attribute.getValue() == null",
                      null, attribute.getValue() );
        assertEquals( "attribute.getParameterCount() == 1",
                      1, attribute.getParameterCount() );
        assertEquals( "attribute.getParameterNames().length == 1",
                      1, attribute.getParameterNames().length );
        assertEquals( "attribute.getParameter('key') == value",
                      "value", attribute.getParameter( "key" ) );
        assertEquals( "attribute.getParameter('dummy','foo') == foo",
                      "foo", attribute.getParameter( "key", "foo" ) );
    }
}
