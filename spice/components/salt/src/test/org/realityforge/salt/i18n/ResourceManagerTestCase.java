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
 * @version $Revision: 1.4 $ $Date: 2003-06-13 01:34:27 $
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
            ResourceManager.getClassResources( ResourceManagerTestCase.class );
        assertNotNull( "resources", resources );
        assertEquals( "package value", false, resources.getBoolean( "package" ) );
        assertEquals( "class value", true, resources.getBoolean( "class" ) );
    }

    public void testGetResources()
        throws Exception
    {
        final Resources resources =
            ResourceManager.getResources( ResourceManagerTestCase.class.getName(),
                                          ResourceManagerTestCase.class.getClassLoader() );
        assertNotNull( "resources", resources );
        assertEquals( "package value", false, resources.getBoolean( "package" ) );
        assertEquals( "class value", true, resources.getBoolean( "class" ) );
    }

    public void testAccessCachedResources()
        throws Exception
    {
        final Resources resources1 =
            ResourceManager.getClassResources( ResourceManagerTestCase.class );
        assertNotNull( "resources1", resources1 );

        final Resources resources2 =
            ResourceManager.getClassResources( ResourceManagerTestCase.class );
        assertNotNull( "resources2", resources2 );

        assertEquals( "resources1 == resources2", resources1, resources2 );
    }
}
