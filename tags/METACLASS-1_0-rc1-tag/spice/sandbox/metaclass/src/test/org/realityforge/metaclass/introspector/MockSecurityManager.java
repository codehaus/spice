/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.introspector;

import java.security.Permission;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-10-04 00:47:49 $
 */
class MockSecurityManager
    extends SecurityManager
{
    public void checkPermission( Permission perm )
    {
    }

    public void checkPermission( Permission perm, Object context )
    {
    }
}
