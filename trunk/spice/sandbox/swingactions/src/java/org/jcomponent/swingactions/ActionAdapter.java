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
import javax.swing.Icon;
import javax.swing.KeyStroke;

/**
 * ActionAdapter is an adapter of the Action interface
 * which uses the ActionManager to fire ActionEvents for the given action.
 *
 * It also provides utility access methods for fields defined in Action.
 *
 * @author <a href="mailto:mauro.talevi at aquilonia.org">Mauro Talevi</a>
 */
public class ActionAdapter
    extends AbstractAction
{
    /** The key used for storing a large icon for the action */
    public static final String LARGE_ICON = "LargeIcon";
    /** The key used for storing the version for the action */
    public static final String VERSION = "Version";

    /** The unique key identifying this Action */
    private Object m_id;

    /** The ActionManager with which the Action is being registered */
    private ActionManager m_manager;

    /**
     * Creates an ActionAdapter
     *
     * @param id the unique key identifying this Action
     * @param manager the ActionManager with which this Action is being registered
     */
    public ActionAdapter( final Object id,
                          final XMLActionManager manager )
    {
        super();
        m_id = id;
        m_manager = manager;
    }

    /**
     * Returns the Action name
     */
    public String getName()
    {
        return (String)getValue( NAME );
    }

    /**
     * Sets the Action name
     */
    public void setName( String name )
    {
        putValue( NAME, name );
    }

    /**
     * Returns the Action Command
     */
    public String getActionCommand()
    {
        return (String)getValue( ACTION_COMMAND_KEY );
    }

    /**
     * Sets the Action Command
     * @param actionCommand the command String for the Action event
     */
    public void setActionCommand( String actionCommand )
    {
        putValue( ACTION_COMMAND_KEY, actionCommand );
    }

    /**
     * Returns the Action shortDescription
     */
    public String getShortDescription()
    {
        return (String)getValue( SHORT_DESCRIPTION );
    }

    /**
     * Sets the Action shortDescription
     */
    public void setShortDescription( String shortDescription )
    {
        putValue( SHORT_DESCRIPTION, shortDescription );
    }

    /**
     * Returns the Action longDescription
     */
    public String getLongDescription()
    {
        return (String)getValue( LONG_DESCRIPTION );
    }

    /**
     * Sets the Action longDescription
     */
    public void setLongDescription( String longDescription )
    {
        putValue( LONG_DESCRIPTION, longDescription );
    }

    /**
     * Returns the Action largeIcon
     */
    public Icon getLargeIcon()
    {
        return (Icon)getValue( LARGE_ICON );
    }

    /**
     * Sets the Action largeIcon
     */
    public void setLargeIcon( Icon largeIcon )
    {
        putValue( LARGE_ICON, largeIcon );
    }

    /**
     * Returns the Action smallIcon
     */
    public Icon getSmallIcon()
    {
        return (Icon)getValue( SMALL_ICON );
    }

    /**
     * Sets the Action smallIcon
     */
    public void setSmallIcon( Icon smallIcon )
    {
        putValue( SMALL_ICON, smallIcon );
    }

    /**
     * Returns the Action mnemonic
     */
    public Character getMnemonic()
    {
        return (Character)getValue( MNEMONIC_KEY );
    }

    /**
     * Sets the Action mnemonic
     */
    public void setMnemonic( Character mnemonic )
    {
        putValue( MNEMONIC_KEY, mnemonic );
    }

    /**
     * Returns the Action accelerator
     */
    public KeyStroke getAccelerator()
    {
        return (KeyStroke)getValue( ACCELERATOR_KEY );
    }

    /**
     * Sets the Action accelerator
     */
    public void setAccelerator( KeyStroke keyStroke )
    {
        putValue( ACCELERATOR_KEY, keyStroke );
    }

    /**
     * Adapter implementation of ActionListener, which
     * uses the ActionManager to fire the ActionEvent to the
     * registered handlers.
     */
    public void actionPerformed( final ActionEvent event )
    {
        m_manager.fireActionEvent( m_id, event );
    }
}