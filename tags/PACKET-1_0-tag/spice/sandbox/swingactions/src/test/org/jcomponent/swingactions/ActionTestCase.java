/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.swingactions;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import org.jcomponent.swingactions.metadata.ActionMetaData;


/**
 * ActionTestCase
 *
 * @author <a href="mailto:mauro.talevi at aquilonia.org">Mauro Talevi</a>
 */
public class ActionTestCase
    extends AbstractTestCase
{

    private ActionManager m_manager;
    public ActionTestCase( final String name )
    {
        super( name );
    }

    protected void setUp() throws Exception
    {
        m_manager = new SimpleActionManager();
    }

    public void testNullMetaData()
        throws Exception
    {
        try
        {
            new ActionAdapter( null, m_manager );
            fail( "Expected to fail due to null metadata" );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "NPE message",
                          "metadata",
                          npe.getMessage() );
        }
    }
    
    public void testNullManager()
        throws Exception
    {
        final Map data = new HashMap();
        data.put( ActionMetaData.ID, "id" );
        data.put( ActionMetaData.NAME, "name" );
        
        final ActionMetaData metadata = new ActionMetaData( data );
        try
        {
            new ActionAdapter( metadata, null );
            fail( "Expected to fail due to null manager" );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "NPE message",
                          "manager",
                          npe.getMessage() );
        }
    }
    
    public void testGetMetaData(){
        final Map data = new HashMap();
        data.put( ActionMetaData.ID, "id" );
        data.put( ActionMetaData.NAME, "name" );
        
        final ActionMetaData metadata = new ActionMetaData( data );
        final ActionAdapter action = new ActionAdapter( metadata, m_manager );
        assertEquals( "Action metadata ", action.getMetaData().toString(), metadata.toString() );
    }
    
    public void testMinimalAction(){
        final Map data = new HashMap();
        data.put( ActionMetaData.ID, "id" );
        data.put( ActionMetaData.NAME, "name" );
        
        final ActionMetaData metadata = new ActionMetaData( data );
        final ActionAdapter action = new ActionAdapter( metadata, m_manager );
        assertEquals( "Action id ", (String)action.getValue( ActionMetaData.ID ), "id" );        
        assertEquals( "Action name ", (String)action.getValue( ActionMetaData.NAME ), "name" );        
    }
    
    public void testActionWithNullEntries(){
        final Map data = new HashMap();
        data.put( ActionMetaData.ID, "id" );
        data.put( ActionMetaData.NAME, "name" );
        data.put( ActionMetaData.SHORT_DESCRIPTION, null );
        data.put( ActionMetaData.LONG_DESCRIPTION, null );
        data.put( ActionMetaData.SMALL_ICON, null );
        data.put( ActionMetaData.LARGE_ICON, null );
        data.put( ActionMetaData.ACCELERATOR_KEY, null );
        data.put( ActionMetaData.MNEMONIC_KEY, null );
        
        final ActionMetaData metadata = new ActionMetaData( data );
        final ActionAdapter action = new ActionAdapter( metadata, m_manager );
        assertNull( "Action shortDescription ", action.getValue( ActionMetaData.SHORT_DESCRIPTION ) );
        assertNull( "Action longDescription ", action.getValue( ActionMetaData.LONG_DESCRIPTION ) );
        assertNull( "Action smallIcon ", action.getValue( ActionMetaData.SMALL_ICON ) );
        assertNull( "Action largeIcon ", action.getValue( ActionMetaData.LARGE_ICON ) );
        assertNull( "Action accelerator ", action.getValue( ActionMetaData.ACCELERATOR_KEY ) );
        assertNull( "Action mnemonic ", action.getValue( ActionMetaData.MNEMONIC_KEY ) );
    }

    public void testActionWithDescription(){
        final Map data = new HashMap();
        data.put( ActionMetaData.ID, "id" );
        data.put( ActionMetaData.NAME, "name" );
        data.put( ActionMetaData.SHORT_DESCRIPTION, "short description" );
        data.put( ActionMetaData.LONG_DESCRIPTION, "long description" );
                
        final ActionMetaData metadata = new ActionMetaData( data );
        final ActionAdapter action = new ActionAdapter( metadata, m_manager );
        assertEquals( "Action shortDescription ", (String)action.getValue( ActionMetaData.SHORT_DESCRIPTION ), 
                                                  "short description" );
        assertEquals( "Action longDescription ", (String)action.getValue( ActionMetaData.LONG_DESCRIPTION ), 
                                                  "long description" );
    }

    public void testActionWithIcon(){
        final Map data = new HashMap();
        data.put( ActionMetaData.ID, "id" );
        data.put( ActionMetaData.NAME, "name" );
        data.put( ActionMetaData.SMALL_ICON, "/org/jcomponent/swingactions/Bean16.gif" );
        data.put( ActionMetaData.LARGE_ICON, "/org/jcomponent/swingactions/Bean24.gif" );
                
        final ActionMetaData metadata = new ActionMetaData( data );
        final ActionAdapter action = new ActionAdapter( metadata, m_manager );
        final Icon smallIcon = (Icon)action.getValue( ActionMetaData.SMALL_ICON );
        assertEquals( "Action smallIcon ", smallIcon.toString(), 
                                           getIcon( "/org/jcomponent/swingactions/Bean16.gif" ).toString() );        
        final Icon largeIcon = (Icon)action.getValue( ActionMetaData.LARGE_ICON );
        assertEquals( "Action largeIcon ", largeIcon.toString(), 
                                           getIcon( "/org/jcomponent/swingactions/Bean24.gif" ).toString() );        
    }

    public void testActionWithInvalidIcon(){
        final Map data = new HashMap();
        data.put( ActionMetaData.ID, "id" );
        data.put( ActionMetaData.NAME, "name" );
        data.put( ActionMetaData.SMALL_ICON, "/path/to/inexsistent/icon16.gif" );
        data.put( ActionMetaData.LARGE_ICON, "/path/to/inexsistent/icon24.gif" );
                
        final ActionMetaData metadata = new ActionMetaData( data );
        final ActionAdapter action = new ActionAdapter( metadata, m_manager );
        assertNull( "Action smallIcon ", action.getValue( ActionMetaData.SMALL_ICON ) );
        assertNull( "Action largeIcon ", action.getValue( ActionMetaData.LARGE_ICON ) );
    }

    public void testActionWithAccelerator(){
        final Map data = new HashMap();
        data.put( ActionMetaData.ID, "id" );
        data.put( ActionMetaData.NAME, "name" );
        data.put( ActionMetaData.ACCELERATOR_KEY, "control A" );
                
        final ActionMetaData metadata = new ActionMetaData( data );
        final ActionAdapter action = new ActionAdapter( metadata, m_manager );
        final KeyStroke keyStroke = (KeyStroke)action.getValue( ActionMetaData.ACCELERATOR_KEY );
        assertEquals( "Action accelerator ", keyStroke.toString(), 
                                             KeyStroke.getKeyStroke( "control A" ).toString() );
    }

    public void testActionWithInvalidAccelerator(){
        final Map data = new HashMap();
        data.put( ActionMetaData.ID, "id" );
        data.put( ActionMetaData.NAME, "name" );
        data.put( ActionMetaData.ACCELERATOR_KEY, "wrong A" );
                
        final ActionMetaData metadata = new ActionMetaData( data );
        final ActionAdapter action = new ActionAdapter( metadata, m_manager );
        assertNull( "Action accelerator ", action.getValue( ActionMetaData.ACCELERATOR_KEY ) );
    }

    public void testActionWithMnemonic(){
        final Map data = new HashMap();
        data.put( ActionMetaData.ID, "id" );
        data.put( ActionMetaData.NAME, "name" );
        data.put( ActionMetaData.MNEMONIC_KEY, "A" );
                
        final ActionMetaData metadata = new ActionMetaData( data );
        final ActionAdapter action = new ActionAdapter( metadata, m_manager );
        final Character mnemonic = (Character)action.getValue( ActionMetaData.MNEMONIC_KEY );
        assertEquals( "Action mnemonic ", mnemonic.toString(), 
                                          new Character( 'A' ).toString() );
    }

    public void testActionWithInvalidMnemonic(){
        final Map data = new HashMap();
        data.put( ActionMetaData.ID, "id" );
        data.put( ActionMetaData.NAME, "name" );
        data.put( ActionMetaData.MNEMONIC_KEY, "" );
                
        final ActionMetaData metadata = new ActionMetaData( data );
        final ActionAdapter action = new ActionAdapter( metadata, m_manager );
        final Character mnemonic = (Character)action.getValue( ActionMetaData.MNEMONIC_KEY );
        assertNull( "Action mnemonic ", action.getValue( ActionMetaData.MNEMONIC_KEY ) );
    }

    public void testActionPerformed(){
        final Map data = new HashMap();
        data.put( ActionMetaData.ID, "id" );
        data.put( ActionMetaData.NAME, "name" );
                
        final ActionMetaData metadata = new ActionMetaData( data );
        final ActionAdapter action = new ActionAdapter( metadata, m_manager );
        action.actionPerformed( new ActionEvent( this, ActionEvent.ACTION_PERFORMED, "test action") );
        assertTrue( "actionPerformed", true );
    }

    private Icon getIcon(final String path)
    {
        final URL resource = getClass().getResource( path );
        if (resource != null)
        {
            return new ImageIcon(resource);
        }
        return null;
    }
}
