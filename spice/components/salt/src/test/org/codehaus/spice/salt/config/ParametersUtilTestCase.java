/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.salt.config;

import java.util.Properties;
import junit.framework.TestCase;

/**
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2003-12-02 02:15:04 $
 */
public class ParametersUtilTestCase
    extends TestCase
{
    public void testFromProperties()
        throws Exception
    {
        final Properties properties = new Properties();
        properties.setProperty( "key1", "value1" );
        properties.setProperty( "key2", "value2" );
        final Parameters parameters = ParametersUtil.fromProperties(
            properties );
        final String[] names = parameters.getParameterNames();
        assertEquals( "names.length", 2, names.length );
        assertEquals( "parameters('key1')", "value1",
                      parameters.getParameter( "key1" ) );
        assertEquals( "parameters('key2')", "value2",
                      parameters.getParameter( "key2" ) );
    }

    public void testMergeInputIntoOutput()
        throws Exception
    {
        final DefaultParameters input = new DefaultParameters();
        input.setParameter( "key1", "value1" );
        input.setParameter( "key2", "value2" );
        final DefaultParameters output = new DefaultParameters();
        ParametersUtil.copy( output, input );
        final String[] names = output.getParameterNames();
        assertEquals( "output.names.length", 2, names.length );
        assertEquals( "output('key1')", "value1",
                      output.getParameter( "key1" ) );
        assertEquals( "output('key2')", "value2",
                      output.getParameter( "key2" ) );
    }

    public void testMergeWithOverwriting()
        throws Exception
    {
        final DefaultParameters input1 = new DefaultParameters();
        input1.setParameter( "key1", "input1" );
        final DefaultParameters input2 = new DefaultParameters();
        input2.setParameter( "key1", "input2" );
        final Parameters output = ParametersUtil.merge( input1, input2 );
        final String[] names = output.getParameterNames();
        assertEquals( "output.names.length", 1, names.length );
        assertEquals( "output('key1')", "input2",
                      output.getParameter( "key1" ) );
    }
}
