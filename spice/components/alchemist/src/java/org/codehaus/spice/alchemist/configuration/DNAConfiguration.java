/*
 * Copyright (C) The Spice Group. All rights reserved. This software is
 * published under the terms of the Spice Software License version 1.1, a copy
 * of which has been included with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.alchemist.configuration;

import org.codehaus.dna.Configuration;
import org.codehaus.dna.ConfigurationException;
import org.codehaus.dna.impl.DefaultConfiguration;


/**
 * DNA Configuration facade implementation for the Avalon Configuration.
 * 
 * @author Mauro Talevi
 * @version $Revision: 1.1 $ $Date: 2004-06-13 13:35:14 $
 */
public class DNAConfiguration implements Configuration {

    /** String used for default values */
    private static final String EMPTY = "";
    
    /**
     * The Avalon Configuration.
     */
    private final org.apache.avalon.framework.configuration.Configuration _configuration;

    /**
     * Create an instance of configuration facade.
     * 
     * @param configuration
     */
    public DNAConfiguration(
            final org.apache.avalon.framework.configuration.Configuration configuration ) {
        _configuration = configuration;
    }

    /**
     * @see org.apache.avalon.framework.configuration.Configuration#getName()
     */
    public String getName() {
        return _configuration.getName();
    }

    /**
     * @see org.codehaus.dna.Configuration#getLocation()
     */
    public String getLocation() {
        return _configuration.getLocation();
    }

    /**
     * @see org.codehaus.dna.Configuration#getPath()
     */
    public String getPath() {
        return EMPTY;
    }

    /**
     * @see org.codehaus.dna.Configuration#getChild(java.lang.String)
     */
    public Configuration getChild( String arg0 ) {
        return new DNAConfiguration( _configuration.getChild( arg0 ) );
    }

    /**
     * @see org.codehaus.dna.Configuration#getChild(java.lang.String,
     *      boolean)
     */
    public Configuration getChild( String arg0, boolean arg1 ) {
        return new DNAConfiguration( _configuration.getChild( arg0, arg1 ) );
    }

    /**
     * @see org.codehaus.dna.Configuration#getChildren()
     */
    public Configuration[] getChildren() {
        final DefaultConfiguration result = new DefaultConfiguration(
                _configuration.getName(), _configuration.getLocation(), EMPTY );
        final org.apache.avalon.framework.configuration.Configuration[] children = _configuration
                .getChildren();
        for ( int i = 0; i < children.length; i++ ) {
            final Configuration child = new DNAConfiguration( children[i] );
            result.addChild( child );
        }
        return result.getChildren();
    }

    /**
     * @see org.codehaus.dna.Configuration#getChildren(java.lang.String)
     */
    public Configuration[] getChildren( String arg0 ) {
        final DefaultConfiguration result = new DefaultConfiguration(
                _configuration.getName(), _configuration.getLocation(), EMPTY );
        final org.apache.avalon.framework.configuration.Configuration[] children = _configuration
                .getChildren( arg0 );
        for ( int i = 0; i < children.length; i++ ) {
            final Configuration child = new DNAConfiguration( children[i] );
            result.addChild( child );
        }
        return result.getChildren();
    }

    /**
     * @see org.codehaus.dna.Configuration#getAttributeNames()
     */
    public String[] getAttributeNames() {
        return _configuration.getAttributeNames();
    }

    /**
     * @see org.codehaus.dna.Configuration#getAttribute(java.lang.String)
     */
    public String getAttribute( String arg0 ) throws ConfigurationException {
        try {
            return _configuration.getAttribute( arg0 );
        } catch ( org.apache.avalon.framework.configuration.ConfigurationException e ) {
            throw new ConfigurationException( e.getMessage(), e );
        }
    }

    /**
     * @see org.codehaus.dna.Configuration#getAttributeAsInteger(java.lang.String)
     */
    public int getAttributeAsInteger( String arg0 )
            throws ConfigurationException {
        try {
            return _configuration.getAttributeAsInteger( arg0 );
        } catch ( org.apache.avalon.framework.configuration.ConfigurationException e ) {
            throw new ConfigurationException( e.getMessage(), e );
        }
    }

    /**
     * @see org.codehaus.dna.Configuration#getAttributeAsLong(java.lang.String)
     */
    public long getAttributeAsLong( String arg0 ) throws ConfigurationException {
        try {
            return _configuration.getAttributeAsLong( arg0 );
        } catch ( org.apache.avalon.framework.configuration.ConfigurationException e ) {
            throw new ConfigurationException( e.getMessage(), e );
        }
    }

    /**
     * @see org.codehaus.dna.Configuration#getAttributeAsFloat(java.lang.String)
     */
    public float getAttributeAsFloat( String arg0 )
            throws ConfigurationException {
        try {
            return _configuration.getAttributeAsFloat( arg0 );
        } catch ( org.apache.avalon.framework.configuration.ConfigurationException e ) {
            throw new ConfigurationException( e.getMessage(), e);
        }
    }

