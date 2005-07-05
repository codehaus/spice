/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.jervlet.impl;

import org.codehaus.spice.jervlet.Instantiator;

/**
 * Standard class instantiator. This instantiator just creates a
 * new instance of the Servlet or Filter class. No lifecycle
 * interfaces will be called, so using this instantiator is the
 * same as deploying servlets in a standatd servlet container.
 *
 * @author Johan Sjoberg
 */
public class StandardServletInstantiator implements Instantiator
{
    /**
     * Create a new instance of a Servlet or Filter class.
     *
     * @param clazz The calss to instantiate
     * @return A new instance of the given class
     * @throws InstantiationException If the class couldn't ne instantiated
     * @throws IllegalAccessException If an access problem occured
     */
    public Object instantiate( Class clazz )
        throws InstantiationException, IllegalAccessException
    {
        return clazz.newInstance();
    }
}
