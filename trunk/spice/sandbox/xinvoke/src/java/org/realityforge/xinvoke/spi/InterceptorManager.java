/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.xinvoke.spi;

import java.util.Map;
import org.realityforge.xinvoke.Interceptor;

/**
 * The InterceptorManager is responsible for managing the Interceptors
 * for each Proxy. The Manager may create a new {@link Interceptor} graph
 * for each Proxy or may share {@link Interceptor}s between multiple Proxys.
 * The exact Policy is an implementation detail which will be documented with
 * the implementation.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-04-16 10:47:01 $
 */
public interface InterceptorManager
{
    /** The role key for service */
    String ROLE = InterceptorManager.class.getName();

    /**
     * Return an {@link Interceptor} instance for specified parameters.
     * A new {@link Interceptor} may be created for each call or the same
     * {@link Interceptor} objects could be returned depending on the policy
     * of the InterceptorManager implementation.
     *
     * @param parameters the map of parameters that used to create Interceptor
     * @return the created Interceptor object
     * @throws Exception if unable to create the Interceptor, or missing data
     *         required to create Interceptor.
     */
    Interceptor getInterceptor( Map parameters )
        throws Exception;

    /**
     * Release the specified interceptor.
     * The specified object MUST be a {@link Interceptor} created by the current
     * InterceptorManager else an exception is thrown. The {@link Interceptor}
     * MUST be passed to releaseInterceptor when it is no longer required and
     * MUST NOT be passed to releaseInterceptor more than once. Any invocations
     * on the interceptor after this method will result in undefined behaviour.
     *
     * @param interceptor the interceptor to destroy
     */
    void releaseInterceptor( Interceptor interceptor )
        throws Exception;
}
