/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.salt.i18n;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * A class to simplify extracting localized strings, icons and other common
 * resources from a ResourceBundle.
 *
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2003-12-02 02:15:04 $
 */
public class Resources
{
    /** Local of Resources. */
    private final Locale m_locale;

    /** Base name of resource bundle. */
    private final String m_baseName;

    /** ClassLoader from which to load resources. */
    private final ClassLoader m_classLoader;

    /** Resource bundle referenced by manager. */
    private ResourceBundle m_bundle;

    /**
     * Constructor that builds a manager in default locale using specified
     * ClassLoader.
     *
     * @param baseName the base name of ResourceBundle
     * @param classLoader the classLoader to load ResourceBundle from
     */
    public Resources( final String baseName, final ClassLoader classLoader )
    {
        this( baseName, Locale.getDefault(), classLoader );
    }

    /**
     * Constructor that builds a manager in specified locale.
     *
     * @param baseName the base name of ResourceBundle
     * @param locale the Locale for resource bundle
     * @param classLoader the classLoader to load ResourceBundle from
     */
    public Resources( final String baseName,
                      final Locale locale,
                      final ClassLoader classLoader )
    {
        if( null == baseName )
        {
            throw new NullPointerException( "baseName" );
        }
        if( null == locale )
        {
            throw new NullPointerException( "locale" );
        }
        if( null == classLoader )
        {
            throw new NullPointerException( "classLoader" );
        }

        m_baseName = baseName;
        m_locale = locale;
        m_classLoader = classLoader;
    }

    /**
     * Retrieve a boolean from bundle.
     *
     * @param key the key of resource
     * @param defaultValue the default value if key is missing
     * @return the resource boolean
     */
    public boolean getBoolean( final String key, final boolean defaultValue )
    {
        try
        {
            return getBoolean( key );
        }
        catch( final MissingResourceException mre )
        {
            return defaultValue;
        }
    }

    /**
     * Retrieve a boolean from bundle.
     *
     * @param key the key of resource
     * @return the resource boolean
     */
    public boolean getBoolean( final String key )
        throws MissingResourceException
    {
        final ResourceBundle bundle = getBundle();
        final String value = bundle.getString( key );
        return value.equalsIgnoreCase( "true" );
    }

    /**
     * Retrieve a byte from bundle.
     *
     * @param key the key of resource
     * @param defaultValue the default value if key is missing
     * @return the resource byte
     */
    public byte getByte( final String key, final byte defaultValue )
    {
        try
        {
            return getByte( key );
        }
        catch( final MissingResourceException mre )
        {
            return defaultValue;
        }
    }

    /**
     * Retrieve a byte from bundle.
     *
     * @param key the key of resource
     * @return the resource byte
     */
    public byte getByte( final String key )
        throws MissingResourceException
    {
        final ResourceBundle bundle = getBundle();
        final String value = bundle.getString( key );
        try
        {
            return Byte.parseByte( value );
        }
        catch( final NumberFormatException nfe )
        {
            throw new MissingResourceException(
                "Expecting a byte value but got " + value,
                "java.lang.String",
                key );
        }
    }

    /**
     * Retrieve a char from bundle.
     *
     * @param key the key of resource
     * @param defaultValue the default value if key is missing
     * @return the resource char
     */
    public char getChar( final String key, final char defaultValue )
    {
        try
        {
            return getChar( key );
        }
        catch( final MissingResourceException mre )
        {
            return defaultValue;
        }
    }

    /**
     * Retrieve a char from bundle.
     *
     * @param key the key of resource
     * @return the resource char
     */
    public char getChar( final String key )
        throws MissingResourceException
    {
        final ResourceBundle bundle = getBundle();
        final String value = bundle.getString( key );

        if( 1 == value.length() )
        {
            return value.charAt( 0 );
        }
        else
        {
            throw new MissingResourceException(
                "Expecting a char value but got " + value,
                "java.lang.String",
                key );
        }
    }

    /**
     * Retrieve a short from bundle.
     *
     * @param key the key of resource
     * @param defaultValue the default value if key is missing
     * @return the resource short
     */
    public short getShort( final String key, final short defaultValue )
    {
        try
        {
            return getShort( key );
        }
        catch( final MissingResourceException mre )
        {
            return defaultValue;
        }
    }

    /**
     * Retrieve a short from bundle.
     *
     * @param key the key of resource
     * @return the resource short
     */
    public short getShort( final String key )
        throws MissingResourceException
    {
        final ResourceBundle bundle = getBundle();
        final String value = bundle.getString( key );
        try
        {
            return Short.parseShort( value );
        }
        catch( final NumberFormatException nfe )
        {
            throw new MissingResourceException(
                "Expecting a short value but got " + value,
                "java.lang.String",
                key );
        }
    }

    /**
     * Retrieve a integer from bundle.
     *
     * @param key the key of resource
     * @param defaultValue the default value if key is missing
     * @return the resource integer
     */
    public int getInteger( final String key, final int defaultValue )
    {
        try
        {
            return getInteger( key );
        }
        catch( final MissingResourceException mre )
        {
            return defaultValue;
        }
    }

