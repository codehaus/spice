/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.alchemist.configuration;

import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;


/**
 * @author Mauro Talevi
 */
public class MockAvalonConfigurable implements Configurable {

    /**
     * @see Configurable#configure(Configuration)
     */
    public void configure( Configuration configuration ) {
        // no-op
    }
}
