/*
 * Copyright  The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 */
package org.codehaus.spice.configkit;

import junit.framework.TestCase;

/**
 * Basic unit tests for the info objects.
 *
 * @author Peter Donald
 */
public final class EntityInfoTestCase
    extends TestCase
{
    public void testFullySpecified()
    {
        doInfoTest( TestData.PUBLIC_ID,
                    TestData.SYSTEM_ID,
                    TestData.RESOURCE );
    }

    public void testNullSystemId()
    {
        doInfoTest( TestData.PUBLIC_ID, null, TestData.RESOURCE );
    }

    public void testNullPublicId()
    {
        doInfoTest( null, TestData.SYSTEM_ID, TestData.RESOURCE );
    }

    public void testNullResource()
    {
        try
        {
            doInfoTest( TestData.PUBLIC_ID, TestData.SYSTEM_ID, null );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( npe.getMessage(), "resource" );
            return;
        }
        fail( "Expected Null pointer due to null resource" );
    }

    public void testNullIDs()
    {
        try
        {
            doInfoTest( null, null, TestData.RESOURCE );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( npe.getMessage(), "systemId" );
            return;
        }
        fail( "Expected Null pointer due to null publicId/systemId" );
    }

    private void doInfoTest( final String publicId,
                             final String systemId,
                             final String resource )
    {
        final EntityInfo info = new EntityInfo( publicId, systemId, resource );
        assertEquals( "PUBLIC_ID", publicId, info.getPublicId() );
        assertEquals( "SYSTEM_ID", systemId, info.getSystemId() );
        assertEquals( "RESOURCE", resource, info.getResource() );
    }
}

