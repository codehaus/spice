/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.swingactions;

import java.awt.event.ActionEvent;
import java.util.NoSuchElementException;

import javax.swing.Action;

/**
 *  <p>ActionManager is responsible for managing a set of Actions.
 *  Each <code>Action</code> is uniquely identified by an <code>actionId</code> 
 *  which will tipically be <code>String</code> or a <code>Class</code>, 
 *  but may be any <code>Object</code> key.</p>
 * 
 * @author <a href="mailto:mauro.talevi at aquilonia.org">Mauro Talevi</a>
 */
public interface ActionManager
{
    /** 
     *  Enables or disables an Action 
     *  @param actionId the Id of the Action 
     *  @param enabled the boolean flag - <code>true</code> if Action is enabled
     */
    public void enableAction(
        final Object actionId,
        final boolean enabled);
        
    /** 
     *  Returns the Action registered with the ActionManager 
     *  @param actionId the Id of the Action 
     */
    public Action getAction(final Object actionId)
        throws NoSuchElementException;
        
    /** 
     *  Returns all the Actions registered with the ActionManager
     */
    public Action[] getActions();
    
    /** 
     *  Fires ActionEvent wihch will be handled by the registered handlers.
     *  @param actionId the Id of the Action being executed
     *  @param actionEvent the ActionEvent being fired
     */
    public void fireActionEvent(
        final Object actionId,
        final ActionEvent actionEvent);
}