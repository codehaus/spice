/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.configkit;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.iso_relax.verifier.Schema;
import org.iso_relax.verifier.Verifier;
import org.iso_relax.verifier.VerifierConfigurationException;
import org.iso_relax.verifier.VerifierFilter;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * The ConfigValidator is responsible for validating an XML configuration source
 * according to a specified Schema. The schema is defined when the validator is
 * created. Note that the user should get a reference to the validator via the
 * ConfigValidatorFactory.
 *
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2003-12-03 03:19:28 $
 */
public class ConfigValidator
{
    /** The schema that used when validating configuration. */
    private Schema m_schema;

    /** The entity resolver used by validator if any. */
    private EntityResolver m_resolver;

    /**
     * Create a validator for specified schema.
     *
     * @param schema the schema
     */
    ConfigValidator( final Schema schema,
                     final EntityResolver resolver )
    {
        if( null == schema )
        {
            throw new NullPointerException( "schema" );
        }
        m_schema = schema;
        m_resolver = resolver;
    }

    /**
     * Validate input stream according to shcema.
     *
     * @param inputStream the inputStream to validate
     * @param contentHandler the handler to send to after validation
     * @param errorHandler that will be passed any errors. May be null.
     * @throws ValidateException if unable to validate the input
     */
    public void validate( final InputStream inputStream,
                          final ContentHandler contentHandler,
                          final ErrorHandler errorHandler )
        throws ValidateException
    {
        if( null == inputStream )
        {
            throw new NullPointerException( "inputStream" );
        }
        validate( new InputSource( inputStream ),
                  contentHandler,
                  errorHandler );
    }

    /**
     * Validate input stream according to shcema.
     *
     * @param inputStream the inputStream to validate
     * @param errorHandler that will be passed any errors. May be null.
     * @throws ValidateException if unable to validate the input
     */
    public void validate( final InputStream inputStream,
                          final ErrorHandler errorHandler )
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
     * Validate InputStream according to shcema.
     *
     * @param inputStream the inputStream to validate
     * @param contentHandler the handler to send to after validation
     * @return the ValidationResults
     */
    public ValidationResult validate( final InputStream inputStream,
                                      final ContentHandler contentHandler )
    {
        if( null == inputStream )
        {
            throw new NullPointerException( "inputStream" );
        }
        return validate( new InputSource( inputStream ), contentHandler );
    }

    /**
     * Validate InputSource according to shcema.
     *
     * @param source the InputSource to validate
     * @param errorHandler that will be passed any errors. May be null.
     * @throws ValidateException if unable to validate the input
     */
    public void validate( final InputSource source,
                          final ErrorHandler errorHandler )
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
     * @param source the InputSource to validate
     * @param contentHandler the handler to send to after validation
     * @param errorHandler that will be passed any errors. May be null.
     * @throws ValidateException if unable to validate the input
     */
    public void validate( final InputSource source,
                          final ContentHandler contentHandler,
                          final ErrorHandler errorHandler )
        throws ValidateException
    {
        if( null == source )
        {
            throw new NullPointerException( "source" );
        }
        doValidate( source, contentHandler, errorHandler );
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
     * Validate InputSource according to shcema.
     *
     * @param source the source to validate
     * @param contentHandler the handler to send to after validation
     * @return the ValidationResults
     */
    public ValidationResult validate( final InputSource source,
                                      final ContentHandler contentHandler )
    {
        if( null == source )
        {
            throw new NullPointerException( "source" );
        }
        return doValidate( source, contentHandler );
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
            if( null != errorHandler )
            {
                verifier.setErrorHandler( errorHandler );
            }
            if( null != m_resolver )
            {
                verifier.setEntityResolver( m_resolver );
            }
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

    /**
     * Perform the validation via filtering and collect the results.
     *
     * @param input an InputSource to validate
     * @param contentHandler the handler to send to after validation
     * @return the ValidationResults
     */
    private ValidationResult doValidate( final InputSource input,
                                         final ContentHandler contentHandler )
    {
        final List issueSet = new ArrayList();
        ValidateException exception = null;
        try
        {
            doValidate( input,
                        contentHandler,
                        new IssueCollector( issueSet ) );
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
     * Actually perform the validation via filtering.
     *
     * @param input an InputSource to validate
     * @param contentHandler the handler to send to after validation
     * @param errorHandler the error handler that receives error messages
     * @throws ValidateException if unable to validate the input
     */
    private void doValidate( final InputSource input,
                             final ContentHandler contentHandler,
                             final ErrorHandler errorHandler )
        throws ValidateException
    {
        if( null == contentHandler )
        {
            throw new NullPointerException( "contentHandler" );
        }
        final SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        saxParserFactory.setNamespaceAware( true );

        try
        {
            final SAXParser saxParser = saxParserFactory.newSAXParser();
            final XMLReader reader = saxParser.getXMLReader();

            final Verifier verifier = m_schema.newVerifier();
            if( null != errorHandler )
            {
                verifier.setErrorHandler( errorHandler );
            }
            if( null != m_resolver )
            {
                verifier.setEntityResolver( m_resolver );
            }
            final VerifierFilter filter = verifier.getVerifierFilter();
            filter.setParent( reader );
            filter.setContentHandler( contentHandler );
            if( null != errorHandler )
            {
                filter.setErrorHandler( errorHandler );
            }
            if( null != m_resolver )
            {
                filter.setEntityResolver( m_resolver );
            }
            filter.parse( input );

            if( !filter.isValid() )
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
            final String message = "Error reading input input XML.";
            throw new ValidateException( message, ioe );
        }
        catch( final ParserConfigurationException pce )
        {
            final String message = "Unable to get parser.";
            throw new ValidateException( message, pce );
        }
    }
}
