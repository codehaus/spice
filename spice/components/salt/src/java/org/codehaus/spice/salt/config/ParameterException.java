/*
 * Copyright (C) The JContainer Group. All rights reserved.
 *
 * This software is published under the terms of the JContainer
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.salt.config;

/**
 * The ParameterException is used to signal a problem retrieving a parameter
 * from the Parameters object.
 *
 * @version $Revision: 1.1 $ $Date: 2003-12-02 02:15:04 $
 */
public class ParameterException
    extends Exception
{
    /** The exception that caused this exception if any. */
    private final Throwable m_cause;

    /** The parameter key that caused the problem. */
    private final String m_key;

    /**
     * Create a ParameterException with specified message and key.
     *
     * @param message the message
     * @param key the key
     */
    public ParameterException( final String message,
                               final String key )
    {
        this( message, key, null );
    }

    /**
     * Create a ParameterException with specified message, key and cause.
     *
     * @param message the message
     * @param key the key
     * @param cause the cause
     */
    public ParameterException( final String message,
                               final String key,
                               final Throwable cause )
    {
        super( message );
        m_key = key;
        m_cause = cause;
    }

    /**
     * Return the parameter key that caused the problem.
     *
     * @return the parameter key that caused the problem.
     */
    public String getKey()
    {
        return m_key;
    }

    /**
     * Return the exception that caused this exception if any.
     *
     * @return the exception that caused this exception if any.
     */
    public Throwable getCause()
    {
        return m_cause;
    }
}