    /**
     * @see org.codehaus.dna.Configuration#getAttributeAsBoolean(java.lang.String)
     */
    public boolean getAttributeAsBoolean( String arg0 )
            throws ConfigurationException {
        try {
            return _configuration.getAttributeAsBoolean( arg0 );
        } catch ( org.apache.avalon.framework.configuration.ConfigurationException e ) {
            throw new ConfigurationException( e.getMessage(), e);
        }
    }

    /**
     * @see org.codehaus.dna.Configuration#getValue()
     */
    public String getValue() throws ConfigurationException {
        try {
            return _configuration.getValue();
        } catch ( org.apache.avalon.framework.configuration.ConfigurationException e ) {
            throw new ConfigurationException( e.getMessage(), e);
        }
    }

    /**
     * @see org.codehaus.dna.Configuration#getValueAsInteger()
     */
    public int getValueAsInteger() throws ConfigurationException {
        try {
            return _configuration.getValueAsInteger();
        } catch ( org.apache.avalon.framework.configuration.ConfigurationException e ) {
            throw new ConfigurationException( e.getMessage(), e);
        }
    }

    /**
     * @see org.codehaus.dna.Configuration#getValueAsFloat()
     */
    public float getValueAsFloat() throws ConfigurationException {
        try {
            return _configuration.getValueAsFloat();
        } catch ( org.apache.avalon.framework.configuration.ConfigurationException e ) {
            throw new ConfigurationException( e.getMessage(), e);
        }
    }

    /**
     * @see org.codehaus.dna.Configuration#getValueAsBoolean()
     */
    public boolean getValueAsBoolean() throws ConfigurationException {
        try {
            return _configuration.getValueAsBoolean();
        } catch ( org.apache.avalon.framework.configuration.ConfigurationException e ) {
            throw new ConfigurationException( e.getMessage(), e);
        }
    }

    /**
     * @see org.codehaus.dna.Configuration#getValueAsLong()
     */
    public long getValueAsLong() throws ConfigurationException {
        try {
            return _configuration.getValueAsLong();
        } catch ( org.apache.avalon.framework.configuration.ConfigurationException e ) {
            throw new ConfigurationException( e.getMessage(), e);
        }
    }

    /**
     * @see org.codehaus.dna.Configuration#getValue(java.lang.String)
     */
    public String getValue( String arg0 ) {
        return _configuration.getValue( arg0 );
    }

    /**
     * @see org.codehaus.dna.Configuration#getValueAsInteger(int)
     */
    public int getValueAsInteger( int arg0 ) {
        return _configuration.getValueAsInteger( arg0 );
    }

    /**
     * @see org.codehaus.dna.Configuration#getValueAsLong(long)
     */
    public long getValueAsLong( long arg0 ) {
        return _configuration.getValueAsLong( arg0 );
    }

    /**
     * @see org.codehaus.dna.Configuration#getValueAsFloat(float)
     */
    public float getValueAsFloat( float arg0 ) {
        return _configuration.getValueAsFloat( arg0 );
    }

    /**
     * @see org.codehaus.dna.Configuration#getValueAsBoolean(boolean)
     */
    public boolean getValueAsBoolean( boolean arg0 ) {
        return _configuration.getValueAsBoolean( arg0 );
    }

    /**
     * @see org.codehaus.dna.Configuration#getAttribute(java.lang.String,
     *      java.lang.String)
     */
    public String getAttribute( String arg0, String arg1 ) {
        return _configuration.getAttribute( arg0, arg1 );
    }

    /**
     * @see org.codehaus.dna.Configuration#getAttributeAsInteger(java.lang.String,
     *      int)
     */
    public int getAttributeAsInteger( String arg0, int arg1 ) {
        return _configuration.getAttributeAsInteger( arg0, arg1 );
    }

    /**
     * @see org.codehaus.dna.Configuration#getAttributeAsLong(java.lang.String,
     *      long)
     */
    public long getAttributeAsLong( String arg0, long arg1 ) {
        return _configuration.getAttributeAsLong( arg0, arg1 );
    }

    /**
     * @see org.codehaus.dna.Configuration#getAttributeAsFloat(java.lang.String,
     *      float)
     */
    public float getAttributeAsFloat( String arg0, float arg1 ) {
        return _configuration.getAttributeAsFloat( arg0, arg1 );
    }

    /**
     * @see org.codehaus.dna.Configuration#getAttributeAsBoolean(java.lang.String,
     *      boolean)
     */
    public boolean getAttributeAsBoolean( String arg0, boolean arg1 ) {
        return _configuration.getAttributeAsBoolean( arg0, arg1 );
    }
}