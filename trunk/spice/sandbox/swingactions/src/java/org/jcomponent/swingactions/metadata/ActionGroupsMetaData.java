/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.swingactions.metadata;

/**
 * Defines the metadata of groups of actions
 *
 * @author <a href="mailto:mauro.talevi at aquilonia.org">Mauro Talevi</a>
 */
public class ActionGroupsMetaData
{

    /** The action group ids */
    private final String[] m_groupIds;
    /** The action sets - one for each group */
    private final ActionSetMetaData[] m_actionSets;

    /**
     * Constructs an ActionGroupsMetaData object from the actionSets
     * @param groupIds the ids of the action groups
     * @param actionSets the action sets corresponding to the groupIds 
     */
    public ActionGroupsMetaData(final String[] groupIds, 
                                 final ActionSetMetaData[] actionSets )
    {
        if ( groupIds == null )
        {
            throw new NullPointerException( "groupIds" );
        }
        m_groupIds = groupIds;
        if ( actionSets == null )
        {
            throw new NullPointerException( "actionSets" );
        }
        m_actionSets = actionSets;
    }
    
    /**
     * Returns the group Ids
     * @return the array of Strings
     */
    public String[] getGroupIds()
    {
        return m_groupIds;
    }

    /**
     * Returns all the action sets 
     * @return the array of ActionSetMetaData
     */
    public ActionSetMetaData[] getActionSets()
    {
        return m_actionSets;
    }

    /**
     * Returns the set of action for a given group
     * @param groupId the Id of the group the actions belong to
     * @return the ActionMetaData 
     */
    public ActionSetMetaData getActionSet( final String groupId )
    {
        for ( int i = 0; i < m_groupIds.length; i++ )
        { 
            if ( m_groupIds[ i ].equals( groupId ) )
            {
                return m_actionSets[ i ];
            }
        }
        return null;
    }
    
    /**
     *  String representation
     */
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append( "[ActionGroupsMetaData groups=[" );
        for ( int i = 0; i < m_groupIds.length; i++ )
        { 
            sb.append( "id=" );
            sb.append( m_groupIds[ i ] );
            sb.append( ", actionSet=" );
            sb.append( m_actionSets[ i ].getActions() );
            if ( i < m_groupIds.length - 1 )
            {
                sb.append( "," );
            }
        }
        sb.append( "]" );
        sb.append( "]" );
        return sb.toString();
    }
}