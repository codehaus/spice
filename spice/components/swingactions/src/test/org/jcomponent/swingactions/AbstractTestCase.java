/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.swingactions;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;

import junit.framework.TestCase;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

/**
 * AbstactTestCase
 *
 * @author <a href="mailto:mauro.talevi at aquilonia.org">Mauro Talevi</a>
 */
public class AbstractTestCase
    extends TestCase
{
    protected static final String MESSAGE = "Testing Logger";
    protected static final String MESSAGE2 = "This occurs in sub-category";

    private File actionsDir;

    public AbstractTestCase( final String name )
    {
        super( name );
    }

    protected void setUp() throws Exception
    {
        actionsDir = new File( "actions/" );
        actionsDir.mkdirs();
    }


    protected final InputStream getResource( final String name )
    {
        return getClass().getResourceAsStream( name );
    }



    /**
     *  Builds an Element from a resource
     *  @param resource the InputStream of the configuration resource
     *  @param resolver the EntityResolver required by the DocumentBuilder -
     *                  or <code>null</code> if none required
     *  @param systemId the String encoding the systemId required by the InputSource -
     *                  or <code>null</code> if none required
     */
    protected static Element buildElement( final InputStream resource,
                                         final EntityResolver resolver,
                                         final String systemId )
        throws Exception
    {
        DocumentBuilderFactory dbf = null;
        try
        {
            dbf = DocumentBuilderFactory.newInstance();
        }
        catch( FactoryConfigurationError e )
        {
            final String message = "Failed to create a DocumentBuilderFactory";
            throw new Exception( message, e );
        }

        try
        {
            dbf.setValidating( true );
            DocumentBuilder db = dbf.newDocumentBuilder();
            if( resolver != null )
            {
                db.setEntityResolver( resolver );
            }
            InputSource source = new InputSource( resource );
            if( systemId != null )
            {
                source.setSystemId( systemId );
            }
            Document doc = db.parse( source );
            return doc.getDocumentElement();
        }
        catch( Exception e )
        {
            final String message = "Failed to parse Document";
            throw new Exception( message, e );
        }
    }


    protected void runInvalidInputData( final ActionManagerFactory factory )
    {
        try
        {
            factory.createActionManager( new HashMap() );
            fail( "Expected createActionManager to fail with invalid input data" );
        }
        catch( Exception e )
        {
        }
    }

    protected void runStreamBasedFactoryTest( final String inputFile,
                                            final ActionManagerFactory factory,
                                            final int level,
                                            final String outputFile,
                                            final HashMap inputData )
        throws Exception
    {
        //URL Should in file: format
        final URL url = getClass().getResource( inputFile );
        assertEquals( "URL is of file type", url.getProtocol(), "file" );

        final HashMap config = new HashMap();
        config.put( ActionManagerFactory.URL_LOCATION,
                    url.toExternalForm() );
        config.putAll( inputData );
        runFactoryTest( factory,
                        level,
                        config,
                        outputFile );
        final HashMap config2 = new HashMap();
        config2.put( URL.class.getName(), url );
        config2.putAll( inputData );
        runFactoryTest( factory,
                        level,
                        config2,
                        outputFile );
        final String filename = url.toExternalForm().substring( 5 );
        final HashMap config3 = new HashMap();
        config3.put( ActionManagerFactory.FILE_LOCATION, filename );
        config3.putAll( inputData );
        runFactoryTest( factory,
                        level,
                        config3,
                        outputFile );
        final HashMap config4 = new HashMap();
        config4.put( File.class.getName(), new File( filename ) );
        config4.putAll( inputData );
        runFactoryTest( factory,
                        level,
                        config4,
                        outputFile );
        final HashMap config5 = new HashMap();
        config5.put( InputStream.class.getName(),
                     new FileInputStream( filename ) );
        config5.putAll( inputData );
        runFactoryTest( factory,
                        level,
                        config5,
                        outputFile );
    }

    protected void runFactoryTest( final ActionManagerFactory factory, final int level, final HashMap config, final String filename ) throws Exception
    {
        final ActionManager manager = factory.createActionManager( config );
    }
}
