/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaschema;

import java.io.InputStream;
import java.util.Properties;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

public class SchemaValidatorTest
    extends TestCase
{
    private static SchemaValidator validator = new SchemaValidator();

    public SchemaValidatorTest( final String name )
    {
        super( name );
    }

    public static Test suite()
    {
        return new TestSuite( SchemaValidatorTest.class );
    }

    /**
     * Runs the test case.
     */
    public static void main( String args[] )
    {
        TestRunner.run( suite() );
    }

    public void testReadSchema()
    {
        ClassLoader cl = SchemaValidator.class.getClassLoader();
        InputStream is = cl.getResourceAsStream( "etc/schema.xml" );
        try
        {
            validator.readSchema( is );
        }
        catch( InvalidMetaDataException e )
        {
            e.printStackTrace();
            fail( e.getMessage() );
        }
    }

    public void testValidateAttributeByValue()
    {
        try
        {
            validator.validateAttribute( "class/interface", "param", "param1 this is a parameter" );
        }
        catch( InvalidMetaDataException e )
        {
            e.printStackTrace();
            fail( e.getMessage() );
        }
    }

    public void testValidateAttributeByProperties()
    {
        try
        {
            Properties p = new Properties();
            p.setProperty( "type", "Foo" );
            p.setProperty( "context", "Boo" );
            validator.validateAttribute( "class/interface", "avalon-service", p );
        }
        catch( InvalidMetaDataException e )
        {
            e.printStackTrace();
            fail( e.getMessage() );
        }
    }
}
