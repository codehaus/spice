/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.salt.i18n;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import junit.framework.TestCase;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.10 $ $Date: 2003-06-13 00:32:44 $
 */
public class ResourcesTestCase
    extends TestCase
{
    public ResourcesTestCase( final String name )
    {
        super( name );
    }

    protected void setUp() throws Exception
    {
        MockResourceBundle.cleanResourceSet();
    }

    public void testGetBoolean()
        throws Exception
    {
        final Resources resources =
            new Resources( "org.realityforge.salt.i18n.MockResourceBundle",
                           Locale.getDefault(),
                           MockResourceBundle.class.getClassLoader() );
        MockResourceBundle.addResource( "rez", "true" );
        assertEquals( true, resources.getBoolean( "rez" ) );
    }

    public void testGetBooleanWithDefault()
        throws Exception
    {
        final Resources resources =
            new Resources( "org.realityforge.salt.i18n.MockResourceBundle",
                           Locale.getDefault(),
                           MockResourceBundle.class.getClassLoader() );
        assertEquals( true, resources.getBoolean( "rez", true ) );
    }

    public void testGetByte()
        throws Exception
    {
        final Resources resources =
            new Resources( "org.realityforge.salt.i18n.MockResourceBundle",
                           Locale.getDefault(),
                           MockResourceBundle.class.getClassLoader() );
        MockResourceBundle.addResource( "rez", "0" );
        assertEquals( 0, resources.getByte( "rez" ) );
    }

    public void testGetByteWithDefault()
        throws Exception
    {
        final Resources resources =
            new Resources( "org.realityforge.salt.i18n.MockResourceBundle",
                           Locale.getDefault(),
                           MockResourceBundle.class.getClassLoader() );
        assertEquals( 2, resources.getByte( "rez", (byte)2 ) );
    }

    public void testGetInteger()
        throws Exception
    {
        final Resources resources =
            new Resources( "org.realityforge.salt.i18n.MockResourceBundle",
                           Locale.getDefault(),
                           MockResourceBundle.class.getClassLoader() );
        MockResourceBundle.addResource( "rez", "0" );
        assertEquals( 0, resources.getInteger( "rez" ) );
    }

    public void testGetIntegerWithDefault()
        throws Exception
    {
        final Resources resources =
            new Resources( "org.realityforge.salt.i18n.MockResourceBundle",
                           Locale.getDefault(),
                           MockResourceBundle.class.getClassLoader() );
        assertEquals( 2, resources.getInteger( "rez", (byte)2 ) );
    }

    public void testGetFloat()
        throws Exception
    {
        final Resources resources =
            new Resources( "org.realityforge.salt.i18n.MockResourceBundle",
                           Locale.getDefault(),
                           MockResourceBundle.class.getClassLoader() );
        MockResourceBundle.addResource( "rez", "0" );
        assertEquals( 0.0f, resources.getFloat( "rez" ), 0.0 );
    }

    public void testGetFloatWithDefault()
        throws Exception
    {
        final Resources resources =
            new Resources( "org.realityforge.salt.i18n.MockResourceBundle",
                           Locale.getDefault(),
                           MockResourceBundle.class.getClassLoader() );
        assertEquals( 2f, resources.getFloat( "rez", 2f ), 0.0 );
    }

    public void testGetDouble()
        throws Exception
    {
        final Resources resources =
            new Resources( "org.realityforge.salt.i18n.MockResourceBundle",
                           Locale.getDefault(),
                           MockResourceBundle.class.getClassLoader() );
        MockResourceBundle.addResource( "rez", "0" );
        assertEquals( 0, resources.getDouble( "rez" ), 0.0 );
    }

    public void testGetDoubleWithDefault()
        throws Exception
    {
        final Resources resources =
            new Resources( "org.realityforge.salt.i18n.MockResourceBundle",
                           Locale.getDefault(),
                           MockResourceBundle.class.getClassLoader() );
        assertEquals( 2.0, resources.getDouble( "rez", 2.0 ), 0.0 );
    }

    public void testGetLong()
        throws Exception
    {
        final Resources resources =
            new Resources( "org.realityforge.salt.i18n.MockResourceBundle",
                           Locale.getDefault(),
                           MockResourceBundle.class.getClassLoader() );
        MockResourceBundle.addResource( "rez", "0" );
        assertEquals( 0, resources.getLong( "rez" ) );
    }

    public void testGetLongWithDefault()
        throws Exception
    {
        final Resources resources =
            new Resources( "org.realityforge.salt.i18n.MockResourceBundle",
                           Locale.getDefault(),
                           MockResourceBundle.class.getClassLoader() );
        assertEquals( 2, resources.getLong( "rez", 2 ) );
    }

    public void testGetShort()
        throws Exception
    {
        final Resources resources =
            new Resources( "org.realityforge.salt.i18n.MockResourceBundle",
                           Locale.getDefault(),
                           MockResourceBundle.class.getClassLoader() );
        MockResourceBundle.addResource( "rez", "0" );
        assertEquals( 0, resources.getShort( "rez" ) );
    }

    public void testGetShortWithDefault()
        throws Exception
    {
        final Resources resources =
            new Resources( "org.realityforge.salt.i18n.MockResourceBundle",
                           Locale.getDefault(),
                           MockResourceBundle.class.getClassLoader() );
        assertEquals( 2, resources.getShort( "rez", (short)2 ) );
    }

    public void testGetChar()
        throws Exception
    {
        final Resources resources =
            new Resources( "org.realityforge.salt.i18n.MockResourceBundle",
                           Locale.getDefault(),
                           MockResourceBundle.class.getClassLoader() );
        MockResourceBundle.addResource( "rez", "a" );
        assertEquals( 'a', resources.getChar( "rez" ) );
    }

    public void testGetCharWithDefault()
        throws Exception
    {
        final Resources resources =
            new Resources( "org.realityforge.salt.i18n.MockResourceBundle",
                           Locale.getDefault(),
                           MockResourceBundle.class.getClassLoader() );
        assertEquals( 'a', resources.getChar( "rez", 'a' ) );
    }

    public void testGetDate()
        throws Exception
    {
        final Resources resources =
            new Resources( "org.realityforge.salt.i18n.MockResourceBundle",
                           Locale.getDefault(),
                           MockResourceBundle.class.getClassLoader() );
        final DateFormat format =
            DateFormat.getDateInstance( DateFormat.DEFAULT, Locale.getDefault() );
        final String current = format.format( new Date() );
        MockResourceBundle.addResource( "rez", current );
        final Date expected = format.parse( current );
        assertEquals( expected, resources.getDate( "rez" ) );
    }

    public void testGetDateWithDefault()
        throws Exception
    {
        final Resources resources =
            new Resources( "org.realityforge.salt.i18n.MockResourceBundle",
                           Locale.getDefault(),
                           MockResourceBundle.class.getClassLoader() );
        final Date date = new Date();
        assertEquals( date, resources.getDate( "rez", date ) );
    }

    public void testGetTime()
        throws Exception
    {
        final Resources resources =
            new Resources( "org.realityforge.salt.i18n.MockResourceBundle",
                           Locale.getDefault(),
                           MockResourceBundle.class.getClassLoader() );
        final Date date = new Date();
        final DateFormat format =
            DateFormat.getTimeInstance( DateFormat.DEFAULT, Locale.getDefault() );
        final String current = format.format( date );
        final Date expected = format.parse( current );
        MockResourceBundle.addResource( "rez", current );
        assertEquals( expected, resources.getTime( "rez" ) );
    }

    public void testGetTimeWithDefault()
        throws Exception
    {
        final Resources resources =
            new Resources( "org.realityforge.salt.i18n.MockResourceBundle",
                           Locale.getDefault(),
                           MockResourceBundle.class.getClassLoader() );
        final Date date = new Date();
        assertEquals( date, resources.getTime( "rez", date ) );
    }

    public void testGetDateTime()
        throws Exception
    {
        final Resources resources =
            new Resources( "org.realityforge.salt.i18n.MockResourceBundle",
                           Locale.getDefault(),
                           MockResourceBundle.class.getClassLoader() );
        final Date date = new Date();
        final DateFormat format =
            DateFormat.getDateTimeInstance( DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.getDefault() );
        final String current = format.format( date );
        final Date expected = format.parse( current );
        MockResourceBundle.addResource( "rez", current );
        assertEquals( expected, resources.getDateTime( "rez" ) );
    }

    public void testGetDateTimeWithDefault()
        throws Exception
    {
        final Resources resources =
            new Resources( "org.realityforge.salt.i18n.MockResourceBundle",
                           Locale.getDefault(),
                           MockResourceBundle.class.getClassLoader() );
        final Date date = new Date();
        assertEquals( date, resources.getDateTime( "rez", date ) );
    }

    public void testGetString()
        throws Exception
    {
        final Resources resources =
            new Resources( "org.realityforge.salt.i18n.MockResourceBundle",
                           Locale.getDefault(),
                           MockResourceBundle.class.getClassLoader() );
        MockResourceBundle.addResource( "rez", "a message in a bottle" );
        assertEquals(  "a message in a bottle", resources.getString( "rez" ) );
    }

    public void testGetStringWithOneArg()
        throws Exception
    {
        final Resources resources =
            new Resources( "org.realityforge.salt.i18n.MockResourceBundle",
                           Locale.getDefault(),
                           MockResourceBundle.class.getClassLoader() );
        MockResourceBundle.addResource( "rez",
                                        "a message in a bottle said {0}" );
        assertEquals( "a message in a bottle said bob",
                      resources.getString( "rez", "bob" ) );
    }

    public void testGetStringWith2Args()
        throws Exception
    {
        final Resources resources =
            new Resources( "org.realityforge.salt.i18n.MockResourceBundle",
                           Locale.getDefault(),
                           MockResourceBundle.class.getClassLoader() );
        MockResourceBundle.addResource( "rez",
                                        "a message in a bottle said {0} to {1}" );
        assertEquals( "a message in a bottle said bob to mary",
                      resources.getString( "rez", "bob", "mary" ) );
    }

    public void testGetStringWith3Args()
        throws Exception
    {
        final Resources resources =
            new Resources( "org.realityforge.salt.i18n.MockResourceBundle",
                           Locale.getDefault(),
                           MockResourceBundle.class.getClassLoader() );
        MockResourceBundle.addResource( "rez",
                                        "{0}, {1}, {2}" );
        assertEquals( "bob to mary",
                      resources.getString( "rez", "bob", "mary" ) );
    }
}
