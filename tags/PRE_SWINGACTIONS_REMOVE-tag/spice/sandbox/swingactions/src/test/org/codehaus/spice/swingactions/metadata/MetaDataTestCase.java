/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.swingactions.metadata;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.spice.swingactions.metadata.ActionMetaData;
import org.codehaus.spice.swingactions.metadata.ActionSetMetaData;

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
        assertEquals( "ActionMetaData id", action.getValue( ActionMetaData.ID), "id" );
        assertEquals( "ActionMetaData name", action.getValue( ActionMetaData.NAME), "name" );
    }

    public void testNonMandatoryActionKeys()
        throws Exception
    {
        final Map data = new HashMap();
        data.put( ActionMetaData.ID, "id" );
        data.put( ActionMetaData.NAME, "name" );
        data.put( ActionMetaData.SHORT_DESCRIPTION, "shortDescription" );
        data.put( ActionMetaData.LONG_DESCRIPTION, null );
        ActionMetaData action = new ActionMetaData( data );
        assertEquals( "ActionMetaData shortDescription", action.getValue( ActionMetaData.SHORT_DESCRIPTION ), "shortDescription" );
        assertNull( "ActionMetaData longDescription", action.getValue( ActionMetaData.LONG_DESCRIPTION ) );
        assertNull( "ActionMetaData mnemonicKey", action.getValue( ActionMetaData.MNEMONIC_KEY ) );
    }
    
    public void testGetActionKeys()
        throws Exception
    {
        final Map data = new HashMap();
        data.put( ActionMetaData.ID, "id" );
        data.put( ActionMetaData.NAME, "name" );
        data.put( ActionMetaData.SHORT_DESCRIPTION, "shortDescription" );
        ActionMetaData action = new ActionMetaData( data );
        assertEquals( "ActionMetaData keys", action.getKeys().length, 3 );
    }

    public void testActionToString()
        throws Exception
    {
        final Map data = new HashMap();
        data.put( ActionMetaData.ID, "id" );
        data.put( ActionMetaData.NAME, "name" );
        data.put( ActionMetaData.SHORT_DESCRIPTION, "shortDescription" );
        ActionMetaData action = new ActionMetaData( data );
        assertEquals( "ActionMetaData.toString()", action.toString(), "[ActionMetaData id=id, name=name, shortDescription=shortDescription, longDescription=null, smallIcon=null, largeIcon=null, actionCommandKey=null, acceleratorKey=null, mnemonicKey=null]" );
        data.put( ActionMetaData.SMALL_ICON, "/path/to/icon" );
        data.put( ActionMetaData.MNEMONIC_KEY, "'M'" );
        action = new ActionMetaData( data );
        assertEquals( "ActionMetaData.toString()", action.toString(), "[ActionMetaData id=id, name=name, shortDescription=shortDescription, longDescription=null, smallIcon=/path/to/icon, largeIcon=null, actionCommandKey=null, acceleratorKey=null, mnemonicKey='M']" );
    }

    
    public void testNullActionSet()
        throws Exception
    {
        try
        {
            new ActionSetMetaData( null );
            fail( "Expected to fail due to null ActionSet" );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "NPE message",
                          "actions",
                          npe.getMessage() );
        }
    }

    public void testGetActionsFromSet()
        throws Exception
    {
        final ActionSetMetaData actionSet = new ActionSetMetaData( createTwoActions() );
        assertEquals( "Found 2 actions", actionSet.getActions().length, 2 );
        assertEquals( "Action ids", actionSet.toString(), "[ActionSetMetaData ids=[id-1,id-2]]" );
        final ActionMetaData action1 = actionSet.getAction( "id-1" );
        assertEquals( "Action 1 id", action1.getValue( ActionMetaData.ID ), "id-1" );
        final ActionMetaData action2 = actionSet.getAction( "id-2" );
        assertEquals( "Action 2 id", action2.getValue( ActionMetaData.ID ), "id-2" );
    }

    public void testActionNotFoundInSet()
        throws Exception
    {
        final ActionSetMetaData actionSet = new ActionSetMetaData( createTwoActions() );
        assertNull( "Action not found", actionSet.getAction( "another-id" ) );
        assertNull( "Action key null", actionSet.getAction( null ) );
    }

    public void testEmptyActionSet()
        throws Exception
    {
        final ActionSetMetaData actionSet = new ActionSetMetaData( new ActionMetaData[ 0 ] );
        assertNull( "Action not found", actionSet.getAction( "any-id" ) );
    }

    
    private ActionMetaData[] createTwoActions()
    {
        final Map data1 = new HashMap();
        data1.put( ActionMetaData.ID, "id-1" );
        data1.put( ActionMetaData.NAME, "name-1" );
        ActionMetaData action1 = new ActionMetaData( data1 );
        final Map data2 = new HashMap();
        data2.put( ActionMetaData.ID, "id-2" );
        data2.put( ActionMetaData.NAME, "name-2" );
        ActionMetaData action2 = new ActionMetaData( data2 );
        return new ActionMetaData[]{ action1, action2 };
    }

}