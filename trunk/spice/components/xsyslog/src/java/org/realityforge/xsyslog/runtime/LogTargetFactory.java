/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.xsyslog.runtime;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.log.LogTarget;

/**
 * The factory for creating LogTarget objects.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-04-16 11:00:18 $
 */
public interface LogTargetFactory
{
    /**
     * Create a LogTarget for specified configuration.
     *
     * @param configuration the configuration
     * @return the newly create LogTarget
     * @throws org.apache.avalon.framework.configuration.ConfigurationException if unable to create Target
     */
    LogTarget createTarget( Configuration configuration )
        throws ConfigurationException;
}