    /**
     * Retrieve a integer from bundle.
     *
     * @param key the key of resource
     * @return the resource integer
     */
    public int getInteger( final String key )
        throws MissingResourceException
    {
        final ResourceBundle bundle = getBundle();
        final String value = bundle.getString( key );
        try
        {
            return Integer.parseInt( value );
        }
        catch( final NumberFormatException nfe )
        {
            throw new MissingResourceException(
                "Expecting a integer value but got " + value,
                "java.lang.String",
                key );
        }
    }

    /**
     * Retrieve a long from bundle.
     *
     * @param key the key of resource
     * @param defaultValue the default value if key is missing
     * @return the resource long
     */
    public long getLong( final String key, final long defaultValue )
    {
        try
        {
            return getLong( key );
        }
        catch( final MissingResourceException mre )
        {
            return defaultValue;
        }
    }

    /**
     * Retrieve a long from bundle.
     *
     * @param key the key of resource
     * @return the resource long
     */
    public long getLong( final String key )
        throws MissingResourceException
    {
        final ResourceBundle bundle = getBundle();
        final String value = bundle.getString( key );
        try
        {
            return Long.parseLong( value );
        }
        catch( final NumberFormatException nfe )
        {
            throw new MissingResourceException(
                "Expecting a long value but got " + value,
                "java.lang.String",
                key );
        }
    }

    /**
     * Retrieve a float from bundle.
     *
     * @param key the key of resource
     * @param defaultValue the default value if key is missing
     * @return the resource float
     */
    public float getFloat( final String key, final float defaultValue )
    {
        try
        {
            return getFloat( key );
        }
        catch( final MissingResourceException mre )
        {
            return defaultValue;
        }
    }

    /**
     * Retrieve a float from bundle.
     *
     * @param key the key of resource
     * @return the resource float
     */
    public float getFloat( final String key )
        throws MissingResourceException
    {
        final ResourceBundle bundle = getBundle();
        final String value = bundle.getString( key );
        try
        {
            return Float.parseFloat( value );
        }
        catch( final NumberFormatException nfe )
        {
            throw new MissingResourceException(
                "Expecting a float value but got " + value,
                "java.lang.String",
                key );
        }
    }

    /**
     * Retrieve a double from bundle.
     *
     * @param key the key of resource
     * @param defaultValue the default value if key is missing
     * @return the resource double
     */
    public double getDouble( final String key, final double defaultValue )
    {
        try
        {
            return getDouble( key );
        }
        catch( final MissingResourceException mre )
        {
            return defaultValue;
        }
    }

    /**
     * Retrieve a double from bundle.
     *
     * @param key the key of resource
     * @return the resource double
     */
    public double getDouble( final String key )
        throws MissingResourceException
    {
        final ResourceBundle bundle = getBundle();
        final String value = bundle.getString( key );
        try
        {
            return Double.parseDouble( value );
        }
        catch( final NumberFormatException nfe )
        {
            throw new MissingResourceException(
                "Expecting a double value but got " + value,
                "java.lang.String",
                key );
        }
    }

    /**
     * Retrieve a date from bundle.
     *
     * @param key the key of resource
     * @param defaultValue the default value if key is missing
     * @return the resource date
     */
    public Date getDate( final String key, final Date defaultValue )
    {
        try
        {
            return getDate( key );
        }
        catch( final MissingResourceException mre )
        {
            return defaultValue;
        }
    }

    /**
     * Retrieve a date from bundle.
     *
     * @param key the key of resource
     * @return the resource date
     */
    public Date getDate( final String key )
        throws MissingResourceException
    {
        final ResourceBundle bundle = getBundle();
        final String value = bundle.getString( key );
        try
        {
            final DateFormat format =
                DateFormat.getDateInstance( DateFormat.DEFAULT, m_locale );
            return format.parse( value );
        }
        catch( final ParseException pe )
        {
            throw new MissingResourceException(
                "Expecting a date value but got " + value,
                "java.lang.String",
                key );
        }
    }

    /**
     * Retrieve a time from bundle.
     *
     * @param key the key of resource
     * @param defaultValue the default value if key is missing
     * @return the resource time
     */
    public Date getTime( final String key, final Date defaultValue )
    {
        try
        {
            return getTime( key );
        }
        catch( final MissingResourceException mre )
        {
            return defaultValue;
        }
    }

    /**
     * Retrieve a time from bundle.
     *
     * @param key the key of resource
     * @return the resource time
     */
    public Date getTime( final String key )
        throws MissingResourceException
    {
        final ResourceBundle bundle = getBundle();
        final String value = bundle.getString( key );
        try
        {
            final DateFormat format =
                DateFormat.getTimeInstance( DateFormat.DEFAULT, m_locale );
            return format.parse( value );
        }
        catch( final ParseException pe )
        {
            throw new MissingResourceException(
                "Expecting a time value but got " + value,
                "java.lang.String",
                key );
        }
    }

    /**
     * Retrieve a time from bundle.
     *
     * @param key the key of resource
     * @param defaultValue the default value if key is missing
     * @return the resource time
     */
    public Date getDateTime( final String key, final Date defaultValue )
    {
        try
        {
            return getDateTime( key );
        }
        catch( final MissingResourceException mre )
        {
            return defaultValue;
        }
    }

