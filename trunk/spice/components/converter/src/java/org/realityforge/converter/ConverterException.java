/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 * This product includes software developed by the
 * Apache Software Foundation (http://www.apache.org/).
 */
package org.realityforge.converter;

/**
 * ConverterException thrown when a problem occurs during convertion etc.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.2 $ $Date: 2003-03-18 09:14:11 $
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

