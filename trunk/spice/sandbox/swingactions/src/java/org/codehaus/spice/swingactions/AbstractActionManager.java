/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.swingactions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.NoSuchElementException;
import javax.swing.Action;
import javax.swing.ActionMap;

/**
 * <p>ActionActionManager is an abstract implementation of <code>ActionManager</code> which
 * is common to all configurations.
 *
 * @author Mauro Talevi
 */
public abstract class AbstractActionManager
    implements ActionManager
{
    /**
     * Empty set of Action objects.
     */
    private static final Action[] EMPY_ACTIONS = new Action[ 0 ];

    /** Action map */
    private final ActionMap m_actions = new ActionMap();

    /** Delegate map */
    private final HashMap m_delegates = new HashMap();

    /**
     * Enables or disables an Action
     *
     * @param actionId the Id of the Action
     * @param enabled the boolean flag -
     *    <code>true</code> if Action is enabled
     */
    public void enableAction( final Object actionId,
                              final boolean enabled )
    {
        try
        {
            final Action action = getAction( actionId );
            action.setEnabled( enabled );
        }
        catch( NoSuchElementException e )
        {
            // do nothing, Action is not in the map
        }
    }

    /**
     * Returns the Action registered with the ActionManager
     *
     * @param actionId the Id of the Action
     */
    public synchronized Action getAction( final Object actionId )
        throws NoSuchElementException
    {
        final Action action = m_actions.get( actionId );
        if( action == null )
        {
            final String message = "Cannot find action for id " + actionId;
            throw new NoSuchElementException( message );
        }
        return action;
    }

    /**
     * Returns all the Actions registered with the ActionManager
     */
    public synchronized Action[] getActions()
    {
        final Object[] keys = m_actions.keys();
        if( keys == null )
        {
            return EMPY_ACTIONS;
        }
        final Action[] array = new Action[ keys.length ];
        for( int i = 0; i < keys.length; i++ )
        {
            array[ i ] = m_actions.get( keys[ i ] );
        }
        return array;
    }

    /**
     * Adds an Action to the ActionManager
     *
     * @param actionId the Id of the Action
     * @param action the Action being added
     */
    protected synchronized void addAction( final Object actionId,
                                           final Action action )
    {
        m_actions.put( actionId, action );
    }

    /**
     * Removed an Action from the ActionManager
     * @param actionId the Id of the Action
     */
    protected synchronized void removeAction( final Object actionId )
    {
        m_actions.remove( actionId );
    }

    /**
     * Adds an ActionListener which handles a given Action
     *
     * @param actionId the Id of the Action
     * @param handler the ActionListener which handles the Action
     */
    public void addHandler( Object actionId, ActionListener handler )
    {
        final ActionDelegate delegate = getActionDelegate( actionId );
        delegate.addActionListener( handler );
    }

    /**
     * Removes a handler for a given Action
     *
     * @param actionId the Id of the Action
     * @param handler the ActionListener which handles the Action
     */
    protected void removeHandler( Object actionId, ActionListener handler )
    {
        final ActionDelegate delegate = getActionDelegate( actionId );
        delegate.removeActionListener( handler );
    }

    /**
     * Fires ActionEvent wihch will be handled by the
     * registered handlers.
     *
     * @param actionId the Id of the Action being executed
     * @param actionEvent the ActionEvent being fired
     */
    public void fireActionEvent( final Object actionId,
                                 final ActionEvent actionEvent )
    {
        final ActionDelegate delegate = getActionDelegate( actionId );
        delegate.fireActionEvent( actionEvent );
    }

    /**
     * Returns the ActionDelegate for a given Action.
     * If no delegate is found, one is created
     *
     * @param actionId the Id of the Action
     * @return the ActionDelegate
     */
    protected synchronized ActionDelegate getActionDelegate( final Object actionId )
    {
        if( actionId == null )
        {
            throw new NullPointerException( "actionId" );
        }
        ActionDelegate delegate = (ActionDelegate)m_delegates.get( actionId );
        if( delegate == null )
        {
            delegate = new ActionDelegate();
            m_delegates.put( actionId, delegate );
        }
        return delegate;
    }
}
