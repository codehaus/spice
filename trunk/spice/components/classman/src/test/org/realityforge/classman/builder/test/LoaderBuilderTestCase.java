/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.classman.builder.test;

import java.io.File;
import java.util.HashMap;
import org.realityforge.classman.builder.LoaderBuilder;
import org.realityforge.classman.builder.SimpleLoaderResolver;
import org.realityforge.classman.metadata.ClassLoaderMetaData;
import org.realityforge.classman.metadata.ClassLoaderSetMetaData;
import org.realityforge.classman.metadata.FileSetMetaData;
import org.realityforge.classman.metadata.JoinMetaData;
import org.realityforge.classman.test.AbstractLoaderTestCase;
import org.realityforge.classman.test.DataConstants;
import org.realityforge.extension.Extension;

/**
 * Unit test for join classloader.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.3 $ $Date: 2003-08-01 02:51:54 $
 */
public class LoaderBuilderTestCase
    extends AbstractLoaderTestCase
{
    public LoaderBuilderTestCase( String name )
    {
        super( name );
    }

    public void testMissingClassLoader()
        throws Exception
    {
        final LoaderBuilder builder = new LoaderBuilder();
        final ClassLoaderMetaData classLoader =
            new ClassLoaderMetaData( "blah",
                                     "bad-parent",
                                     new String[ 0 ],
                                     new Extension[ 0 ],
                                     new FileSetMetaData[ 0 ] );
        final ClassLoaderSetMetaData metadata =
            new ClassLoaderSetMetaData( "blah",
                                        new String[ 0 ],
                                        new ClassLoaderMetaData[]{classLoader},
                                        new JoinMetaData[ 0 ] );
        final SimpleLoaderResolver resolver = new SimpleLoaderResolver( null );
        final HashMap predefined = new HashMap();
        try
        {
            builder.buildClassLoaders( metadata, resolver, predefined );
            fail( "Expected to fail as unable to find classloader 'bad-parent'" );
        }
        catch( Exception e )
        {
            assertTrue( "Exception message", -1 != e.getMessage().indexOf( "bad-parent" ) );
            return;
        }
    }

    public void testMissingPredefined()
        throws Exception
    {
        final LoaderBuilder builder = new LoaderBuilder();
        final ClassLoaderSetMetaData metadata =
            new ClassLoaderSetMetaData( "*system*",
                                        new String[]{"*system*"},
                                        new ClassLoaderMetaData[ 0 ],
                                        new JoinMetaData[ 0 ] );
        final SimpleLoaderResolver resolver = new SimpleLoaderResolver( null );
        final HashMap predefined = new HashMap();
        try
        {
            builder.buildClassLoaders( metadata, resolver, predefined );
            fail( "Expected to fail as predefined list did " +
                  "not container predefined specified in metadata" );
        }
        catch( Exception e )
        {
            assertTrue( "Exception message", -1 != e.getMessage().indexOf( "*system*" ) );
            return;
        }
    }

    public void testExtensions()
        throws Exception
    {
        final LoaderBuilder builder = new LoaderBuilder();
        final ClassLoaderMetaData classLoader =
            new ClassLoaderMetaData( "ext",
                                     "*system*",
                                     new String[ 0 ],
                                     new Extension[]{DataConstants.EXTENSION},
                                     new FileSetMetaData[ 0 ] );
        final ClassLoaderSetMetaData metadata =
            new ClassLoaderSetMetaData( "ext",
                                        new String[]{"*system*"},
                                        new ClassLoaderMetaData[]{classLoader},
                                        new JoinMetaData[ 0 ] );
        final SimpleLoaderResolver resolver = new TestLoaderResolver( new File( "." ) );
        final HashMap predefined = new HashMap();
        predefined.put( "*system*", ClassLoader.getSystemClassLoader() );
        try
        {
            builder.buildClassLoaders( metadata, resolver, predefined );
        }
        catch( Exception e )
        {
            fail( "Unexpected failure to build classloaders with extension: " + e );
        }
    }
}
