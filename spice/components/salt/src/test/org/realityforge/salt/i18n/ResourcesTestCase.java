/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.salt.i18n;

import junit.framework.TestCase;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-06-13 00:01:56 $
 */
public class ResourcesTestCase
    extends TestCase
{
    public ResourcesTestCase( final String name )
    {
        super( name );
    }

    protected void setUp() throws Exception
    {
        MockResourceBundle.cleanResourceSet();
    }
}
