/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.swingactions;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;

import org.codehaus.spice.swingactions.ActionManager;
import org.codehaus.spice.swingactions.ActionManagerFactory;

import junit.framework.TestCase;

/**
 * AbstactTestCase
 *
 * @author Mauro Talevi
 */
public class AbstractTestCase
    extends TestCase
{
    protected static final String MESSAGE = "Testing Action";
    protected static final String MESSAGE2 = "Testing Action 2";
    
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
                        config,
                        outputFile );
        final HashMap config2 = new HashMap();
        config2.put( URL.class.getName(), url );
        config2.putAll( inputData );
        runFactoryTest( factory,
                        config2,
                        outputFile );
        final String filename = url.toExternalForm().substring( 5 );
        final HashMap config3 = new HashMap();
        config3.put( ActionManagerFactory.FILE_LOCATION, filename );
        config3.putAll( inputData );
        runFactoryTest( factory,
                        config3,
                        outputFile );
        final HashMap config4 = new HashMap();
        config4.put( File.class.getName(), new File( filename ) );
        config4.putAll( inputData );
        runFactoryTest( factory,
                        config4,
                        outputFile );
        final HashMap config5 = new HashMap();
        config5.put( InputStream.class.getName(),
                     new FileInputStream( filename ) );
        config5.putAll( inputData );
        runFactoryTest( factory,
                        config5,
                        outputFile );
    }

    protected void runFactoryTest( final ActionManagerFactory factory, final HashMap config, final String filename ) throws Exception
    {
        final ActionManager manager = factory.createActionManager( config );
    }

    protected void runManagerTest( final ActionManager manager )
    {
    }
}
