/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.xinvoke;

/**
 * The Interceptor is the component through which invocations
 * pass through. In most cases the invocations pass through a
 * series of Interceptor objects before the target object is
 * invoked.
 *
 * <p>Interceptors should not store any data relevent to a
 * particular invocation and potentially should not store
 * any data relevent to a particular target object (It depends
 * on the policy via which Interceptors are created via the
 * {@link org.realityforge.xinvoke.spi.ProxyManager}).</p>
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.2 $ $Date: 2003-09-02 04:17:27 $
 */
public interface Interceptor
{
    /**
     * Process a particular invocation.
     * The method must NEVER throw an exception and any exceptions should
     * be caught and placed into the invocation via {@link Invocation#setThrowable}.
     *
     * <p>Note: most Interceptors should pass control to the next Interceptor
     * in the series.</p>
     *
     * @param invocation the invocation to process
     */
    void invoke( Invocation invocation );
}
