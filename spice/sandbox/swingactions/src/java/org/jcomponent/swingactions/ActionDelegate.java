/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */

package org.jcomponent.swingactions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.EventListenerList;

/**
 * ActionDelegate mantains the list of all the <code>ActionListener</code>s for
 * a given <code>Action</code> and is responsible for notifying them when the
 * action is fired.
 *
 * @author <a href="mailto:mauro.talevi at aquilonia.org">Mauro Talevi</a>
 */
class ActionDelegate
{
    /** List of all the ActionListeners */
    private EventListenerList m_listeners;

    /**
     * Add a ActionListener to the listener list.
     * The listener is registered for all properties.
     *
     * @param listener  The ActionListener to be added
     */
    public synchronized void addActionListener( final ActionListener listener )
    {
        if( m_listeners == null )
        {
            m_listeners = new EventListenerList();
        }
        m_listeners.add( ActionListener.class, listener );
    }

    /**
     * Remove a ActionListener from the listener list.
     * This removes a ActionListener that was registered for all actions.
     *
     * @param listener  The ActionListener to be removed
     */
    public synchronized void removeActionListener( final ActionListener listener )
    {
        if( m_listeners == null )
        {
            return;
        }
        m_listeners.remove( ActionListener.class, listener );
    }

    /**
     * Returns the array of registers listeners
     */
    public synchronized ActionListener[] getListeners()
    {
        if( m_listeners == null )
        {
            return new ActionListener[]{};
        }
        return (ActionListener[])m_listeners.getListeners( ActionListener.class );
    }

    /**
     * Fire an ActionEvent to any registered listeners.
     *
     * @param event  The ActionEvent
     */
    public void fireActionEvent( ActionEvent event )
    {
        final ActionListener[] targets = getListeners();
        for( int i = 0; i < targets.length; i++ )
        {
            targets[ i ].actionPerformed( event );
        }
    }
}