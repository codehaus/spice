/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.configkit;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.ArrayList;
import org.iso_relax.verifier.Schema;
import org.iso_relax.verifier.Verifier;
import org.iso_relax.verifier.VerifierConfigurationException;
import org.w3c.dom.Node;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * The ConfigValidator is responsible for validating an XML configuration
 * source according to a specified Schema. The schema is defined when the validator
 * is created. Note that the user should get a reference to the validator via the
 * ConfigValidatorFactory.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-04-04 11:13:02 $
 */
public class ConfigValidator
{
    /**
     * The schema that used when validating configuration.
     */
    private Schema m_schema;

    /**
     * Create a validator for specified schema.
     *
     * @param schema the schema
     */
    ConfigValidator( final Schema schema )
    {
        if( null == schema )
        {
            throw new NullPointerException( "schema" );
        }
        m_schema = schema;
    }

    /**
     * Validate input stream according to shcema.
     *
     * @param inputStream the inputStream to validate
     * @param errorHandler that will be passed any errors. May be null.
     * @throws ValidateException if unable to validate the input
     */
    public void validate( final InputStream inputStream, final ErrorHandler errorHandler )
        throws ValidateException
    {
        if( null == inputStream )
        {
            throw new NullPointerException( "inputStream" );
        }
        validate( new InputSource( inputStream ), errorHandler );
    }

    /**
     * Validate InputStream according to shcema.
     *
     * @param inputStream the inputStream to validate
     * @return the ValidationResults
     */
    public ValidationResult validate( final InputStream inputStream )
    {
        if( null == inputStream )
        {
            throw new NullPointerException( "inputStream" );
        }
        return validate( new InputSource( inputStream ) );
    }

    /**
     * Validate InputSource according to shcema.
     *
     * @param source the InputSource to validate
     * @param errorHandler that will be passed any errors. May be null.
     * @throws ValidateException if unable to validate the input
     */
    public void validate( final InputSource source, final ErrorHandler errorHandler )
        throws ValidateException
    {
        if( null == source )
        {
            throw new NullPointerException( "source" );
        }
        doValidate( source, errorHandler );
    }

    /**
     * Validate InputSource according to shcema.
     *
     * @param source the source to validate
     * @return the ValidationResults
     */
    public ValidationResult validate( final InputSource source )
    {
        if( null == source )
        {
            throw new NullPointerException( "source" );
        }
        return doValidate( source );
    }

    /**
     * Validate Node according to shcema.
     *
     * @param node the node to validate
     * @param errorHandler that will be passed any errors. May be null.
     * @throws ValidateException if unable to validate the input
     */
    public void validate( final Node node, final ErrorHandler errorHandler )
        throws ValidateException
    {
        if( null == node )
        {
            throw new NullPointerException( "node" );
        }
        doValidate( node, errorHandler );
    }

    /**
     * Validate Node according to shcema.
     *
     * @param node the node to validate
     * @return the ValidationResults
     */
    public ValidationResult validate( final Node node )
    {
        if( null == node )
        {
            throw new NullPointerException( "node" );
        }
        return doValidate( node );
    }

    /**
     * Perform the validation and collect the results.
     *
     * @param data either a DOM Node or an InputSource to validate
     * @return the ValidationResults
     */
    private ValidationResult doValidate( final Object data )
    {
        final List issueSet = new ArrayList();
        ValidateException exception = null;
        try
        {
            doValidate( data, new IssueCollector( issueSet ) );
        }
        catch( final ValidateException ve )
        {
            exception = ve;
        }

        final ValidationIssue[] issues = (ValidationIssue[])issueSet.
            toArray( new ValidationIssue[ issueSet.size() ] );
        return new ValidationResult( exception, issues );
    }

    /**
     * Actually perform the validation.
     *
     * @param data either a DOM Node or an InputSource to validate
     * @param errorHandler the error handler that receives error messages
     * @throws ValidateException if unable to validate the input
     */
    private void doValidate( final Object data,
                             final ErrorHandler errorHandler )
        throws ValidateException
    {
        try
        {
            final Verifier verifier = m_schema.newVerifier();
            verifier.setErrorHandler( errorHandler );
            boolean valid;
            if( data instanceof InputSource )
            {
                valid = verifier.verify( (InputSource)data );
            }
            else
            {
                valid = verifier.verify( (Node)data );
            }

            if( !valid )
            {
                final String message = "Unable to validate input according to schema";
                throw new ValidateException( message );
            }
        }
        catch( final VerifierConfigurationException vce )
        {
            final String message = "Unable to locate verifier for schema";
            throw new ValidateException( message, vce );
        }
        catch( final SAXException se )
        {
            final String message = "Malformed input XML.";
            throw new ValidateException( message, se );
        }
        catch( final IOException ioe )
        {
            final String message = "Error reading input data XML.";
            throw new ValidateException( message, ioe );
        }
    }
}
