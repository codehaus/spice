/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.tools.tasks;

/**
 * Task to add one InterceptorSet to another.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-11-20 10:00:38 $
 */
public class AddToInterceptorSetTask
    extends AddToPluginSetTask
{
    /**
     * Create task.
     */
    public AddToInterceptorSetTask()
    {
        super( InterceptorSet.class );
    }
}
