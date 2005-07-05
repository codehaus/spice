/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.jervlet;

/**
 * Exception class to be used when handling contexts
 * 
 * @author Johan Sjoberg
 */
public class ContextException extends Exception
{
    /** The original <code>Throwable</code> */
    private final Throwable m_throwable;

    /**
     * Create a new <code>ContextException</code>.
     *
     * @param message Explanation for this exception.
     */
    public ContextException( final String message )
    {
        this( message, null );
    }

    /**
     * Create a new <code>ContextException</code>.
     *
     * @param message Explanation for this exception.
     * @param throwable The original throwable that caused
     *        this exception. Give null if none exist.
     */
    public ContextException( final String message, final Throwable throwable )
    {
        super( message );
        m_throwable = throwable;
    }

    /**
     * Retrieve the original throwable that caused this
     * deployment exception.
     *
     * @return The original throwable or null if none exist.
     */
    public final Throwable getCause()
    {
        return m_throwable;
    }
}