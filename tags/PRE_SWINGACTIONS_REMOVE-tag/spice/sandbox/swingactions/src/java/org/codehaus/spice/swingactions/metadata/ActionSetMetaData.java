/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.swingactions.metadata;


/**
 * Defines the metadata of a set of actions
 *
 * @author Mauro Talevi
 */
public class ActionSetMetaData
{

    /** The actions */
    private final ActionMetaData[] m_actions;

    /**
     * Constructs an ActionSetMetaData object from the actions
     * @param actions the array of ActionsMetaData
     */
    public ActionSetMetaData( final ActionMetaData[] actions )
    {
        if ( actions == null )
        {
            throw new NullPointerException( "actions" );
        }
        m_actions = actions;
    }
    
    /**
     * Returns the actions in this set
     * @return the array of ActionMetaData
     */
    public ActionMetaData[] getActions()
    {
        return m_actions;
    }

    /**
     * Returns an Action by the Id data value for a given key
     * @return the ActionMetaData 
     */
    public ActionMetaData getAction( String id )
    {
        for ( int i = 0; i < m_actions.length; i++ )
        { 
            final ActionMetaData action = m_actions[ i ];
            if ( action.getValue( ActionMetaData.ID ).equals( id ))
            {
                return action;
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
        sb.append( "[ActionSetMetaData ids=[" );
        for ( int i = 0; i < m_actions.length; i++ )
        { 
            sb.append( m_actions[ i ].getValue( ActionMetaData.ID ) );
            if ( i < m_actions.length - 1 )
            {
                sb.append( "," );
            }
        }
        sb.append( "]" );
        sb.append( "]" );
        return sb.toString();
    }
}