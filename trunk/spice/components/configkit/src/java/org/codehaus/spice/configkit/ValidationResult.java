/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.configkit;

/**
 * Result of validating a document against a schema. The result indicates
 * whether validation was successful, any exception throws by validator and any
 * {@link ValidationIssue}s that were reported.
 *
 * @author Peter Donald
 * @author Peter Royal
 * @version $Revision: 1.2 $ $Date: 2003-12-03 03:34:17 $
 */
public class ValidationResult
{
    /** Validation exception if validation not successful, else null. */
    private final ValidateException m_exception;

    /** The issues that were reported during validation. */
    private final ValidationIssue[] m_issues;

    /**
     * Create a ValidationResult.
     *
     * @param exception the exception thrown if validation failed
     * @param issues the issues that were reported during validation.
     */
    public ValidationResult( final ValidateException exception,
                             final ValidationIssue[] issues )
    {
        if( null == issues )
        {
            throw new NullPointerException( "issues" );
        }
        for( int i = 0; i < issues.length; i++ )
        {
            final ValidationIssue issue = issues[ i ];
            if( null == issue )
            {
                throw new NullPointerException( "issues[" + i + "]" );
            }
        }
        m_exception = exception;
        m_issues = issues;
    }

    /**
     * Return true if validation successful. false otherwise.
     *
     * @return true if validation successful. false otherwise.
     */
    public boolean isValid()
    {
        return null == getException();
    }

    /**
     * Return the exception thrown if validation not successful.
     *
     * @return the exception thrown if validation not successful.
     */
    public ValidateException getException()
    {
        return m_exception;
    }

    /**
     * Return the issues that were reported during validation.
     *
     * @return the issues that were reported during validation.
     */
    public ValidationIssue[] getIssues()
    {
        return m_issues;
    }
}
