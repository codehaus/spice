/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.classman.builder;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.realityforge.classman.metadata.ClassLoaderMetaData;
import org.realityforge.classman.metadata.ClassLoaderSetMetaData;
import org.realityforge.classman.metadata.FileSetMetaData;
import org.realityforge.classman.metadata.JoinMetaData;
import org.realityforge.extension.Extension;

/**
 * This class is responsible for building the set of actual {@link
 * java.lang.ClassLoader}s out of the {@link ClassLoaderSetMetaData} objects.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.5 $ $Date: 2003-12-02 07:28:52 $
 */
public class LoaderBuilder
{
    /**
     * Build a map of all the classloaders defined by the metadata.
     *
     * @param metadata the metadata
     * @param resolver the reoslver to use to resolve files/urls etc
     * @param predefined the map containing all the predefined classloaders.
     * @return the map of created classloaders
     * @throws Exception if unable to create some classloaders
     */
    public Map buildClassLoaders( final ClassLoaderSetMetaData metadata,
                                  final LoaderResolver resolver,
                                  final Map predefined )
        throws Exception
    {
        final HashMap classLoaders = new HashMap();

        addPredefined( metadata.getPredefined(), predefined, classLoaders );
        addAllClassLoaders( metadata, resolver, classLoaders );
        addAllJoins( metadata, resolver, classLoaders );

        return classLoaders;
    }

    /**
     * Add all the classloaders to the result metaData.
     *
     * @param metaData the metadata
     * @param resolver the resolver to use
     * @param classLoaders the result map of classloaders
     * @throws Exception if unable to create the classloaders
     */
    private void addAllClassLoaders( final ClassLoaderSetMetaData metaData,
                                     final LoaderResolver resolver,
                                     final Map classLoaders )
        throws Exception
    {
        final ClassLoaderMetaData[] classLoaderDefs = metaData.getClassLoaders();
        for( int i = 0; i < classLoaderDefs.length; i++ )
        {
            final String name = classLoaderDefs[ i ].getName();
            processClassLoader( name, metaData, resolver, classLoaders );
        }
    }

    /**
     * Add all the classloaders to the result metaData.
     *
     * @param metaData the metadata
     * @param resolver the resolver to use
     * @param classLoaders the result map of classloaders
     * @throws Exception if unable to create the classloaders
     */
    private void addAllJoins( final ClassLoaderSetMetaData metaData,
                              final LoaderResolver resolver,
                              final Map classLoaders )
        throws Exception
    {
        final JoinMetaData[] joins = metaData.getJoins();
        for( int i = 0; i < joins.length; i++ )
        {
            final String name = joins[ i ].getName();
            processClassLoader( name, metaData, resolver, classLoaders );
        }
    }

    /**
     * Process a ClassLoader entry, create ClassLoader and add it to result
     * map.
     *
     * @param name the name of classloader
     * @param set the set of metatadata
     * @param resolver the resolver for construction
     * @param classLoaders the result map of classloaders
     * @throws Exception if unable to process classloader
     */
    private void processClassLoader( final String name,
                                     final ClassLoaderSetMetaData set,
                                     final LoaderResolver resolver,
                                     final Map classLoaders )
        throws Exception
    {
        if( classLoaders.containsKey( name ) )
        {
            return;
        }

        final ClassLoaderMetaData regular = set.getClassLoader( name );
        if( null != regular )
        {
            //Make sure parent classloader is built
            processClassLoader( regular.getParent(),
                                set,
                                resolver,
                                classLoaders );

            final ClassLoader classLoader =
                buildRegularClassLoader( regular, resolver, classLoaders );
            classLoaders.put( name, classLoader );
        }
        else
        {
            final JoinMetaData join = set.getJoin( name );
            if( null == join )
            {
                final String message = "Unknown classloader " + name;
                throw new Exception( message );
            }
            //Make sure all our dependencies are processed
            final String[] names = join.getClassloaders();
            for( int i = 0; i < names.length; i++ )
            {
                processClassLoader( names[ i ], set, resolver, classLoaders );
            }
            final ClassLoader classLoader =
                buildJoinClassLoader( join, resolver, classLoaders );
            classLoaders.put( name, classLoader );
        }
    }

    /**
     * Create a Join ClassLoader.
     *
     * @param join the metadata
     * @param resolver the resolver to use to resolve URLs etc
     * @param classLoaders the already created ClassLoaders
     * @return the created JoinClassLoader
     */
    private ClassLoader buildJoinClassLoader( final JoinMetaData join,
                                              final LoaderResolver resolver,
                                              final Map classLoaders )
        throws Exception
    {
        final ArrayList list = new ArrayList();
        final String[] names = join.getClassloaders();
        for( int i = 0; i < names.length; i++ )
        {
            list.add( classLoaders.get( names[ i ] ) );
        }

        final ClassLoader[] elements =
            (ClassLoader[])list.toArray( new ClassLoader[ list.size() ] );

        return resolver.createJoinClassLoader( elements );
    }

    /**
     * Build a regular classloader with entries, extensions and filesets fully
     * resolved.
     *
     * @param metaData the classloader definition
     * @param resolver the resolver to use when creating URLs/Extensions etc
     * @param classLoaders the already created ClassLoaders
     * @return the created ClassLoader
     * @throws Exception if unable to create ClassLoader
     */
    private ClassLoader buildRegularClassLoader(
        final ClassLoaderMetaData metaData,
        final LoaderResolver resolver,
        final Map classLoaders )
        throws Exception
    {
        final ClassLoader parent =
            (ClassLoader)classLoaders.get( metaData.getParent() );
        final ArrayList urlSet = new ArrayList();

        final String[] entrys = metaData.getEntrys();
        for( int i = 0; i < entrys.length; i++ )
        {
            final URL url = resolver.resolveURL( entrys[ i ] );
            urlSet.add( url );
        }

        //Add all filesets defined for classloader
        final FileSetMetaData[] filesets = metaData.getFilesets();
        for( int i = 0; i < filesets.length; i++ )
        {
            final FileSetMetaData fileset = filesets[ i ];
            final String baseDirectory = fileset.getBaseDirectory();
            final String[] includes = fileset.getIncludes();
            final String[] excludes = fileset.getExcludes();
            final URL[] urls =
                resolver.resolveFileSet( baseDirectory,
                                         includes,
                                         excludes );
            for( int j = 0; j < urls.length; j++ )
            {
                urlSet.add( urls[ j ] );
            }
        }

        final Extension[] extensions = metaData.getExtensions();
        for( int i = 0; i < extensions.length; i++ )
        {
            final URL url = resolver.resolveExtension( extensions[ i ] );
            urlSet.add( url );
        }

        final URL[] urls = (URL[])urlSet.toArray( new URL[ urlSet.size() ] );
        return resolver.createClassLoader( parent, urls );
    }

    /**
     * Add all the predefined classloaders to the set of available
     * ClassLoaders.
     *
     * @param entrys the names of predefined classloaders
     * @param predefined the map containg predefined classloaders
     * @param classLoaders the destination map of classloaders
     */
    private void addPredefined( final String[] entrys,
                                final Map predefined,
                                final HashMap classLoaders )
    {
        for( int i = 0; i < entrys.length; i++ )
        {
            final String name = entrys[ i ];
            final ClassLoader classLoader =
                (ClassLoader)predefined.get( name );
            if( null == classLoader )
            {
                final String message =
                    "Missing predefined ClassLoader " + name;
                throw new IllegalArgumentException( message );
            }
            classLoaders.put( name, classLoader );
        }
    }
}