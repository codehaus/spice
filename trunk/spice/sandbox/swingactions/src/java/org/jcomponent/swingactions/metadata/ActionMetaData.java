/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.swingactions.metadata;

import java.util.Map;
import java.util.Set;

import javax.swing.Action;

/**
 * Defines the metadata of a single action
 *
 * @author <a href="mailto:mauro.talevi at aquilonia.org">Mauro Talevi</a>
 */
public class ActionMetaData
{

    /** The key used for storing the id the action */
    public static final String ID = "Id";
    /** The key used for storing the name the action */
    public static final String NAME = Action.NAME;
    /** The key used for storing a short description of the action */
    public static final String SHORT_DESCRIPTION = Action.SHORT_DESCRIPTION;
    /** The key used for storing a long description of the action */
    public static final String LONG_DESCRIPTION = Action.LONG_DESCRIPTION;
    /** The key used for storing a small icon for the action */
    public static final String SMALL_ICON = Action.SMALL_ICON;
    /** The key used for storing a large icon for the action */
    public static final String LARGE_ICON = "LargeIcon";
    /** The key used for storing the command key the action */
    public static final String ACTION_COMMAND_KEY = Action.ACTION_COMMAND_KEY;
    /** The key used for storing the accelerator key for the action */
    public static final String ACCELERATOR_KEY = Action.ACCELERATOR_KEY;
    /** The key used for storing the mnemonic key for the action */
    public static final String MNEMONIC_KEY = Action.MNEMONIC_KEY;

    /** The list of mandatory keys */
    private static final String[] MANDATORY_KEYS = new String[] { ID, NAME };

    /** The map holding the metadata */
    protected final Map m_data;

    /**
     * Constructs ActionMetaData object
     */
    public ActionMetaData( final Map data )
    {
        if ( data == null )
        {
            throw new NullPointerException( "data" );
        }
        m_data = data;
        checkData();        
    }
    
    /**
     * Returns a data object for a given key
     * @return the metadata Object indexed by the key
     */
    public String[] getKeys()
    {
        final Set keys = m_data.keySet();
        return (String[])keys.toArray(new String[keys.size()]);
    }

    /**
     * Returns a data value for a given key
     * @return the value String
     */
    public String getValue( String key )
    {
        return (String)m_data.get( key );
    }

    /**
     *  Returns the array of mandatory keys
     */
    protected String[] getMandatoryKeys()
    {
        return MANDATORY_KEYS;
    }
    
    /**
     *  Checks data for null entries in mandatory entries
     *  @throws NullPointerException
     */
    private void checkData()
    {
        final String[] mandatoryKeys = getMandatoryKeys();
        for ( int i = 0; i < mandatoryKeys.length; i++ )
        {
            if ( m_data.get( mandatoryKeys[i] ) == null )
            {
                throw new NullPointerException( mandatoryKeys[i] );
            }
            
        }
    }
    
    /**
     *  String representation
     */
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append( "[ActionMetaData id=" );
        sb.append( getValue( ID ) );
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