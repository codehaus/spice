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
import java.util.NoSuchElementException;
import javax.swing.Action;

/**
 * <p>ActionManager is responsible for managing a set of Actions.
 * Each <code>Action</code> is uniquely identified by an <code>actionId</code>
 * which will tipically be <code>String</code> or a <code>Class</code>,
 * but may be any <code>Object</code> key.</p>
 *
 * @author Mauro Talevi
 */
public interface ActionManager
{
    /**
     * Adds an ActionListener which handles a given Action
     *
     * @param actionId the Id of the Action
     * @param handler the ActionListener which handles the Action
     */
    void addHandler( Object actionId, ActionListener handler );

    /**
     * Enables or disables an Action
     *
     * @param actionId the Id of the Action
     * @param enabled the boolean flag -
     *    <code>true</code> if Action is enabled
     */
    void enableAction( Object actionId, boolean enabled );

    /**
     * Returns the Action registered with the ActionManager
     *
     * @param actionId the Id of the Action
     */
    Action getAction( Object actionId )
        throws NoSuchElementException;

    /**
     * Returns all the Actions registered with the
     * ActionManager
     */
    Action[] getActions();

    /**
     * Fires ActionEvent wihch will be handled by
     * the registered handlers.
     *
     * @param actionId the Id of the Action being executed
     * @param actionEvent the ActionEvent being fired
     */
    void fireActionEvent( Object actionId, ActionEvent actionEvent );
}