/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.swingactions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.jcomponent.swingactions.metadata.ActionMetaData;
/**
 * ActionAdapter is an adapter of the Action interface
 * which uses the ActionManager to fire ActionEvents for the given action.
 *
 * It also provides utility access methods for fields defined in Action.
 *
 * @author <a href="mailto:mauro.talevi at aquilonia.org">Mauro Talevi</a>
 */
public class ActionAdapter extends AbstractAction
{

    /** The ActionMetaData describing this Action */
    private final ActionMetaData m_metadata;
    /** The ActionManager with which the Action is being registered */
    private final ActionManager m_manager;

    /**
     * Creates an ActionAdapter
     *
     * @param metadata the ActionMetaData describing this Action
     * @param manager the ActionManager with which this Action is being registered
     */
    public ActionAdapter( final ActionMetaData metadata,
                          final ActionManager manager )
    {
        super();
        if ( metadata == null )
        {
            throw new NullPointerException( "metadata" );
        }
        addMetaData( metadata );       
        m_metadata = metadata;
        if ( manager == null )
        {
            throw new NullPointerException( "manager" );
        }
        m_manager = manager;
    }

    /**
     * Returns the ActionMetaData
     */
    public ActionMetaData getMetaData()
    {
        return m_metadata;
    }


    /**
     * Adapter implementation of ActionListener, which
     * uses the ActionManager to fire the ActionEvent to the
     * registered handlers.
     */
    public void actionPerformed( final ActionEvent event )
    {
        m_manager.fireActionEvent( m_metadata.getValue( ActionMetaData.ID ), event );
    }

    /**
     * Adds metadata to the Action implementation
     * @param metadata the ActionMetaData
     */
    private void addMetaData( final ActionMetaData metadata ){
        String[] keys = metadata.getKeys();
        for ( int i = 0; i < keys.length; i++ )
        {
            putValue( keys[i], metadata.getValue( keys[i] ) );            
        }        
    }
}