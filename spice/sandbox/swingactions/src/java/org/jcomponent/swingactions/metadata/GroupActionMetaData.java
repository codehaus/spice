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
 * Defines the metadata of a group action, ie an action which 
 * is displayed as part of a group, such as in menus, toolbars, etc.
 * It extends ActionMetaData as some of the display data of the action 
 * may be overridden when part of the group.  
 * Only the id of the action may not be changed.
 *
 * @author <a href="mailto:mauro.talevi at aquilonia.org">Mauro Talevi</a>
 */
public class GroupActionMetaData extends ActionMetaData
{

    /** The key used for storing the id the group of the action */
    public static final String GROUP_ID = "groupId";

    /** The key used for storing the order in the group */
    public static final String GROUP_ORDER = "groupOrder";

    
    /** The list of mandatory keys */
    private static final String[] MANDATORY_KEYS = new String[] { GROUP_ID, ID };

    /**
     * Constructs GroupActionMetaData object
     */
    public GroupActionMetaData( final Map data )
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
        sb.append( "[GroupActionMetaData groupId=" );
        sb.append( getValue( GROUP_ID ) );
        sb.append( ", id=" );
        sb.append( getValue( ID ) );
        sb.append( ", groupOrder=" );
        sb.append( getValue( GROUP_ORDER ) );
        sb.append( ", name=" );
        sb.append( getValue( NAME ) );
        sb.append( ", shortDescription=" );
        sb.append( getValue( SHORT_DESCRIPTION ) );
        sb.append( ", longDescription=" );
        sb.append( getValue( LONG_DESCRIPTION ) );
        sb.append( ", smallIcon=" );
        sb.append( getValue( SMALL_ICON ) );
        sb.append( ", largeIcon=" );
        sb.append( getValue( LARGE_ICON ) );
        sb.append( ", actionCommandKey=" );
        sb.append( getValue( ACTION_COMMAND_KEY ) );
        sb.append( ", acceleratorKey=" );
        sb.append( getValue( ACCELERATOR_KEY ) );
        sb.append( ", mnemonicKey=" );
        sb.append( getValue( MNEMONIC_KEY ) );
        sb.append( "]" );
        return sb.toString();
    }
}