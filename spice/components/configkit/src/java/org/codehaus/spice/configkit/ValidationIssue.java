/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.configkit;

import org.xml.sax.SAXParseException;

/**
 * This class records a specific issue that occured during validation.
 *
 * @author Peter Donald
 * @author Peter Royal
 * @version $Revision: 1.2 $ $Date: 2003-12-03 03:34:17 $
 */
public class ValidationIssue
{
    /** Type code for WARNING issues. */
    static final int TYPE_WARNING = 1;

    /** Type code for ERROR issues. */
    static final int TYPE_ERROR = 2;

    /** Type code for FATAL_ERROR issues. */
    static final int TYPE_FATAL_ERROR = 3;

    /** The type of the issue (one of TYPE_* constants). */
    private final int m_type;

    /** The exception that caused issue. */
    private final SAXParseException m_exception;

    /**
     * Create an issue with specified type and exception that caused issue.
     *
     * @param type the type of issue
     * @param exception the exception that caused issue.
     */
    public ValidationIssue( final int type,
                            final SAXParseException exception )
    {
        if( null == exception )
        {
            throw new NullPointerException( "exception" );
        }
        m_type = type;
        m_exception = exception;
    }

    /**
     * Return true if the issue is a warning, false otherwise.
     *
     * @return true if the issue is a warning, false otherwise.
     */
    public boolean isWarning()
    {
        return TYPE_WARNING == m_type;
    }

    /**
     * Return true if the issue is an error, false otherwise.
     *
     * @return true if the issue is an error, false otherwise.
     */
    public boolean isError()
    {
        return TYPE_ERROR == m_type;
    }

    /**
     * Return true if the issue is a fatal error, false otherwise.
     *
     * @return true if the issue is a fatal error, false otherwise.
     */
    public boolean isFatalError()
    {
        return TYPE_FATAL_ERROR == m_type;
    }

    /**
     * The exception that caused issue.
     *
     * @return the exception that caused issue.
     */
    public SAXParseException getException()
    {
        return m_exception;
    }
}
