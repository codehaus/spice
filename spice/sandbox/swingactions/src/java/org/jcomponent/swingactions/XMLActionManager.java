/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.swingactions;

import java.io.InputStream;

import javax.swing.Action;

import org.jcomponent.swingactions.metadata.ActionGroupsMetaData;
import org.jcomponent.swingactions.metadata.ActionMetaData;
import org.jcomponent.swingactions.metadata.ActionSetMetaData;
import org.jcomponent.swingactions.reader.ConfigReader;

/**
 * XMLActionManager is an implementation of <code>ActionManager</code> which
 * supports configuration via XML.
 *
 * @author <a href="mailto:mauro.talevi at aquilonia.org">Mauro Talevi</a>
 */
public class XMLActionManager
    extends AbstractActionManager
{

    /** 
     * Creates an ActionManager from an XML resource 
     * @param resource the InputStream containing the XML configuration
     * @throws Exception if the configuration fails
     */
    public XMLActionManager( final InputStream resource ) 
        throws Exception
    {
        if ( resource == null )
        {
            throw new NullPointerException( "resource" );
        }
        final ConfigReader reader = new ConfigReader();
        reader.parse( resource );
        configureActions( reader.getActionSet() );
        configureGroups( reader.getActionGroups() );
    }

    /**
     * Configures ActionManager with the given Action set
     * @param actionSet the ActionSetMetaData
     */
    private void configureActions( final ActionSetMetaData actionSet )
    {
        final ActionMetaData[] actions = actionSet.getActions();
        for ( int i = 0; i < actions.length; i++ )
        {
            final Action action = new ActionAdapter( actions[ i ], this );  
            addAction( actions[ i ].getValue( ActionMetaData.ID ), action );
        }
    }

    /**
     * Configures ActionManager with the given Action groups
     * @param actionGroups the ActionGroupsMetaData
     */
    private void configureGroups( final ActionGroupsMetaData actionGroups )
    {
        //TODO construct JFactory with actionGroups
    }
}

