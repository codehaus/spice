/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.configkit;

import java.util.List;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

/**
 * A Error handler used to collect issues reported during validation.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-04-04 11:13:02 $
 */
class IssueCollector
    implements ErrorHandler
{
    /**
     * the list of issues collected.
     */
    private final List m_issues;

    /**
     * Create a collector that adds issues to specified list.
     *
     * @param issues the list to add issues to
     */
    IssueCollector( final List issues )
    {
        if( null == issues )
        {
            throw new NullPointerException( "issues" );
        }
        m_issues = issues;
    }

    /**
     * Add a warning issue to issue list.
     *
     * @param exception the exception that caused issue
     */
    public void warning( final SAXParseException exception )
    {
        m_issues.add( new ValidationIssue( ValidationIssue.TYPE_WARNING, exception ) );
    }

    /**
     * Add a error issue to issue list.
     *
     * @param exception the exception that caused issue
     */
    public void error( final SAXParseException exception )
    {
        m_issues.add( new ValidationIssue( ValidationIssue.TYPE_ERROR, exception ) );
    }

    /**
     * Add a warning fatalError to issue list.
     *
     * @param exception the exception that caused issue
     */
    public void fatalError( final SAXParseException exception )
    {
        m_issues.add( new ValidationIssue( ValidationIssue.TYPE_FATAL_ERROR, exception ) );
    }
}
