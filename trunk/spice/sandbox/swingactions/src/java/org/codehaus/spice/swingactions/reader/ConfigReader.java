/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.swingactions.reader;

import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.codehaus.spice.swingactions.metadata.ActionGroupsMetaData;
import org.codehaus.spice.swingactions.metadata.ActionMetaData;
import org.codehaus.spice.swingactions.metadata.ActionSetMetaData;
import org.codehaus.spice.swingactions.metadata.GroupActionMetaData;
import org.codehaus.spice.swingactions.metadata.SeparatorActionMetaData;

/** 
 * ConfigReader is a helper class used by the digester to store 
 * the configuration objects as they are created when parsing the XML file.
 *
 * @author Mauro Talevi
 */
public class ConfigReader {
    
    private Map m_actions; 
    private Map m_groups; 
    private String m_currentGroupId;    

    /**
     * Creates a ConfigReader
     */
    public ConfigReader() {
        m_actions = new HashMap();
        m_groups = new HashMap();
    }
    
    /**
     * Adds an Action metadata
     * @param metadata
     */
    public void addActionMetaData( final ActionMetaData metadata )
    {
        m_actions.put( metadata.getValue( ActionMetaData.ID), metadata );
    }

    /**
     * Adds a GroupAction metadata
     * @param metadata
     */
    public void addGroupActionMetaData( final GroupActionMetaData metadata )
    {
        final String groupId = metadata.getValue( GroupActionMetaData.GROUP_ID );
        Set groupActions = (Set)m_groups.get( groupId );
        if ( groupActions == null )
        {
            groupActions = new HashSet();
            m_groups.put( groupId, groupActions );
        }
        groupActions.add( metadata );
    }

    /**
     * Adds a SeparatorAction metadata
     * @param metadata
     */
    public void addSeparatorActionMetaData( final SeparatorActionMetaData metadata )
    {
        final String groupId = metadata.getValue( SeparatorActionMetaData.GROUP_ID );
        Set groupActions = (Set)m_groups.get( groupId );
        if ( groupActions == null )
        {
            groupActions = new HashSet();
            m_groups.put( groupId, groupActions );
        }
        groupActions.add( metadata );
    }

    /**
     * Returns the set of actions parsed
     * @return the ActionSetMetaData
     */
    public ActionSetMetaData getActionSet()
    {
        final ActionMetaData[] actions = 
            (ActionMetaData[])m_actions.values().toArray(new ActionMetaData[ m_actions.size() ] );
        return new ActionSetMetaData( actions );
    }

    /**
     * Returns the groups of actions parsed
     * @return the ActionGroupsMetaData
     */
    public ActionGroupsMetaData getActionGroups()
    {
        final String[] groupIds = getActionGroupIds();
        final ActionSetMetaData[] actionSets = new ActionSetMetaData[ groupIds.length ];
        for ( int i = 0; i < groupIds.length; i++ )
        {
            actionSets[ i ] = getActionGroup( groupIds[ i ] );
        }
        return new ActionGroupsMetaData( groupIds, actionSets );
    }

    /**
     * Returns the action group Ids 
     * @return the array of ids of groups
     */
    private String[] getActionGroupIds()
    {
        final Set groupKeys  = m_groups.keySet();
        return (String[])groupKeys.toArray(new String[ groupKeys.size() ] );
    }
    
    /**
     * Returns the set of actions for a given group
     * @param groupId the Id of the group
     * @return the ActionSetMetaData
     */
    private ActionSetMetaData getActionGroup( final String groupId )
    {
        final Set groupActions = (Set)m_groups.get( groupId );       
        final ActionMetaData[] actions = 
            (ActionMetaData[])groupActions.toArray(new ActionMetaData[ groupActions.size() ] );
        return new ActionSetMetaData( actions );
    }


    /**
     * Returns the current group Id
     * @return the Id of the group being parsed
     */
    public String getCurrentGroupId()
    {
        return m_currentGroupId;       
    }
    
    /**
     * Sets the current group Id
     * @param groupId the Id of the group being parsed
     */
    public void setCurrentGroupId( final String groupId )
    {
        m_currentGroupId = groupId;        
    }

    /**
     * Parses configuration resource adding content to this ConfigReader
     * @param resource the InputStream configuration
     * @throws Exception if parse fails
     */
    public void parse( final InputStream resource) throws Exception {     
        throw new Exception("Not yet implemented");
    }



    
}
