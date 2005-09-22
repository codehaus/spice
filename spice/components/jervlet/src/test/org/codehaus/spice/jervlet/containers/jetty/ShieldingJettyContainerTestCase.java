/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.jervlet.containers.jetty;

import junit.framework.TestCase;

import java.util.Properties;

/**
 * TestCase for ShieldingJettyContainer
 *
 * @author Johan Sjoberg
 */
public class ShieldingJettyContainerTestCase extends TestCase
{
    private static final String DEFAULT_CONFIGURATION = "../../testdata/jetty/jetty.xml";

    /**
     * Test a dafault plain Jetty, no conf but with the
     * shielding turned on.
     *
     * @throws Exception on all errors
     */
    public void testCreationNoConf() throws Exception
    {
        ShieldingJettyContainer container = new ShieldingJettyContainer();
        container.initialize();
        container.start();
        container.stop();
    }

    /**
     * Test jetty with default configuration, shielded,
     * but no parameters.
     *
     * @throws Exception on all errors
     */
    public void testCreationDefaultConfiguration() throws Exception
    {
        ShieldingJettyContainer container = new ShieldingJettyContainer();
        container.setJettyConfiguration( DEFAULT_CONFIGURATION );
        container.initialize();
        container.start();
        container.stop();
    }

    /**
     * Test jetty with default configuration, shielded and
     * with one (overriding) parameter.
     *
     * @throws Exception on all errors
     */
    public void testCreationDefaultConfigurationOneParameter() throws Exception
    {
        ShieldingJettyContainer container = new ShieldingJettyContainer();
        container.setJettyConfiguration( DEFAULT_CONFIGURATION );
        Properties properties = new Properties();
        properties.setProperty( "jetty.port", "10081" );
        container.addJettyProperties( properties );
        container.initialize();
        container.start();
        container.stop();
    }
}