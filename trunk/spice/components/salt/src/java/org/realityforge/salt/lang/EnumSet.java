/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.salt.lang;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A Utility class for managing a set of name-integer constants.
 *
 * <p>The Class has factory methods that will allow the user to scan a Class
 * and find all integer constants and place them in an EnumSet. The constants
 * are placed into the EnumSet based on the name of the field that declared
 * constant. The {@link #createFrom(Class,String)} factory method accepts a
 * Regular Expression which is used to match thje constants to place in the
 * set and to extract the name for the constant. The first "group" in the
 * Regex is the name of the constant. Consider the example;</p>
 *
 * <pre>
 * public class ShaderRenderer
 * {
 *    public static final int CLIP_NOCLIP     = 0;
 *    public static final int CLIP_BACK       = 1;
 *    public static final int CLIP_FRONT      = 2;
 *    public static final EnumSet CLIP_ENUMS  =
 *              EnumSet.createFrom( ShaderRenderer.class, "CLIP_(.*)" );
 *
 *    public static final int FUNC_SIN        = 0;
 *    public static final int FUNC_COS        = 1;
 *    public static final int FUNC_MOD        = 2;
 *    public static final EnumSet FUNC_ENUMS  =
 *              EnumSet.createFrom( ShaderRenderer.class, "FUNC_(.*)" );
 * }
 * </pre>
 *
 * <p>The above example will create two EnumSets for the class. The first EnumSet
 * includes the enums {NOCLIP=0,BACK=1,FRONT=2} and the seconds includes the
 * enums {SIN=0,COS=1,MOD=2}</p>
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 */
public final class EnumSet
{
    /**
     * A map of name to integer code.
     */
    private final Map m_nameMap = new HashMap();

    /**
     * A map of integer code to name.
     */
    private final Map m_codeMap = new HashMap();

    /**
     * Return a read-only set of names in the EnumSet.
     *
     * @return a read-only set of names in the EnumSet.
     */
    public final Set getNames()
    {
        return Collections.unmodifiableSet( m_nameMap.keySet() );
    }

    /**
     * Return a read-only set of codes in the EnumSet.
     *
     * @return a read-only set of codes in the EnumSet.
     */
    public final Set getCodes()
    {
        return Collections.unmodifiableSet( m_codeMap.keySet() );
    }

    /**
     * Return the name for specified code.
     *
     * @param code the code to lookup.
     * @return the name for specified code.
     * @throws IllegalArgumentException if code is not in the EnumSet.
     */
    public final String getNameFor( final int code )
    {
        final String name = (String)m_codeMap.get( new Integer( code ) );
        if( null == name )
        {
            final String message = "Unknown code " + code;
            throw new IllegalArgumentException( message );
        }
        return name;
    }

    /**
     * Return the code for specified name.
     *
     * @param name the name to lookup.
     * @return the code for specified name.
     * @throws IllegalArgumentException if name is not in the EnumSet.
     */
    public final int getCodeFor( final String name )
    {
        final Integer code = (Integer)m_nameMap.get( name );
        if( null == code )
        {
            final String message = "Unknown name " + name;
            throw new IllegalArgumentException( message );
        }
        return code.intValue();
    }

    /**
     * Create an EnumSet for specified Class including all
     * integer constants. A shortcut for
     * <tt>createFrom( clazz, "(.*)" )</tt>.
     *
     * @param clazz the class to extract constants from
     * @return the created EnumSet
     */
    public static EnumSet createFrom( final Class clazz )
    {
        return createFrom( clazz, "(.*)" );
    }

    /**
     * Create an EnumSet for specified Class including all
     * integer constants that match specified pattern. A
     * shortcut for <tt>createFrom( clazz, "(.*)", true )</tt>.
     *
     * @param clazz the class to extract constants from
     * @param match the pattern that constants must match
     * @return the created EnumSet
     */
    public static EnumSet createFrom( final Class clazz,
                                      final String match )

    {
        return createFrom( clazz, match, true );
    }

    /**
     * Create an EnumSet for specified Class including all
     * integer constants that match specified pattern and scanning
     * superclass if deep is true.
     *
     * @param clazz the class to extract constants from
     * @param match the pattern that constants must match
     * @param deep if true will scan all parent classes to locate constants
     * @return the created EnumSet
     */
    public static EnumSet createFrom( final Class clazz,
                                      final String match,
                                      final boolean deep )
    {
        final Pattern pattern = Pattern.compile( match );

        final EnumSet set = new EnumSet();
        final Field[] fields = getFields( clazz, deep );
        for( int i = 0; i < fields.length; i++ )
        {
            final Field field = fields[ i ];
            final int modifiers = field.getModifiers();
            if( !Modifier.isFinal( modifiers ) ||
                !Modifier.isPublic( modifiers ) ||
                !Modifier.isStatic( modifiers ) ||
                field.getType() != Integer.TYPE )
            {
                continue;
            }
            final String name = field.getName();
            final int value;
            try
            {
                value = field.getInt( null );
            }
            catch( final Exception e )
            {
                final String message = "Unable to get value from field";
                throw new IllegalStateException( message );
            }

            final Matcher matcher = pattern.matcher( name );
            if( matcher.matches() )
            {
                final int count = matcher.groupCount();
                String key = name;
                if( 0 != count )
                {
                    key = matcher.group( 1 );
                }
                set.add( key, value );
            }
        }

        return set;
    }

    /**
     * Return the fields for specified Class. If deep is true
     * then it will return all the fields of the class, including
     * fields defined in super-classes, otherwise it will just
     * return the fields declared in specified class.
     *
     * @param clazz the class that fields are retrieved from
     * @param deep if true will include fields in super classes
     * @return an array of fields
     */
    private static Field[] getFields( final Class clazz,
                                      final boolean deep )
    {
        final Field[] fields;
        if( deep )
        {
            fields = clazz.getFields();
        }
        else
        {
            fields = clazz.getDeclaredFields();
        }
        return fields;
    }

    /**
     * Helper method for adding a name-code mapping to EnumSet.
     *
     * @param name the name of enum.
     * @param code the code of enum.
     */
    private void add( final String name, final int code )
    {
        m_nameMap.put( name, new Integer( code ) );
        m_codeMap.put( new Integer( code ), name );
    }
}