    /**
     * Retrieve a date + time from bundle.
     *
     * @param key the key of resource
     * @return the resource date + time
     */
    public Date getDateTime( final String key )
        throws MissingResourceException
    {
        final ResourceBundle bundle = getBundle();
        final String value = bundle.getString( key );
        try
        {
            final DateFormat format =
                DateFormat.getDateTimeInstance( DateFormat.DEFAULT,
                                                DateFormat.DEFAULT,
                                                m_locale );
            return format.parse( value );
        }
        catch( final ParseException pe )
        {
            throw new MissingResourceException(
                "Expecting a time value but got " + value,
                "java.lang.String",
                key );
        }
    }

    /**
     * Retrieve a raw string from bundle.
     *
     * @param key the key of resource
     * @return the resource string
     */
    public String getString( final String key )
        throws MissingResourceException
    {
        final ResourceBundle bundle = getBundle();
        return bundle.getString( key );
    }

    /**
     * Retrieve a raw string from bundle.
     *
     * @param key the key of resource
     * @param defaultValue the default value if key is missing
     * @return the resource string
     */
    public String getString( final String key, final String defaultValue )
    {
        try
        {
            return getString( key );
        }
        catch( final MissingResourceException mre )
        {
            return defaultValue;
        }
    }

    /**
     * Retrieve a string from resource bundle and format it with specified
     * args.
     *
     * @param key the key for resource
     * @param arg1 an arg
     * @return the formatted string
     */
    public String format( final String key, final Object arg1 )
    {
        final Object[] args = new Object[]{arg1};
        return format( key, args );
    }

    /**
     * Retrieve a string from resource bundle and format it with specified
     * args.
     *
     * @param key the key for resource
     * @param arg1 an arg
     * @param arg2 an arg
     * @return the formatted string
     */
    public String format( final String key,
                          final Object arg1,
                          final Object arg2 )
    {
        final Object[] args = new Object[]{arg1, arg2};
        return format( key, args );
    }

    /**
     * Retrieve a string from resource bundle and format it with specified
     * args.
     *
     * @param key the key for resource
     * @param arg1 an arg
     * @param arg2 an arg
     * @param arg3 an arg
     * @return the formatted string
     */
    public String format( final String key,
                          final Object arg1,
                          final Object arg2,
                          final Object arg3 )
    {
        final Object[] args = new Object[]{arg1, arg2, arg3};
        return format( key, args );
    }

    /**
     * Retrieve a string from resource bundle and format it with specified
     * args.
     *
     * @param key the key for resource
     * @param arg1 an arg
     * @param arg2 an arg
     * @param arg3 an arg
     * @param arg4 an arg
     * @return the formatted string
     */
    public String format( final String key,
                          final Object arg1,
                          final Object arg2,
                          final Object arg3,
                          final Object arg4 )
    {
        final Object[] args = new Object[]{arg1, arg2, arg3, arg4};
        return format( key, args );
    }

    /**
     * Retrieve a string from resource bundle and format it with specified
     * args.
     *
     * @param key the key for resource
     * @param arg1 an arg
     * @param arg2 an arg
     * @param arg3 an arg
     * @param arg4 an arg
     * @param arg5 an arg
     * @return the formatted string
     */
    public String format( final String key,
                          final Object arg1,
                          final Object arg2,
                          final Object arg3,
                          final Object arg4,
                          final Object arg5 )
    {
        final Object[] args = new Object[]{arg1, arg2, arg3, arg4, arg5};
        return format( key, args );
    }

    /**
     * Retrieve a string from resource bundle and format it with specified
     * args.
     *
     * @param key the key for resource
     * @param args an array of args
     * @return the formatted string
     */
    public String format( final String key, final Object[] args )
    {
        try
        {
            final String pattern = getString( key );
            return MessageFormat.format( pattern, args );
        }
        catch( final MissingResourceException mre )
        {
            final StringBuffer sb = new StringBuffer();
            sb.append( "Unknown resource. Bundle: '" );
            sb.append( m_baseName );
            sb.append( "' Key: '" );
            sb.append( key );
            sb.append( "' Args: '" );

            for( int i = 0; i < args.length; i++ )
            {
                if( 0 != i )
                    sb.append( "', '" );
                sb.append( args[ i ] );
            }

            sb.append( "' Reason: " );
            sb.append( mre );

            return sb.toString();
        }
    }

    /**
     * Retrieve underlying ResourceBundle. If bundle has not been loaded it will
     * be loaded by this method. Access is given in case other resources need to
     * be extracted that this Manager does not provide simplified access to.
     *
     * @return the ResourceBundle
     * @throws MissingResourceException if an error occurs
     */
    public synchronized final ResourceBundle getBundle()
        throws MissingResourceException
    {
        if( null == m_bundle )
        {
            // bundle wasn't cached, so load it, cache it, and return it.
            m_bundle = ResourceBundle.
                getBundle( m_baseName, m_locale, m_classLoader );
        }
        return m_bundle;
    }
}
