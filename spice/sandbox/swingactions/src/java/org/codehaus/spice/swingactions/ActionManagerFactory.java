/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.swingactions;

import java.util.Map;

/**
 * <p>ActionManagerFactory is a factory interface for ActionManager instances.
 * The factory also acts a configurator.
 * The ActionManager is configured via a map of parameters passed in the
 * create method.  ActionManagerFactory defines the keys used to retrieve
 * the elements of the map.</p>
 *
 * @author Mauro Talevi
 */
public interface ActionManagerFactory
{
    /**
     * The URL_LOCATION key.  Used to define the URL where the configuration
     * for ActionManager can be found.
     */
    String URL_LOCATION = "org.codehaus.spice.swingactions.configuration.url";

    /**
     * The FILE_LOCATION key.  Used to define the FILE where the configuration
     * for ActionManager can be found.
     */
    String FILE_LOCATION = "org.codehaus.spice.swingactions.configuration.file";

    /**
     * Creates a ActionManager from a given set of configuration parameters.
     *
     * @param config the Map of parameters for the configuration of the store
     * @return the ActionManager
     * @throws Exception if unable to create the ActionManager
     */
    ActionManager createActionManager( Map config )
        throws Exception;
}
