/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.salt.i18n;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import junit.framework.TestCase;

/**
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2003-12-02 02:15:05 $
 */
public class ResourcesTestCase
    extends TestCase
{
    public ResourcesTestCase( final String name )
    {
        super( name );
    }

    protected void setUp()
        throws Exception
    {
        MockResourceBundle.cleanResourceSet();
    }

    public void testGetBoolean()
        throws Exception
    {
        final Resources resources =
            new Resources( "org.codehaus.spice.salt.i18n.MockResourceBundle",
                           Locale.getDefault(),
                           MockResourceBundle.class.getClassLoader() );
        MockResourceBundle.addResource( "rez", "true" );
        assertEquals( true, resources.getBoolean( "rez" ) );
    }

    public void testGetBooleanWithDefault()
        throws Exception
    {
        final Resources resources =
            new Resources( "org.codehaus.spice.salt.i18n.MockResourceBundle",
                           Locale.getDefault(),
                           MockResourceBundle.class.getClassLoader() );
        assertEquals( true, resources.getBoolean( "rez", true ) );
    }

    public void testGetByte()
        throws Exception
    {
        final Resources resources =
            new Resources( "org.codehaus.spice.salt.i18n.MockResourceBundle",
                           Locale.getDefault(),
                           MockResourceBundle.class.getClassLoader() );
        MockResourceBundle.addResource( "rez", "0" );
        assertEquals( 0, resources.getByte( "rez" ) );
    }

    public void testGetBadlyFormattedByte()
        throws Exception
    {
        final Resources resources =
            new Resources( "org.codehaus.spice.salt.i18n.MockResourceBundle",
                           Locale.getDefault(),
                           MockResourceBundle.class.getClassLoader() );
        MockResourceBundle.addResource( "rez", "f" );
        assertEquals( 2, resources.getByte( "rez", (byte)2 ) );
    }

    public void testGetByteWithDefault()
        throws Exception
    {
        final Resources resources =
            new Resources( "org.codehaus.spice.salt.i18n.MockResourceBundle",
                           Locale.getDefault(),
                           MockResourceBundle.class.getClassLoader() );
        assertEquals( 2, resources.getByte( "rez", (byte)2 ) );
    }

    public void testGetInteger()
        throws Exception
    {
        final Resources resources =
            new Resources( "org.codehaus.spice.salt.i18n.MockResourceBundle",
                           Locale.getDefault(),
                           MockResourceBundle.class.getClassLoader() );
        MockResourceBundle.addResource( "rez", "0" );
        assertEquals( 0, resources.getInteger( "rez" ) );
    }

    public void testGetBadlyFormattedInteger()
        throws Exception
    {
        final Resources resources =
            new Resources( "org.codehaus.spice.salt.i18n.MockResourceBundle",
                           Locale.getDefault(),
                           MockResourceBundle.class.getClassLoader() );
        MockResourceBundle.addResource( "rez", "f" );
        assertEquals( 0, resources.getInteger( "rez", 0 ) );
    }

    public void testGetIntegerWithDefault()
        throws Exception
    {
        final Resources resources =
            new Resources( "org.codehaus.spice.salt.i18n.MockResourceBundle",
                           Locale.getDefault(),
                           MockResourceBundle.class.getClassLoader() );
        assertEquals( 2, resources.getInteger( "rez", (byte)2 ) );
    }

    public void testGetFloat()
        throws Exception
    {
        final Resources resources =
            new Resources( "org.codehaus.spice.salt.i18n.MockResourceBundle",
                           Locale.getDefault(),
                           MockResourceBundle.class.getClassLoader() );
        MockResourceBundle.addResource( "rez", "0" );
        assertEquals( 0.0f, resources.getFloat( "rez" ), 0.0 );
    }

    public void testGetBadlyFormattedFloat()
        throws Exception
    {
        final Resources resources =
            new Resources( "org.codehaus.spice.salt.i18n.MockResourceBundle",
                           Locale.getDefault(),
                           MockResourceBundle.class.getClassLoader() );
        MockResourceBundle.addResource( "rez", "f" );
        assertEquals( 0.0f, resources.getFloat( "rez", 0.0f ), 0.0 );
    }

    public void testGetFloatWithDefault()
        throws Exception
    {
        final Resources resources =
            new Resources( "org.codehaus.spice.salt.i18n.MockResourceBundle",
                           Locale.getDefault(),
                           MockResourceBundle.class.getClassLoader() );
        assertEquals( 2f, resources.getFloat( "rez", 2f ), 0.0 );
    }

    public void testGetDouble()
        throws Exception
    {
        final Resources resources =
            new Resources( "org.codehaus.spice.salt.i18n.MockResourceBundle",
                           Locale.getDefault(),
                           MockResourceBundle.class.getClassLoader() );
        MockResourceBundle.addResource( "rez", "0" );
        assertEquals( 0, resources.getDouble( "rez" ), 0.0 );
    }

    public void testGetBadlyFormattedDouble()
        throws Exception
    {
        final Resources resources =
            new Resources( "org.codehaus.spice.salt.i18n.MockResourceBundle",
                           Locale.getDefault(),
                           MockResourceBundle.class.getClassLoader() );
        MockResourceBundle.addResource( "rez", "f" );
        assertEquals( 0, resources.getDouble( "rez", 0.0 ), 0.0 );
    }

    public void testGetDoubleWithDefault()
        throws Exception
    {
        final Resources resources =
            new Resources( "org.codehaus.spice.salt.i18n.MockResourceBundle",
                           Locale.getDefault(),
                           MockResourceBundle.class.getClassLoader() );
        assertEquals( 2.0, resources.getDouble( "rez", 2.0 ), 0.0 );
    }

    public void testGetLong()
        throws Exception
    {
        final Resources resources =
            new Resources( "org.codehaus.spice.salt.i18n.MockResourceBundle",
                           Locale.getDefault(),
                           MockResourceBundle.class.getClassLoader() );
        MockResourceBundle.addResource( "rez", "0" );
        assertEquals( 0, resources.getLong( "rez" ) );
    }

    public void testGetBadlyFormattedLong()
        throws Exception
    {
        final Resources resources =
            new Resources( "org.codehaus.spice.salt.i18n.MockResourceBundle",
                           Locale.getDefault(),
                           MockResourceBundle.class.getClassLoader() );
        MockResourceBundle.addResource( "rez", "f" );
        assertEquals( 0, resources.getLong( "rez", 0 ) );
    }

    public void testGetLongWithDefault()
        throws Exception
    {
        final Resources resources =
            new Resources( "org.codehaus.spice.salt.i18n.MockResourceBundle",
                           Locale.getDefault(),
                           MockResourceBundle.class.getClassLoader() );
        assertEquals( 2, resources.getLong( "rez", 2 ) );
    }

    public void testGetShort()
        throws Exception
    {
        final Resources resources =
            new Resources( "org.codehaus.spice.salt.i18n.MockResourceBundle",
                           Locale.getDefault(),
                           MockResourceBundle.class.getClassLoader() );
        MockResourceBundle.addResource( "rez", "0" );
        assertEquals( 0, resources.getShort( "rez" ) );
    }

    public void testGetBadlyFormattedShort()
        throws Exception
    {
        final Resources resources =
            new Resources( "org.codehaus.spice.salt.i18n.MockResourceBundle",
                           Locale.getDefault(),
                           MockResourceBundle.class.getClassLoader() );
        MockResourceBundle.addResource( "rez", "f" );
        assertEquals( 0, resources.getShort( "rez", (short)0 ) );
    }

    public void testGetShortWithDefault()
        throws Exception
    {
        final Resources resources =
            new Resources( "org.codehaus.spice.salt.i18n.MockResourceBundle",
                           Locale.getDefault(),
                           MockResourceBundle.class.getClassLoader() );
        assertEquals( 2, resources.getShort( "rez", (short)2 ) );
    }

    public void testGetChar()
        throws Exception
    {
        final Resources resources =
            new Resources( "org.codehaus.spice.salt.i18n.MockResourceBundle",
                           Locale.getDefault(),
                           MockResourceBundle.class.getClassLoader() );
        MockResourceBundle.addResource( "rez", "a" );
        assertEquals( 'a', resources.getChar( "rez" ) );
    }

    public void testGetBadlyFormattedChar()
        throws Exception
    {
        final Resources resources =
            new Resources( "org.codehaus.spice.salt.i18n.MockResourceBundle",
                           Locale.getDefault(),
                           MockResourceBundle.class.getClassLoader() );
        MockResourceBundle.addResource( "rez", "qwerqweq" );
        assertEquals( 'a', resources.getChar( "rez", 'a' ) );
    }

    public void testGetCharWithDefault()
        throws Exception
    {
        final Resources resources =
            new Resources( "org.codehaus.spice.salt.i18n.MockResourceBundle",
                           Locale.getDefault(),
                           MockResourceBundle.class.getClassLoader() );
        assertEquals( 'a', resources.getChar( "rez", 'a' ) );
    }

    public void testGetDate()
        throws Exception
    {
        final Resources resources =
            new Resources( "org.codehaus.spice.salt.i18n.MockResourceBundle",
                           Locale.getDefault(),
                           MockResourceBundle.class.getClassLoader() );
        final DateFormat format =
            DateFormat.getDateInstance( DateFormat.DEFAULT,
                                        Locale.getDefault() );
        final String current = format.format( new Date() );
        MockResourceBundle.addResource( "rez", current );
        final Date expected = format.parse( current );
        assertEquals( expected, resources.getDate( "rez" ) );
    }

    public void testGetBadlyFormattedDate()
        throws Exception
    {
        final Resources resources =
            new Resources( "org.codehaus.spice.salt.i18n.MockResourceBundle",
                           Locale.getDefault(),
                           MockResourceBundle.class.getClassLoader() );
        MockResourceBundle.addResource( "rez", "ss" );
        final Date expected = new Date();
        assertEquals( expected, resources.getDate( "rez", expected ) );
    }

    public void testGetDateWithDefault()
        throws Exception
    {
        final Resources resources =
            new Resources( "org.codehaus.spice.salt.i18n.MockResourceBundle",
                           Locale.getDefault(),
                           MockResourceBundle.class.getClassLoader() );
        final Date date = new Date();
        assertEquals( date, resources.getDate( "rez", date ) );
    }

    public void testGetTime()
        throws Exception
    {
        final Resources resources =
            new Resources( "org.codehaus.spice.salt.i18n.MockResourceBundle",
                           Locale.getDefault(),
                           MockResourceBundle.class.getClassLoader() );
        final Date date = new Date();
        final DateFormat format =
            DateFormat.getTimeInstance( DateFormat.DEFAULT,
                                        Locale.getDefault() );
        final String current = format.format( date );
        final Date expected = format.parse( current );
        MockResourceBundle.addResource( "rez", current );
        assertEquals( expected, resources.getTime( "rez" ) );
    }

    public void testGetBadlyFormattedTime()
        throws Exception
    {
        final Resources resources =
            new Resources( "org.codehaus.spice.salt.i18n.MockResourceBundle",
                           Locale.getDefault(),
                           MockResourceBundle.class.getClassLoader() );
        final Date date = new Date();
        MockResourceBundle.addResource( "rez", "ss" );
        assertEquals( date, resources.getTime( "rez", date ) );
    }

    public void testGetTimeWithDefault()
        throws Exception
    {
        final Resources resources =
            new Resources( "org.codehaus.spice.salt.i18n.MockResourceBundle",
                           Locale.getDefault(),
                           MockResourceBundle.class.getClassLoader() );
        final Date date = new Date();
        assertEquals( date, resources.getTime( "rez", date ) );
    }

    public void testGetDateTime()
        throws Exception
    {
        final Resources resources =
            new Resources( "org.codehaus.spice.salt.i18n.MockResourceBundle",
                           Locale.getDefault(),
                           MockResourceBundle.class.getClassLoader() );
        final Date date = new Date();
        final DateFormat format =
            DateFormat.getDateTimeInstance( DateFormat.DEFAULT,
                                            DateFormat.DEFAULT,
                                            Locale.getDefault() );
        final String current = format.format( date );
        final Date expected = format.parse( current );
        MockResourceBundle.addResource( "rez", current );
        assertEquals( expected, resources.getDateTime( "rez" ) );
    }

    public void testGetBadlyFormattedDateTime()
        throws Exception
    {
        final Resources resources =
            new Resources( "org.codehaus.spice.salt.i18n.MockResourceBundle",
                           Locale.getDefault(),
                           MockResourceBundle.class.getClassLoader() );
        final Date date = new Date();
        MockResourceBundle.addResource( "rez", "ss" );
        assertEquals( date, resources.getDateTime( "rez", date ) );
    }

    public void testGetDateTimeWithDefault()
        throws Exception
    {
        final Resources resources =
            new Resources( "org.codehaus.spice.salt.i18n.MockResourceBundle",
                           Locale.getDefault(),
                           MockResourceBundle.class.getClassLoader() );
        final Date date = new Date();
        assertEquals( date, resources.getDateTime( "rez", date ) );
    }

    public void testGetString()
        throws Exception
    {
        final Resources resources =
            new Resources( "org.codehaus.spice.salt.i18n.MockResourceBundle",
                           Locale.getDefault(),
                           MockResourceBundle.class.getClassLoader() );
        MockResourceBundle.addResource( "rez", "a message in a bottle" );
        assertEquals( "a message in a bottle", resources.getString( "rez" ) );
    }

    public void testStringWithDefault()
        throws Exception
    {
        final Resources resources =
            new Resources( "org.codehaus.spice.salt.i18n.MockResourceBundle",
                           Locale.getDefault(),
                           MockResourceBundle.class.getClassLoader() );
        assertEquals( "boo", resources.getString( "rez", "boo" ) );
    }

    public void testFormatStringWithOneArg()
        throws Exception
    {
        final Resources resources =
            new Resources( "org.codehaus.spice.salt.i18n.MockResourceBundle",
                           Locale.getDefault(),
                           MockResourceBundle.class.getClassLoader() );
        MockResourceBundle.addResource( "rez",
                                        "a message in a bottle said {0}" );
        assertEquals( "a message in a bottle said bob",
                      resources.format( "rez", "bob" ) );
    }

    public void testFormatStringWith2Args()
        throws Exception
    {
        final Resources resources =
            new Resources( "org.codehaus.spice.salt.i18n.MockResourceBundle",
                           Locale.getDefault(),
                           MockResourceBundle.class.getClassLoader() );
        MockResourceBundle.addResource( "rez",
                                        "a message in a bottle said {0} to {1}" );
        assertEquals( "a message in a bottle said bob to mary",
                      resources.format( "rez", "bob", "mary" ) );
    }

    public void testFormatStringWith3Args()
        throws Exception
    {
        final Resources resources =
            new Resources( "org.codehaus.spice.salt.i18n.MockResourceBundle",
                           Locale.getDefault(),
                           MockResourceBundle.class.getClassLoader() );
        MockResourceBundle.addResource( "rez",
                                        "{0}, {1}, {2}" );
        assertEquals( "bob, mary, jenny",
                      resources.format( "rez", "bob", "mary", "jenny" ) );
    }

    public void testFormatStringWith4Args()
        throws Exception
    {
        final Resources resources =
            new Resources( "org.codehaus.spice.salt.i18n.MockResourceBundle",
                           Locale.getDefault(),
                           MockResourceBundle.class.getClassLoader() );
        MockResourceBundle.addResource( "rez",
                                        "{0}, {1}, {2}, {3}" );
        assertEquals( "bob, mary, jenny, peter",
                      resources.format( "rez",
                                        "bob",
                                        "mary",
                                        "jenny",
                                        "peter" ) );
    }

    public void testFormatStringWith5Args()
        throws Exception
    {
        final Resources resources =
            new Resources( "org.codehaus.spice.salt.i18n.MockResourceBundle",
                           Locale.getDefault(),
                           MockResourceBundle.class.getClassLoader() );
        MockResourceBundle.addResource( "rez",
                                        "{0}, {1}, {2}, {3}, {4}" );
        assertEquals( "bob, mary, jenny, peter, heather",
                      resources.format( "rez",
                                        "bob",
                                        "mary",
                                        "jenny",
                                        "peter",
                                        "heather" ) );
    }

    public void testFormatStringWithMultiArgs()
        throws Exception
    {
        final Resources resources =
            new Resources( "org.codehaus.spice.salt.i18n.MockResourceBundle",
                           Locale.getDefault(),
                           MockResourceBundle.class.getClassLoader() );
        MockResourceBundle.addResource( "rez",
                                        "{0}, {1}, {2}, {3}, {4}" );
        final Object[] args = new Object[]{"bob",
                                           "mary",
                                           "jenny",
                                           "peter",
                                           "heather"};
        assertEquals( "bob, mary, jenny, peter, heather",
                      resources.format( "rez", args ) );
    }

    public void testGetNonExistent()
        throws Exception
    {
        final Resources resources =
            new Resources( "org.codehaus.spice.salt.i18n.MockResourceBundle",
                           Locale.getDefault(),
                           MockResourceBundle.class.getClassLoader() );
        final String string = resources.format( "noExist", "blah" );
        assertTrue( "Non existent resource starts with 'Unknown resource'",
                    string.startsWith( "Unknown resource" ) );
    }

    public void testGetNonExistentWithMultipleArgs()
        throws Exception
    {
        final Resources resources =
            new Resources( "org.codehaus.spice.salt.i18n.MockResourceBundle",
                           Locale.getDefault(),
                           MockResourceBundle.class.getClassLoader() );
        final String string = resources.format( "noExist", "blah", "blee" );
        assertTrue( "Non existent resource starts with 'Unknown resource'",
                    string.startsWith( "Unknown resource" ) );
    }

    public void testAlternateConstructor()
        throws Exception
    {
        final Resources resources =
            new Resources( "org.codehaus.spice.salt.i18n.MockResourceBundle",
                           MockResourceBundle.class.getClassLoader() );
        MockResourceBundle.addResource( "rez", "a message in a bottle" );
        assertEquals( "a message in a bottle", resources.getString( "rez" ) );
    }

    public void testAccessingCachedBundle()
        throws Exception
    {
        final Resources resources =
            new Resources( "org.codehaus.spice.salt.i18n.MockResourceBundle",
                           MockResourceBundle.class.getClassLoader() );
        final ResourceBundle bundle1 = resources.getBundle();
        assertNotNull( "getBundle v1", bundle1 );
        final ResourceBundle bundle2 = resources.getBundle();
        assertNotNull( "getBundle v2", bundle2 );
        assertEquals( "v1 == v2", bundle1, bundle2 );
    }

    public void testPassingNullBasenameIntoCtor()
        throws Exception
    {
        try
        {
            new Resources( null,
                           Locale.getDefault(),
                           MockResourceBundle.class.getClassLoader() );
            fail( "Expected to fail due to passing null into ctor" );
        }
        catch( NullPointerException npe )
        {
            assertEquals( "Null value", "baseName", npe.getMessage() );
        }
    }

    public void testPassingNullLocaleIntoCtor()
        throws Exception
    {
        try
        {
            new Resources( "org.codehaus.spice.salt.i18n.MockResourceBundle",
                           null,
                           MockResourceBundle.class.getClassLoader() );
            fail( "Expected to fail due to passing null into ctor" );
        }
        catch( NullPointerException npe )
        {
            assertEquals( "Null value", "locale", npe.getMessage() );
        }
    }

    public void testPassingNullClassLoaderIntoCtor()
        throws Exception
    {
        try
        {
            new Resources( "org.codehaus.spice.salt.i18n.MockResourceBundle",
                           Locale.getDefault(),
                           null );
            fail( "Expected to fail due to passing null into ctor" );
        }
        catch( NullPointerException npe )
        {
            assertEquals( "Null value", "classLoader", npe.getMessage() );
        }
    }
}
