/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.xinvoke.spi;

import java.util.Map;

/**
 * The ProxyManager will manage the creation of proxy objects
 * in a particular scope. The ProxyManager should just manage
 * the creation and destruction of proxies.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-04-16 10:47:01 $
 */
public interface ProxyManager
{
    /** The role key for service */
    String ROLE = ProxyManager.class.getName();

    String TARGET = "target";
    String CLASSLOADER = "classloader";
    String INTERFACES = "interfaces";

    /**
     * Create a Proxy instance.
     * The proxy is created from the specified parameter map. The parameter
     * map contains all the information required to build the proxy. Most
     * implementations of the ProxyManager will support the {@link #TARGET}
     * and {@link #INTERFACES} keys. However different implementation may
     * specify other potential sets of data that can be used to create the
     * Proxy (such as the uri of the Web Service that is adapted).
     *
     * @param parameters the map of parameters defining how to create the proxy
     * @return the created proxy object
     * @throws Exception if unable to create the proxy, or missing data
     *         required to create proxy.
     */
    Object createProxy( Map parameters )
        throws Exception;

    /**
     * Return true if specified object is a proxy created by
     * this ProxyManager.
     *
     * @param object the object to test
     * @return true if specified object is a proxy created by this ProxyManager.
     */
    boolean isManagedProxy( Object object );

    /**
     * Destroy the specified proxy.
     * The specified object MUST be a proxy created by the current
     * ProxyManager else an exception is thrown. The proxy should
     * not have been previously passed to destroyProxy method.
     * Any invocations on the proxy after this method will
     * result in an IllegalStateException.
     *
     * @param object the proxy to destroy
     */
    void destroyProxy( Object object )
        throws Exception;
}
