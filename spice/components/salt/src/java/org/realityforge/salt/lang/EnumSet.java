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
 * A Utility class for extracting a set of int constants from a Java Class.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 */
public final class EnumSet
{
   /**
    * A map of name to integer code.
    */
   private final Map _nameMap = new HashMap();

   /**
    * A map of integer code to name.
    */
   private final Map _codeMap = new HashMap();

   /**
    * Return a read-only set of names in the EnumSet.
    *
    * @return a read-only set of names in the EnumSet.
    */
   public final Set getNames()
   {
      return Collections.unmodifiableSet( _nameMap.keySet() );
   }

   /**
    * Return a read-only set of codes in the EnumSet.
    *
    * @return a read-only set of codes in the EnumSet.
    */
   public final Set getCodes()
   {
      return Collections.unmodifiableSet( _codeMap.keySet() );
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
      final String name = (String) _codeMap.get( new Integer( code ) );
      if ( null == name )
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
      final Integer code = (Integer) _nameMap.get( name );
      if ( null == code )
      {
         final String message = "Unknown name " + name;
         throw new IllegalArgumentException( message );
      }
      return code.intValue();
   }

   public static EnumSet createFrom( final Class clazz )

   {
      return createFrom( clazz, "(.*)" );
   }

   public static EnumSet createFrom( final Class clazz,
                                     final String match )

   {
      return createFrom( clazz, match, true );
   }

   public static EnumSet createFrom( final Class clazz,
                                     final String match,
                                     final boolean deep )
   {
      final Pattern pattern = Pattern.compile( match );

      final EnumSet set = new EnumSet();
      final Field[] fields = getFields( clazz, deep );
      for ( int i = 0; i < fields.length; i++ )
      {
         final Field field = fields[ i ];
         final int modifiers = field.getModifiers();
         if ( !Modifier.isFinal( modifiers ) ||
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
         catch ( final Exception e )
         {
            final String message = "Unable to get value from field";
            throw new IllegalStateException( message );
         }

         final Matcher matcher = pattern.matcher( name );
         if ( matcher.matches() )
         {
            final int count = matcher.groupCount();
            String key = name;
            if ( 0 != count )
            {
               key = matcher.group( 1 );
            }
            set.add( key, value );
         }
      }

      return set;
   }

   private static Field[] getFields( final Class clazz, final boolean deep )
   {
      final Field[] fields;
      if ( deep )
      {
         fields = clazz.getFields();
      }
      else
      {
         fields = clazz.getDeclaredFields();
      }
      return fields;
   }

   private void add( final String name, final int value )
   {
      _nameMap.put( name, new Integer( value ) );
      _codeMap.put( new Integer( value ), name );
   }
}
