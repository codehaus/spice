/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.swingactions;

import java.awt.event.ActionEvent;
import java.net.URL;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

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
        m_metadata = metadata;
        setActionValues( metadata );       
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
     * Sets Action values using the metadata
     * @param metadata the ActionMetaData
     */
    private void setActionValues( final ActionMetaData metadata ){
        String[] keys = metadata.getKeys();
        for ( int i = 0; i < keys.length; i++ )
        {
            final String key = keys[ i ];
            final String value = metadata.getValue( key );
            if ( key.equals( ActionMetaData.SMALL_ICON ) 
              || key.equals( ActionMetaData.LARGE_ICON ) )
            {
                putValue( key, createIcon( value ) );            
            } else if ( key.equals( ActionMetaData.MNEMONIC_KEY ) )
            {
                putValue( key, createCharacter( value ) );
            } else if ( key.equals( ActionMetaData.ACCELERATOR_KEY ) )
            {
                putValue( key, createKeyStroke( value ) );
            } else
            {
                putValue( key, value );
            }
        }        
    }
    
    /**
     *  Creates Icon from value
     */
    private Icon createIcon( final String value )
    {
        if ( value == null )
        {
            return null;
        }
        final URL resource = getClass().getResource( value );
        if ( resource != null ) {
            return new ImageIcon( resource );
        }
        return null;
    }

    /**
     *  Creates Character from value
     */
    private Character createCharacter( final String value )
    {
        if ( value == null )
        {
            return null;
        }
        if ( value.length() > 0 ){
            return new Character( value.charAt( 0 ) );
        } else {
            return null;
        }
    }

    /**
     *  Creates KeyStroke from value
     */
    private KeyStroke createKeyStroke( final String value )
    {
        if ( value == null )
        {
            return null;
        }
        return KeyStroke.getKeyStroke( value );
    }
}