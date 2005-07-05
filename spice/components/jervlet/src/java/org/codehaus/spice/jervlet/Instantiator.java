/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.jervlet;

/**
 * An <code>Instantiator</code> is responsible for instantiating
 * <a href="http://java.sun.com/products/servlet/2.3/javadoc/javax/servlet/Servlet.html">Servlet</a>
 * and
 * <a href="http://java.sun.com/products/servlet/2.3/javadoc/javax/servlet/Filter.html">Filter</a>
 * clsses on behalf of a servlet container.
 * 
 * @author Paul Hammant
 * @author Johan Sjoberg
 * @author Peter Royal
 */
public interface Instantiator
{
    /** Role, used by some component frameworks */
    String ROLE = Instantiator.class.getName();

    /**
     * Instantiate a servlet or filter class.
     *
     * @param clazz The servlet of filter class to instantiate
     * @return The instatiated class
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    Object instantiate( Class clazz ) throws InstantiationException, IllegalAccessException;
}