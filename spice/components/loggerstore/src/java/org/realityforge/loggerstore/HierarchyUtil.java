/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.loggerstore;

import org.apache.avalon.excalibur.logger.DefaultLogTargetFactoryManager;
import org.apache.avalon.excalibur.logger.DefaultLogTargetManager;
import org.apache.avalon.excalibur.logger.LogTargetFactoryManageable;
import org.apache.avalon.excalibur.logger.LogTargetFactoryManager;
import org.apache.avalon.excalibur.logger.LogTargetManager;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.container.ContainerUtil;
import org.apache.avalon.framework.context.DefaultContext;
import org.apache.log.Hierarchy;
import org.apache.log.LogTarget;
import org.apache.log.Logger;
import org.apache.log.Priority;
import org.apache.log.util.Closeable;
import org.apache.log.util.LogKitAvalonLogger;

/**
 * HierarchyUtil is a utility class that allows the configuration
 * of a <code>Hierarchy</code> and the closure of its LogTargets.
 * Class is package-private and is used by LogKitLoggerStore.
 *
 * @author <a href="mailto:mauro.talevi at aquilonia.org">Mauro Talevi</a>
 */
class HierarchyUtil
{
    /**
     *  Private constructor to prevent instantiation of utility class
     */
    private HierarchyUtil()
    {
    }

    /**
     *  Configures a Hierarchy using the Configuration object provided.
     *
     * @param configuration  The configuration object.
     * @param hierarchy The hierarchy to configure
     * @throws Exception if the configuration is malformed
     */
    public static final void configure( final Configuration configuration, final Hierarchy hierarchy )
        throws Exception
    {
        final Configuration factories = configuration.getChild( "factories" );
        final LogTargetFactoryManager targetFactoryManager =
            createTargetFactoryManager( factories );

        final Configuration targets = configuration.getChild( "targets" );
        final LogTargetManager targetManager =
            createTargetManager( targets, targetFactoryManager );

        final Configuration categories = configuration.getChild( "categories" );
        configureLoggers( hierarchy,
                          targetManager,
                          null,
                          categories.getChildren( "category" ),
                          categories.getAttributeAsBoolean( "additive", false ) );
    }

    /**
     * Closes the LogTargets of all the Loggers of the Hierarchy
     *
     * @param hierarchy The hierarchy containing the LogTargets to close
     */
    public static final void closeLogTargets( final Hierarchy hierarchy )
    {
        closeLogTargets( hierarchy.getRootLogger() );
    }

    /**
     * Creates a LogTargetFactoryManager
     *
     * @param configuration  The configuration object.
     * @throws Exception if the configuration is malformed
     */
    private static final LogTargetFactoryManager
        createTargetFactoryManager( final Configuration configuration )
        throws Exception
    {
        final DefaultLogTargetFactoryManager targetFactoryManager = new DefaultLogTargetFactoryManager();

        final Hierarchy hierarchy = new Hierarchy();

        ContainerUtil.enableLogging( targetFactoryManager, new LogKitAvalonLogger( hierarchy.getRootLogger() ) );

        ContainerUtil.contextualize( targetFactoryManager, new DefaultContext() );

        ContainerUtil.configure( targetFactoryManager, configuration );

        return targetFactoryManager;
    }

    /**
     * Create a LogTargetManager
     *
     * @param configuration  The configuration object.
     * @param targetFactoryManager the LogTargetFactoryManager
     * @throws Exception LogTargetManager cannot be created
     */
    private static final LogTargetManager createTargetManager(
        final Configuration configuration,
        final LogTargetFactoryManager targetFactoryManager )
        throws Exception
    {
        final DefaultLogTargetManager targetManager = new DefaultLogTargetManager();

        final Hierarchy hierarchy = new Hierarchy();

        ContainerUtil.enableLogging( targetManager, new LogKitAvalonLogger( hierarchy.getRootLogger() ) );

        if( targetManager instanceof LogTargetFactoryManageable )
        {
            targetManager.setLogTargetFactoryManager( targetFactoryManager );
        }

        ContainerUtil.configure( targetManager, configuration );

        return targetManager;
    }

    /**
     * Configure Loggers for a given Hierarchy.
     * Recursives configures Loggers for all the sub categories.
     *
     * @param hierarchy the Hierarchy of Loggers
     * @param targetManager the LogTargetManager
     * @param categories The array of configuration objects for the logger categories.
     * @param parentCategory the parentCategory or <code>null</code> if none.
     * @param defaultAdditive the Logger default additivity boolean flag
     * @throws Exception if configuration is malformed
     */
    private static final void configureLoggers( final Hierarchy hierarchy,
                                                final LogTargetManager targetManager,
                                                final String parentCategory,
                                                final Configuration[] categories,
                                                final boolean defaultAdditive )
        throws Exception
    {
        for( int i = 0; i < categories.length; i++ )
        {
            final String name = categories[ i ].getAttribute( "name" );
            final String loglevel = categories[ i ].getAttribute( "log-level" ).toUpperCase();
            final boolean additive = categories[ i ].getAttributeAsBoolean( "additive",
                                                                            defaultAdditive );

            final Configuration[] targets = categories[ i ].getChildren( "log-target" );
            final LogTarget[] logTargets = new LogTarget[ targets.length ];
            for( int j = 0; j < targets.length; j++ )
            {
                final String id = targets[ j ].getAttribute( "id-ref" );
                logTargets[ j ] = targetManager.getLogTarget( id );
            }

            if( "".equals( name ) && logTargets.length > 0 )
            {
                hierarchy.setDefaultPriority( Priority.getPriorityForName( loglevel ) );
                hierarchy.setDefaultLogTargets( logTargets );
            }

            final String category = getFullCategoryName( parentCategory, name );
            final org.apache.log.Logger logger = hierarchy.getLoggerFor( category );

            logger.setPriority( Priority.getPriorityForName( loglevel ) );
            logger.setLogTargets( logTargets );
            logger.setAdditivity( additive );

            final Configuration[] subCategories = categories[ i ].getChildren( "category" );
            if( null != subCategories )
            {
                configureLoggers( hierarchy, targetManager, category, subCategories, defaultAdditive );
            }
        }
    }

    /**
     * Closes all the LogTargets of a given logger.
     * Recursives closes the LogTargets of all the children loggers.
     *
     * @param logger the Logger
     */
    private static final void closeLogTargets( final Logger logger )
    {
        final LogTarget[] targets = logger.getLogTargets();
        for( int i = 0; i < targets.length; i++ )
        {
            if( targets[ i ] instanceof Closeable )
            {
                ( (Closeable)targets[ i ] ).close();
            }
        }

        final Logger[] children = logger.getChildren();
        for( int i = 0; i < children.length; i++ )
        {
            closeLogTargets( children[ i ] );
        }
    }

    /**
     * Generates a full category from the category parent and child.
     *
     * @param parent the parent category name.
     * @param name the child category name.
     */
    private static final String getFullCategoryName( String parent, String name )
    {
        if( ( null == parent ) || ( parent.length() == 0 ) )
        {
            if( name == null )
            {
                return "";
            }
            else
            {
                return name;
            }
        }
        else
        {
            if( ( null == name ) || ( name.length() == 0 ) )
            {
                return parent;
            }
            else
            {
                return parent + org.apache.log.Logger.CATEGORY_SEPARATOR + name;
            }
        }
    }

}
