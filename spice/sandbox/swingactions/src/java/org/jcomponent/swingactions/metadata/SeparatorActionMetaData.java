/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.swingactions.metadata;

import java.util.Map;

/**
 * Defines the metadata of a separator element in the action group
 *
 * @author <a href="mailto:mauro.talevi at aquilonia.org">Mauro Talevi</a>
 */
public class SeparatorActionMetaData extends ActionMetaData
{

    /** The bound value of the separator for the action id */
    public static final String SEPARATOR = "separator";

    /** The key used for storing the id the group of the action */
    public static final String GROUP_ID = "groupId";
    
    /** The list of mandatory keys */
    private static final String[] MANDATORY_KEYS = new String[] { GROUP_ID };

    /**
     * Constructs GroupActionMetaData object
     */
    public SeparatorActionMetaData( final Map data )
    {
        super( data );
    }
    
    /**
     *  Returns the array of mandatory keys
     */
    protected String[] getMandatoryKeys()
    {
        return MANDATORY_KEYS;
    }
    
    /**
     *  String representation
     */
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append( "[SeparatorActionMetaData groupId=" );
        sb.append( getValue( GROUP_ID ) );
        sb.append( "]" );
        return sb.toString();
    }
}