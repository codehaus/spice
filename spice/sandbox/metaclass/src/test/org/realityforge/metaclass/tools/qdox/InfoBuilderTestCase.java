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

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-08-22 05:27:57 $
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
}
