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
 * @version $Revision: 1.1 $ $Date: 2003-06-13 01:31:06 $
 */
public class ResourceManagerTestCase
    extends TestCase
{
    public ResourceManagerTestCase( final String name )
    {
        super( name );
    }

    protected void setUp() throws Exception
    {
        ResourceManager.clearResourceCache();
    }

    public void testGetPackageResources()
        throws Exception
    {
        final Resources resources =
            ResourceManager.getPackageResources( ResourceManagerTestCase.class );
        assertNotNull( "resources", resources );
        assertEquals( "package value", true, resources.getBoolean( "package" ) );
        assertEquals( "class value", false, resources.getBoolean( "class" ) );
    }

    public void testGetClassResources()
        throws Exception
    {
        final Resources resources =
            ResourceManager.getPackageResources( ResourceManagerTestCase.class );
        assertNotNull( "resources", resources );
        assertEquals( "package value", false, resources.getBoolean( "package" ) );
        assertEquals( "class value", true, resources.getBoolean( "class" ) );
    }
}
