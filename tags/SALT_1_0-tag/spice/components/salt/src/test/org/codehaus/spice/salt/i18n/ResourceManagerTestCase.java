/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.salt.i18n;

import junit.framework.TestCase;

/**
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2003-12-02 02:15:05 $
 */
public class ResourceManagerTestCase
    extends TestCase
{
    public ResourceManagerTestCase( final String name )
    {
        super( name );
    }

    protected void setUp()
        throws Exception
    {
        ResourceManager.clearResourceCache();
    }

    public void testGetPackageResources()
        throws Exception
    {
        final Resources resources =
            ResourceManager.getPackageResources( ResourceManagerTestCase.class );
        assertNotNull( "resources", resources );
        assertEquals( "package value",
                      true,
                      resources.getBoolean( "package" ) );
        assertEquals( "class value", false, resources.getBoolean( "class" ) );
    }

    public void testGetPackageResourcesFromDefaultPackage()
        throws Exception
    {
        final Class clazz = Class.forName( "DummyClass" );
        final Resources resources =
            ResourceManager.getPackageResources( clazz );
        assertNotNull( "resources", resources );
        assertEquals( "default value",
                      true,
                      resources.getBoolean( "default" ) );
        assertEquals( "package value",
                      true,
                      resources.getBoolean( "package" ) );
        assertEquals( "class value", false, resources.getBoolean( "class" ) );
    }

    public void testGetUncachedResourceFromCachedClassLoader()
        throws Exception
    {
        final Resources resources1 =
            ResourceManager.getPackageResources( ResourceManagerTestCase.class );
        assertNotNull( "resources1", resources1 );

        final Resources resources2 =
            ResourceManager.getClassResources( ResourceManagerTestCase.class );
        assertNotNull( "resources2", resources2 );

        assertEquals( "package value",
                      true,
                      resources1.getBoolean( "package" ) );
        assertEquals( "class value", false, resources1.getBoolean( "class" ) );

        assertEquals( "package value",
                      false,
                      resources2.getBoolean( "package" ) );
        assertEquals( "class value", true, resources2.getBoolean( "class" ) );
    }

    public void testGetClassResources()
        throws Exception
    {
        final Resources resources =
            ResourceManager.getClassResources( ResourceManagerTestCase.class );
        assertNotNull( "resources", resources );
        assertEquals( "package value",
                      false,
                      resources.getBoolean( "package" ) );
        assertEquals( "class value", true, resources.getBoolean( "class" ) );
    }

    public void testGetResources()
        throws Exception
    {
        final Resources resources =
            ResourceManager.getResources(
                ResourceManagerTestCase.class.getName(),
                ResourceManagerTestCase.class.getClassLoader() );
        assertNotNull( "resources", resources );
        assertEquals( "package value",
                      false,
                      resources.getBoolean( "package" ) );
        assertEquals( "class value", true, resources.getBoolean( "class" ) );
    }

    public void testGetBaseResources()
        throws Exception
    {
        final Resources resources =
            ResourceManager.getBaseResources(
                MockResourceBundle.class.getName(),
                ResourceManagerTestCase.class.getClassLoader() );
        assertNotNull( "resources", resources );
        MockResourceBundle.cleanResourceSet();
        MockResourceBundle.addResource( "test", "test" );
        assertEquals( "test value", "test", resources.getString( "test" ) );
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
