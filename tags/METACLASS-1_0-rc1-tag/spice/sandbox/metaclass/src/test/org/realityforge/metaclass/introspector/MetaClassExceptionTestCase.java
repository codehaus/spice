/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.introspector;

import junit.framework.TestCase;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-10-04 00:47:49 $
 */
public class MetaClassExceptionTestCase
    extends TestCase
{
    public void testMetaClassExceptionSimpleCtor()
        throws Exception
    {
        final String message = "aMessage";
        final MetaClassException exception =
            new MetaClassException( message );
        assertEquals( "message", message, exception.getMessage() );
        assertEquals( "cause", null, exception.getCause() );
    }

    public void testMetaClassExceptionComplexCtor()
        throws Exception
    {
        final String message = "aMessage";
        final Throwable cause = new Throwable();
        final MetaClassException exception =
            new MetaClassException( message, cause );
        assertEquals( "message", message, exception.getMessage() );
        assertEquals( "cause", cause, exception.getCause() );
    }
}
