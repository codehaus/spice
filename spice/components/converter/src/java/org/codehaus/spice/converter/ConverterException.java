/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.converter;

/**
 * ConverterException thrown when a problem occurs during convertion etc.
 *
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2003-12-02 08:37:56 $
 */
public class ConverterException
    extends Exception
{
    /**
     * The Throwable that caused this exception to be thrown.
     */
    private final Throwable m_throwable;

    /**
     * Basic constructor with a message
     *
     * @param message the message
     */
    public ConverterException( final String message )
    {
        this( message, null );
    }

    /**
     * Constructor that builds cascade so that other exception information can be retained.
     *
     * @param message the message
     * @param throwable the throwable
     */
    public ConverterException( final String message, final Throwable throwable )
    {
        super( message );
        m_throwable = throwable;
    }

    /**
     * Retrieve root cause of the exception.
     *
     * @return the root cause
     */
    public final Throwable getCause()
    {
        return m_throwable;
    }
}

