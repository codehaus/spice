/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.swingactions.metadata;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

/**
 * TestCase for MetaData
 * 
 * @author Mauro Talevi
 */
public class MetaDataTestCase
    extends TestCase
{
    public MetaDataTestCase( final String name )
    {
        super( name );
    }

    public void testNullDataInAction()
        throws Exception
    {
        try
        {
            new ActionMetaData( null );
            fail( "Expected to fail due to null data" );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "NPE message",
                          "data",
                          npe.getMessage() );
        }
    }

    public void testNullActionId()
        throws Exception
    {
        try
        {
            final Map data = new HashMap();
            new ActionMetaData( data );
            fail( "Expected to fail due to null Action Id" );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "NPE message",
                          "Id",
                          npe.getMessage() );
        }
    }

    public void testNullActionName()
        throws Exception
    {
        try
        {
            final Map data = new HashMap();
            data.put( ActionMetaData.ID, "id" );
            new ActionMetaData( data );
            fail( "Expected to fail due to null Action Name" );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "NPE message",
                          "Name",
                          npe.getMessage() );
        }
    }

    public void testMandatoryActionKeys()
        throws Exception
    {
        final Map data = new HashMap();
        data.put( ActionMetaData.ID, "id" );
        data.put( ActionMetaData.NAME, "name" );
        ActionMetaData action = new ActionMetaData( data );
        assertEquals( "ActionMetaData", "id", action.getValue( ActionMetaData.ID) );
        assertEquals( "ActionMetaData", "name", action.getValue( ActionMetaData.NAME) );
    }
}