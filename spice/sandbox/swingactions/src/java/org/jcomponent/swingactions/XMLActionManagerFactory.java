/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.swingactions;

import java.io.InputStream;
import java.util.Map;

/**
 * XMLActionManagerFactory is an implementation of ActionManagerFactory
 * using an XML configuration resource.
 *
 * @author Mauro Talevi
 */
public class XMLActionManagerFactory
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
        final InputStream resource = getInputStream( config );
        if( null != resource )
        {
            return new XMLActionManager( resource );
        }
        return missingConfiguration();
    }
}
