/*
 * Copyright (C) The JContainer Group. All rights reserved.
 *
 * This software is published under the terms of the JContainer
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.salt.config;

import junit.framework.TestCase;

/**
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2003-12-02 02:15:04 $
 */
public class ParameterExceptionTestCase
    extends TestCase
{
    public void testParameterExceptionConstruction()
        throws Exception
    {
        final String message = "myMessage";
        final String key = "myKey";
        final Throwable cause = new Throwable();
        final ParameterException exception =
            new ParameterException( message, key, cause );

        assertEquals( "message", message, exception.getMessage() );
        assertEquals( "key", key, exception.getKey() );
        assertEquals( "cause", cause, exception.getCause() );
    }

    public void testParameterExceptionConstructionWithNullCause()
        throws Exception
    {
        final String message = "myMessage";
        final String key = "myKey";
        final Throwable cause = null;
        final ParameterException exception =
            new ParameterException( message, key, cause );

        assertEquals( "message", message, exception.getMessage() );
        assertEquals( "key", key, exception.getKey() );
        assertEquals( "cause", cause, exception.getCause() );
    }

    public void testParameterExceptionConstructionWithNullKey()
        throws Exception
    {
        final String message = "myMessage";
        final String key = null;
        final Throwable cause = new Throwable();
        final ParameterException exception =
            new ParameterException( message, key, cause );

        assertEquals( "message", message, exception.getMessage() );
        assertEquals( "key", key, exception.getKey() );
        assertEquals( "cause", cause, exception.getCause() );
    }

    public void testParameterExceptionConstructionWithNullMessage()
        throws Exception
    {
        final String message = null;
        final String key = "myKey";
        final Throwable cause = new Throwable();
        final ParameterException exception =
            new ParameterException( message, key, cause );

        assertEquals( "message", message, exception.getMessage() );
        assertEquals( "key", key, exception.getKey() );
        assertEquals( "cause", cause, exception.getCause() );
    }

    public void testParameterExceptionConstructionWithSimpleCtor()
        throws Exception
    {
        final String message = "myMessage";
        final String key = "myKey";
        final ParameterException exception =
            new ParameterException( message, key );

        assertEquals( "message", message, exception.getMessage() );
        assertEquals( "key", key, exception.getKey() );
        assertEquals( "cause", null, exception.getCause() );
    }
}
