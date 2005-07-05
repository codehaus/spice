/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
/*
 * This product includes software developed by the
 * Apache Software Foundation (http://www.apache.org/).
 */
package org.codehaus.spice.jervlet.tools.isolate;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Properties;

/**
 * Access to this Properties object is forwarded to a properties
 * object that has been associated with this thread.
 *
 * @author Peter Donald
 * @author Johan Sjoberg
 */
public final class DemuxProperties extends Properties
{
    /** The properties objects associated with each thread. */
    private final InheritableThreadLocal m_properties =
      new InheritableThreadLocal();

    /**
     * Crate a new DemuxProperties instance
     *
     * @param defaults Default properties
     */
    public DemuxProperties( final Properties defaults )
    {
        super( defaults );
    }

    /**
     * Bind the specified Properties object to the current thread.
     *
     * @param properties the Properties object to bind
     */
    public Properties bindProperties( final Properties properties )
    {
        final Properties oldProperties = (Properties)m_properties.get();
        m_properties.set( properties );
        return oldProperties;
    }

    /**
     * Fetch a property
     *
     * @param key The key to search for
     * @return A value for the given key, or <code>null</null>
     *         if none existed.
     */
    public Object get( final Object key )
    {
        return getProperties().get( key );
    }

    /**
     * Store a property
     *
     * @param key The key to be placed into this property list.
     * @param value The corresponding value to <code>key</code>.
     * @return The previous value of the specified key in this
     *         property list, or <code>null</code> if it did not
     *         have one.
     */
    public Object put( final Object key,
                       final Object value )
    {
        return getProperties().put( key, value );
    }

    /**
     * Remove a key-value mapping.
     *
     * @param key The key of the key-value mapping to remove.
     * @return The value to which the key had been mapped to,
     *         or null if the key did not have a mapping.
     */
    public Object remove( final Object key )
    {
        return getProperties().remove( key );
    }

    /**
     * Fetch the size, meaning the number of stored mappings.
     *
     * @return Number of mappings
     */
    public final int size()
    {
        return getProperties().size();
    }

    /**
     * Check if the properties is empty.
     *
     * @return true if no mappings have been stored, else false
     */
    public final boolean isEmpty()
    {
        return getProperties().isEmpty();
    }

    /**
     * Fetch all keys
     *
     * @return Enumeration of stored keys
     */
    public Enumeration keys()
    {
        return getProperties().keys();
    }

    /**
     * Fetch all values
     *
     * @return Enumeration of values
     */
    public Enumeration elements()
    {
        return getProperties().keys();
    }

    /**
     * Check if a value already exists.
     *
     * @param value The value
     * @return true if <code>value</code> existed, else false
     */
    public boolean contains( final Object value )
    {
        return getProperties().contains( value );
    }

    /**
     * Check if a key exists
     *
     * @param key The key
     * @return true if <code>key</code> existed, else false
     */
    public boolean containsKey( final Object key )
    {
        return getProperties().containsKey( key );
    }

    /**
     * Clear out all key-value mapping in this properties object.
     */
    public void clear()
    {
        getProperties().clear();
    }

    /**
     * Create a copy of this properties object.
     *
     * @return A new identical properties object as this
     */
    public Object clone()
    {
        return getProperties().clone();
    }

    /**
     * Create a <code>String</code> representation of this
     * properties object.
     *
     * @return A string
     */
    public String toString()
    {
        return getProperties().toString();
    }

    /**
     * Fetch a property
     *
     * @param key The key to search for
     * @return A property corresponding to <code>key</code>
     */
    public final String getProperty( final String key )
    {
        return getProperties().getProperty( key );
    }

    /**
     * Fetch all property names.
     *
     * @return Enumeration of property names
     */
    public final Enumeration propertyNames()
    {
        return getProperties().propertyNames();
    }

    /**
     * Print this property list out to the specified output stream.
     *
     * @param out PrintStream to write to.
     */
    public final void list( final PrintStream out )
    {
        getProperties().list( out );
    }

    /**
     * Print this property list out to the specified output stream.
     *
     * @param out PrintWriter to write to.
     */
    public final void list( final PrintWriter out )
    {
        getProperties().list( out );
    }

    /**
     * Utility method to retrieve properties bound to current thread (if any).
     */
    private Properties getProperties()
    {
        final Properties properties = (Properties)m_properties.get();
        if( null != properties )
        {
            return properties;
        }
        else
        {
            return defaults;
        }
    }
}
