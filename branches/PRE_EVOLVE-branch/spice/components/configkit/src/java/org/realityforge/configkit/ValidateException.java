/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.configkit;

/**
 * Exception thrown when there is an error validating
 * configuration.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-04-04 11:13:02 $
 */
public class ValidateException
    extends Exception
{
    /**
     * Create an exception with specified message.
     *
     * @param message the message.
     */
    public ValidateException( final String message )
    {
        super( message );
    }

    /**
     * Create an exception with specified message and cause.
     *
     * @param message the message
     * @param cause the cause
     */
    public ValidateException( final String message, final Throwable cause )
    {
        super( message, cause );
    }
}
