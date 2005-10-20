/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.jervlet.containers.jetty.pico;

import java.util.Properties;
import java.net.URL;

/**
 * Default Jetty container configuration
 *
 * @author Johan Sjoberg
 */
public class DefaultJettyContainerConfiguration
    implements JettyContainerConfiguration
{
    /** Shield Jetty or not? */
    private boolean m_shieldJetty = false;

    /** Jetty's system properties */
    private Properties m_jettyProperties = null;

    /** Jetty's configuration */
    private Object m_jettyConfiguration = null;

    /**
     * Flag indication whether the Jetty instance should be shielded
     * or not. If not, no Jetty properties can be used.
     *
     * @see org.codehaus.spice.jervlet.containers.jetty.ShieldingJettyContainer for more
     *      information about how shielding is done
     * @return true if Jetty should be shielded, else false
     */
    public boolean shieldJetty()
    {
        return m_shieldJetty;
    }

    /**
     * Jetty properties can be used to set any so called system
     * parameter Jetty understands. Note, if shielding of Jetty
     * is not turned on, any possible parameters here will not
     * be used.
     *
     * @see org.codehaus.spice.jervlet.containers.jetty.ShieldingJettyContainer
     *      for more information about how parameters are handled.
     * @return A properties object with system parameters for Jetty, or null
     */
    public Properties getProperties()
    {
        return m_jettyProperties;
    }

    /**
     * Possible configuration for Jetty. There are three ways
     * to configure Jetty, with a String holding the path to
     * Jetty's XML configuration file, a String that IS the
     * XML configuration itself or with a URL also pointing
     * at the configuration file.
     *
     * @return Information about Jetty's configuration file or null
     */
    public Object getConfiguration()
    {
        return m_jettyConfiguration;
    }

    /**
     * Set Jetty's properties
     *
     * @param properties the properties to add to Jetty
     * @see org.codehaus.spice.jervlet.containers.jetty.ShieldingJettyContainer for
     *      more information about how parameters are handled and some examples.
     */
    public void setProperties( final Properties properties )
    {
        m_shieldJetty = null != properties;
        m_jettyProperties = properties;
    }

    /**
     * Set Jetty's configuration. There are three ways
     * to configure Jetty, with a String holding the path to
     * Jetty's XML configuration file, a String that IS the
     * XML configuration itself or with a URL also pointing
     * at the configuration file. Note, this configuration
     * is in Jetty's own format defined by the Jetty project.
     * Different versions of Jetty might need different types
     * of configuration files.
     *
     * @param configuration a String, a URL or null to reset
     */
    public void setConfiguration( final Object configuration )
    {
        if( null == configuration ||
            configuration instanceof String ||
            configuration instanceof URL )
        {
            m_jettyConfiguration = configuration;
        }
        else
        {
            throw new IllegalArgumentException( "Only String, URL or null are acceptable.");
        }
    }
}