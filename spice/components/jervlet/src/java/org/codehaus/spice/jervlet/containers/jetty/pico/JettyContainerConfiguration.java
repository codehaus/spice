/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.jervlet.containers.jetty.pico;

import java.net.URL;
import java.util.Properties;

/**
 * Configuration for a JettyContainer.
 *
 * @author Johan Sjoberg
 */
public interface JettyContainerConfiguration
{
    /**
     * Flag indication whether the Jetty instance should be shielded
     * or not. If not, no Jetty properties can be used.
     *
     * @return true if Jetty should be shielded, else false
     */
    boolean shieldJetty();

    /**
     * Jetty properties can be used to set any so called system
     * parameter Jetty understands. Note, if shielding of Jetty
     * is not turned on any possible parameters here will not
     * be used.
     *
     * @return A properties object with system parameters for Jetty,
     *         or null
     */
    Properties getProperties();

    /**
     * Possible configuration for Jetty. There are three ways
     * to configure Jetty, with a String holding the path to
     * Jetty's XML configuration file, a String that IS the
     * XML configuration itself or with an URL also pointing
     * at the configuration file.
     *
     * @return Information about Jetty's configuration file or null
     */
    Object getConfiguration();
}