/*
 * Copyright  The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 */
package org.realityforge.configkit;

import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import junit.framework.TestCase;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.EntityResolver;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Basic unit tests for the info objects.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 */
public final class ConfigValidatorTestCase
    extends TestCase
    implements ErrorHandler
{
    private static final boolean DEBUG = false;
    private static final ContentHandler HANDLER = new NoopContentHandler();

    public ConfigValidatorTestCase( final String name )
    {
        super( name );
    }

    public void testValidationIssue()
        throws Exception
    {
        final SAXParseException spe = new SAXParseException( "", null );

        final ValidationIssue issue1 = new ValidationIssue( ValidationIssue.TYPE_WARNING, spe );
        assertTrue( "issue3.isWarning", issue1.isWarning() );
        assertTrue( "!issue3.isError()", !issue1.isError() );
        assertTrue( "!issue3.isFatalError()", !issue1.isFatalError() );
        assertNotNull( "issue3.exception not null", issue1.getException() );

        final ValidationIssue issue2 = new ValidationIssue( ValidationIssue.TYPE_ERROR, spe );
        assertTrue( "issue3.isWarning", !issue2.isWarning() );
        assertTrue( "!issue3.isError()", issue2.isError() );
        assertTrue( "!issue3.isFatalError()", !issue2.isFatalError() );
        assertNotNull( "issue3.exception not null", issue2.getException() );

        final ValidationIssue issue3 = new ValidationIssue( ValidationIssue.TYPE_FATAL_ERROR, spe );
        assertTrue( "issue3.isWarning", !issue3.isWarning() );
        assertTrue( "!issue3.isError()", !issue3.isError() );
        assertTrue( "!issue3.isFatalError()", issue3.isFatalError() );
        assertNotNull( "issue3.exception not null", issue3.getException() );
    }

    public void testIssueColector()
        throws Exception
    {
        final SAXParseException spe = new SAXParseException( "", null );

        final ArrayList issues = new ArrayList();
        final IssueCollector collector = new IssueCollector( issues );
        collector.warning( spe );
        collector.error( spe );
        collector.fatalError( spe );

        assertEquals( "issues.length", 3, issues.size() );

        final ValidationIssue issue1 = (ValidationIssue)issues.get( 0 );
        final ValidationIssue issue2 = (ValidationIssue)issues.get( 1 );
        final ValidationIssue issue3 = (ValidationIssue)issues.get( 2 );

        assertTrue( "issue1.isWarning()", issue1.isWarning() );
        assertTrue( "!issue1.isError()", !issue1.isError() );
        assertTrue( "!issue1.isFatalError()", !issue1.isFatalError() );
        assertEquals( "issue1.exception()", spe, issue1.getException() );

        assertTrue( "issue2.isWarning()", !issue2.isWarning() );
        assertTrue( "!issue2.isError()", issue2.isError() );
        assertTrue( "!issue2.isFatalError()", !issue2.isFatalError() );
        assertEquals( "issue2.exception()", spe, issue2.getException() );

        assertTrue( "issue3.isWarning()", !issue3.isWarning() );
        assertTrue( "!issue3.isError()", !issue3.isError() );
        assertTrue( "!issue3.isFatalError()", issue3.isFatalError() );
        assertEquals( "issue3.exception()", spe, issue3.getException() );
    }

    public void testNullCTorInIssue()
        throws Exception
    {
        try
        {
            new ValidationIssue( ValidationIssue.TYPE_WARNING, null );
            fail( "Expected Null pointer due to null schema" );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( npe.getMessage(), "exception" );
        }
    }

    public void testNullCTorInIssueColector()
        throws Exception
    {
        try
        {
            new IssueCollector( null );
            fail( "Expected Null pointer due to null schema" );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( npe.getMessage(), "issues" );
        }
    }

    public void testNullCTorInResult()
        throws Exception
    {
        try
        {
            new ValidationResult( null, null );
            fail( "Expected Null pointer due to null issues" );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( npe.getMessage(), "issues" );
        }

        try
        {
            final ValidationIssue issue = new ValidationIssue( ValidationIssue.TYPE_WARNING, new SAXParseException( "test", null ) );
            final ValidationIssue[] issues = new ValidationIssue[]{issue, null};
            new ValidationResult( null, issues );
            fail( "Expected Null pointer due to null issues" );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( npe.getMessage(), "issues[1]" );
        }
    }

    public void testNullCTor()
        throws Exception
    {
        try
        {
            new ConfigValidator( null, null );
            fail( "Expected Null pointer due to null schema" );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( npe.getMessage(), "schema" );
        }
    }

    public void testNullHandler()
        throws Exception
    {
        try
        {
            final ConfigValidator validator =
                ConfigValidatorFactory.create( TestData.SCHEMA_PUBLIC_ID,
                                               TestData.SCHEMA_SYSTEM_ID,
                                               createClassLoader() );
            final InputStream inputStream = getClass().getClassLoader().getResourceAsStream( TestData.SCHEMA );
            assertNotNull( "ResourcePresent: " + TestData.CATALOG_JAR, inputStream );
            validator.validate( inputStream, null, null );
            fail( "Expected Null pointer due to null contentHandler" );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( npe.getMessage(), "contentHandler" );
        }
    }

    public void testNullSource()
        throws Exception
    {
        try
        {
            ConfigValidatorFactory.create( ConfigValidatorFactory.RELAX_NG,
                                           (InputSource)null );
            fail( "Expected Null pointer due to null source" );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( npe.getMessage(), "inputSource" );
        }
    }

    public void testNullStream()
        throws Exception
    {
        try
        {
            ConfigValidatorFactory.create( ConfigValidatorFactory.RELAX_NG,
                                           (InputStream)null );
            fail( "Expected Null pointer due to null stream" );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( npe.getMessage(), "inputStream" );
        }
    }

    public void testNullIDs()
        throws Exception
    {
        try
        {
            ConfigValidatorFactory.create( ConfigValidatorFactory.RELAX_NG,
                                           null,
                                           null,
                                           createClassLoader() );
            fail( "Expected Null pointer due to null schema" );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( npe.getMessage(), "publicID" );
        }
    }

    public void testNullClassLoader()
        throws Exception
    {
        try
        {
            ConfigValidatorFactory.create( ConfigValidatorFactory.RELAX_NG,
                                           TestData.SCHEMA_PUBLIC_ID,
                                           null,
                                           null );
            fail( "Expected Null pointer due to null schema" );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( npe.getMessage(), "classLoader" );
        }
    }

    public void testNoExist()
        throws Exception
    {
        try
        {
            ConfigValidatorFactory.create( ConfigValidatorFactory.RELAX_NG,
                                           "noexist",
                                           null,
                                           createClassLoader() );
            fail( "Expected Null pointer due to null schema" );
        }
        catch( final Exception npe )
        {
            assertTrue( -1 != npe.getMessage().indexOf( "noexist" ) );
        }
    }

    public void testLoadViaPublicID()
        throws Exception
    {
        final ConfigValidator configValidator =
            ConfigValidatorFactory.create( ConfigValidatorFactory.RELAX_NG,
                                           TestData.SCHEMA_PUBLIC_ID,
                                           null,
                                           createClassLoader() );
        doValidate( configValidator );
    }

    public void testLoadViaPublicIDNoSchema()
        throws Exception
    {
        final ConfigValidator configValidator =
            ConfigValidatorFactory.create( TestData.SCHEMA_PUBLIC_ID,
                                           null,
                                           createClassLoader() );
        doValidate( configValidator );
    }

    public void testLoadViaSystemID()
        throws Exception
    {
        final ConfigValidator configValidator =
            ConfigValidatorFactory.create( ConfigValidatorFactory.RELAX_NG,
                                           null,
                                           TestData.SCHEMA_SYSTEM_ID,
                                           createClassLoader() );
        doValidate( configValidator );
    }

    public void testLoadViaSystemIDNoSchema()
        throws Exception
    {
        final ConfigValidator configValidator =
            ConfigValidatorFactory.create( null,
                                           TestData.SCHEMA_SYSTEM_ID,
                                           createClassLoader() );
        doValidate( configValidator );
    }

    public void testLoadViaInputStream()
        throws Exception
    {
        final InputStream inputStream = getClass().getClassLoader().getResourceAsStream( TestData.SCHEMA );
        assertNotNull( "ResourcePresent: " + TestData.CATALOG_JAR, inputStream );
        final ConfigValidator configValidator =
            ConfigValidatorFactory.create( ConfigValidatorFactory.RELAX_NG,
                                           new InputSource( inputStream ) );
        doValidate( configValidator );
    }

    public void testLoadViaInputStreamNoSchema()
        throws Exception
    {
        final InputStream inputStream = getClass().getClassLoader().getResourceAsStream( TestData.SCHEMA );
        assertNotNull( "ResourcePresent: " + TestData.CATALOG_JAR, inputStream );
        final ConfigValidator configValidator =
            ConfigValidatorFactory.create( new InputSource( inputStream ) );
        doValidate( configValidator );
    }

    public void testLoadDTD()
        throws Exception
    {
        final InputStream inputStream = getClass().getClassLoader().getResourceAsStream( TestData.DTD );
        assertNotNull( "ResourcePresent: " + TestData.DTD, inputStream );
        final EntityResolver resolver = ResolverFactory.createResolver( createClassLoader() );
        final InputSource inputSource = new InputSource( inputStream );
        inputSource.setPublicId( TestData.PUBLIC_ID );
        inputSource.setSystemId( TestData.SYSTEM_ID );
        final ConfigValidator validator =
            ConfigValidatorFactory.create( inputSource, resolver );
        final ClassLoader classLoader = getClass().getClassLoader();

        final InputStream dataStream = classLoader.getResourceAsStream( TestData.ASSEMBLY_DATA );
        final ValidationResult result = validator.validate( dataStream );
        verifyResult( result, true );
    }

    public void testLoadViaInputSource()
        throws Exception
    {
        final InputStream inputStream = getClass().getClassLoader().getResourceAsStream( TestData.SCHEMA );
        assertNotNull( "ResourcePresent: " + TestData.CATALOG_JAR, inputStream );
        final ConfigValidator configValidator =
            ConfigValidatorFactory.create( ConfigValidatorFactory.RELAX_NG,
                                           inputStream );
        doValidate( configValidator );
    }

    public void testLoadViaInputSourceNoSchema()
        throws Exception
    {
        final InputStream inputStream = getClass().getClassLoader().getResourceAsStream( TestData.SCHEMA );
        assertNotNull( "ResourcePresent: " + TestData.CATALOG_JAR, inputStream );
        final ConfigValidator configValidator =
            ConfigValidatorFactory.create( inputStream );
        doValidate( configValidator );
    }

    private void doValidate( final ConfigValidator configValidator )
        throws Exception
    {
        ValidationResult result = null;
        InputStream dataStream = null;
        final ClassLoader classLoader = getClass().getClassLoader();

        dataStream = classLoader.getResourceAsStream( TestData.XML_DATA );
        configValidator.validate( dataStream, this );

        dataStream = classLoader.getResourceAsStream( TestData.XML_DATA );
        configValidator.validate( dataStream, HANDLER, this );

        dataStream = classLoader.getResourceAsStream( TestData.XML_DATA );
        result = configValidator.validate( dataStream );
        verifyResult( result, true );

        dataStream = classLoader.getResourceAsStream( TestData.XML_DATA );
        result = configValidator.validate( dataStream, HANDLER );
        verifyResult( result, true );

        dataStream = classLoader.getResourceAsStream( TestData.XML_DATA );
        configValidator.validate( new InputSource( dataStream ), this );

        dataStream = classLoader.getResourceAsStream( TestData.XML_DATA );
        configValidator.validate( new InputSource( dataStream ), HANDLER, this );

        dataStream = classLoader.getResourceAsStream( TestData.XML_DATA );
        result = configValidator.validate( new InputSource( dataStream ) );
        verifyResult( result, true );

        dataStream = classLoader.getResourceAsStream( TestData.XML_DATA );
        result = configValidator.validate( new InputSource( dataStream ), HANDLER );
        verifyResult( result, true );

        dataStream = classLoader.getResourceAsStream( TestData.XML_DATA2 );
        try
        {
            configValidator.validate( dataStream, this );
            fail( "Expected fail to not being xml" );
        }
        catch( ValidateException e )
        {
        }

        dataStream = classLoader.getResourceAsStream( TestData.XML_DATA2 );
        try
        {
            configValidator.validate( dataStream, HANDLER, this );
            fail( "Expected fail to not being xml" );
        }
        catch( ValidateException e )
        {
        }

        dataStream = classLoader.getResourceAsStream( TestData.XML_DATA2 );
        result = configValidator.validate( dataStream );
        verifyResult( result, false );

        dataStream = classLoader.getResourceAsStream( TestData.XML_DATA2 );
        result = configValidator.validate( dataStream, HANDLER );
        verifyResult( result, false );

        dataStream = classLoader.getResourceAsStream( TestData.XML_DATA2 );
        try
        {
            configValidator.validate( new InputSource( dataStream ), this );
            fail( "Expected fail to not being xml" );
        }
        catch( ValidateException e )
        {
        }

        dataStream = classLoader.getResourceAsStream( TestData.XML_DATA2 );
        try
        {
            configValidator.validate( new InputSource( dataStream ), HANDLER, this );
            fail( "Expected fail to not being xml" );
        }
        catch( ValidateException e )
        {
        }

        dataStream = classLoader.getResourceAsStream( TestData.XML_DATA2 );
        result = configValidator.validate( new InputSource( dataStream ) );
        verifyResult( result, false );

        dataStream = classLoader.getResourceAsStream( TestData.XML_DATA2 );
        result = configValidator.validate( new InputSource( dataStream ), HANDLER );
        verifyResult( result, false );

        dataStream = classLoader.getResourceAsStream( TestData.XML_DATA3 );
        try
        {
            configValidator.validate( dataStream, this );
            fail( "Expected fail to not conforming" );
        }
        catch( ValidateException e )
        {
        }

        dataStream = classLoader.getResourceAsStream( TestData.XML_DATA3 );
        try
        {
            configValidator.validate( dataStream, HANDLER, this );
            fail( "Expected fail to not conforming" );
        }
        catch( ValidateException e )
        {
        }

        dataStream = classLoader.getResourceAsStream( TestData.XML_DATA3 );
        result = configValidator.validate( dataStream );
        verifyResult( result, false );

        dataStream = classLoader.getResourceAsStream( TestData.XML_DATA3 );
        result = configValidator.validate( dataStream, HANDLER );
        verifyResult( result, false );

        try
        {
            configValidator.validate( new InputSource( dataStream ), this );
            fail( "Expected fail to not conforming" );
        }
        catch( ValidateException e )
        {
        }

        try
        {
            configValidator.validate( new InputSource( dataStream ), HANDLER, this );
            fail( "Expected fail to not conforming" );
        }
        catch( ValidateException e )
        {
        }

        dataStream = classLoader.getResourceAsStream( TestData.XML_DATA3 );
        result = configValidator.validate( new InputSource( dataStream ) );
        verifyResult( result, false );

        dataStream = classLoader.getResourceAsStream( TestData.XML_DATA3 );
        result = configValidator.validate( new InputSource( dataStream ), HANDLER );
        verifyResult( result, false );

        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        final DocumentBuilder builder = factory.newDocumentBuilder();
        final Document document = builder.newDocument();

        try
        {
            configValidator.validate( document.createElement( "root" ), this );
            fail( "Expected fail to not conforming" );
        }
        catch( ValidateException e )
        {
        }
        result = configValidator.validate( document.createElement( "root" ) );
        verifyResult( result, false );

        try
        {
            configValidator.validate( (Node)null, this );
            fail( "Expected Null pointer due to null input" );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( npe.getMessage(), "node" );
        }

        try
        {
            configValidator.validate( (Node)null );
            fail( "Expected Null pointer due to null input" );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( npe.getMessage(), "node" );
        }

        try
        {
            configValidator.validate( (InputStream)null, HANDLER, this );
            fail( "Expected Null pointer due to null input" );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( npe.getMessage(), "inputStream" );
        }

        try
        {
            configValidator.validate( (InputStream)null, this );
            fail( "Expected Null pointer due to null input" );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( npe.getMessage(), "inputStream" );
        }

        try
        {
            configValidator.validate( (InputStream)null );
            fail( "Expected Null pointer due to null input" );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( npe.getMessage(), "inputStream" );
        }

        try
        {
            configValidator.validate( (InputStream)null, HANDLER );
            fail( "Expected Null pointer due to null input" );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( npe.getMessage(), "inputStream" );
        }

        try
        {
            configValidator.validate( (InputSource)null, this );
            fail( "Expected Null pointer due to null input" );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( npe.getMessage(), "source" );
        }

        try
        {
            configValidator.validate( (InputSource)null, HANDLER, this );
            fail( "Expected Null pointer due to null input" );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( npe.getMessage(), "source" );
        }

        try
        {
            configValidator.validate( (InputSource)null );
            fail( "Expected Null pointer due to null input" );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( npe.getMessage(), "source" );
        }

        try
        {
            configValidator.validate( (InputSource)null, HANDLER );
            fail( "Expected Null pointer due to null input" );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( npe.getMessage(), "source" );
        }
    }

    private void verifyResult( final ValidationResult result, final boolean success )
    {
        assertEquals( "Success?", success, result.isValid() );
        final ValidationIssue[] issues = result.getIssues();
        if( success )
        {
            assertTrue( "issue count = 0", 0 == issues.length );
        }
        for( int i = 0; i < issues.length; i++ )
        {
            assertNotNull( "issues[ i ].getException()", issues[ i ].getException() );
        }
    }

    private ClassLoader createClassLoader()
    {
        final URL url = getClass().getClassLoader().getResource( TestData.CATALOG_JAR );
        assertNotNull( "ResourcePresent: " + TestData.CATALOG_JAR, url );
        return new URLClassLoader( new URL[]{url} );
    }

    public void warning( SAXParseException exception )
        throws SAXException
    {
        if( DEBUG )
        {
            System.out.println( "ConfigValidatorTestCase.warning" );
            System.out.println( "exception = " + exception );
        }
    }

    public void error( SAXParseException exception )
        throws SAXException
    {
        if( DEBUG )
        {
            System.out.println( "ConfigValidatorTestCase.error" );
            System.out.println( "exception = " + exception );
        }
    }

    public void fatalError( SAXParseException exception )
        throws SAXException
    {
        if( DEBUG )
        {
            System.out.println( "ConfigValidatorTestCase.fatalError" );
            System.out.println( "exception = " + exception );
        }
    }

    static final class NoopContentHandler
        extends DefaultHandler
    {
    }
}

