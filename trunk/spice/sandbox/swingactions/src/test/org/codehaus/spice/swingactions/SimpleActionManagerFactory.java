/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.swingactions;

import java.util.Map;

import org.codehaus.spice.swingactions.AbstractActionManagerFactory;
import org.codehaus.spice.swingactions.ActionManager;

/**
 * SimpleActionManagerFactory is an implementation of ActionManagerFactory
 * without using any configuration resource.
 *
 * @author Mauro Talevi
 */
public class SimpleActionManagerFactory
    extends AbstractActionManagerFactory
{
    /**
     * Creates a ActionManager from a given set of configuration parameters.
     *
     * @param config the Map of parameters for the configuration of the store
     * @return the ActionManager
     * @throws Exception if unable to create the ActionManager
     */
    protected ActionManager doCreateActionManager( final Map config )
        throws Exception
    {
        return new SimpleActionManager();
    }
}
