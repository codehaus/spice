/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.salt.i18n;

import junit.framework.TestCase;
import java.util.Locale;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.3 $ $Date: 2003-06-13 00:11:25 $
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
        assertEquals( "GetBoolean", true, resources.getBoolean( "rez" ) );
    }

    public void testGetBooleanWithDefault()
        throws Exception
    {
        final Resources resources =
            new Resources( "org.realityforge.salt.i18n.MockResourceBundle",
                           Locale.getDefault(),
                           MockResourceBundle.class.getClassLoader() );
        assertEquals( "GetBoolean", true, resources.getBoolean( "rez", true ) );
    }

    public void testGetByte()
        throws Exception
    {
        final Resources resources =
            new Resources( "org.realityforge.salt.i18n.MockResourceBundle",
                           Locale.getDefault(),
                           MockResourceBundle.class.getClassLoader() );
        MockResourceBundle.addResource( "rez", "0" );
        assertEquals( "GetByte", 0, resources.getByte( "rez" ) );
    }

    public void testGetByteWithDefault()
        throws Exception
    {
        final Resources resources =
            new Resources( "org.realityforge.salt.i18n.MockResourceBundle",
                           Locale.getDefault(),
                           MockResourceBundle.class.getClassLoader() );
        assertEquals( "GetByte", 2, resources.getByte( "rez", (byte)2 ) );
    }

    public void testGetInteger()
        throws Exception
    {
        final Resources resources =
            new Resources( "org.realityforge.salt.i18n.MockResourceBundle",
                           Locale.getDefault(),
                           MockResourceBundle.class.getClassLoader() );
        MockResourceBundle.addResource( "rez", "0" );
        assertEquals( "GetInteger", 0, resources.getInteger( "rez" ) );
    }

    public void testGetIntegerWithDefault()
        throws Exception
    {
        final Resources resources =
            new Resources( "org.realityforge.salt.i18n.MockResourceBundle",
                           Locale.getDefault(),
                           MockResourceBundle.class.getClassLoader() );
        assertEquals( "GetInteger", 2, resources.getInteger( "rez", (byte)2 ) );
    }

    public void testGetFloat()
        throws Exception
    {
        final Resources resources =
            new Resources( "org.realityforge.salt.i18n.MockResourceBundle",
                           Locale.getDefault(),
                           MockResourceBundle.class.getClassLoader() );
        MockResourceBundle.addResource( "rez", "0" );
        assertEquals( "GetFloat", 0.0f, resources.getFloat( "rez" ), 0.0 );
    }

    public void testGetFloatWithDefault()
        throws Exception
    {
        final Resources resources =
            new Resources( "org.realityforge.salt.i18n.MockResourceBundle",
                           Locale.getDefault(),
                           MockResourceBundle.class.getClassLoader() );
        assertEquals( "GetFloat", 2f, resources.getFloat( "rez", 2f ), 0.0 );
    }

    public void testGetDouble()
        throws Exception
    {
        final Resources resources =
            new Resources( "org.realityforge.salt.i18n.MockResourceBundle",
                           Locale.getDefault(),
                           MockResourceBundle.class.getClassLoader() );
        MockResourceBundle.addResource( "rez", "0" );
        assertEquals( "GetDouble", 0, resources.getDouble( "rez" ), 0.0 );
    }

    public void testGetDoubleWithDefault()
        throws Exception
    {
        final Resources resources =
            new Resources( "org.realityforge.salt.i18n.MockResourceBundle",
                           Locale.getDefault(),
                           MockResourceBundle.class.getClassLoader() );
        assertEquals( "GetDouble", 2.0, resources.getDouble( "rez", 2.0 ), 0.0 );
    }

    public void testGetLong()
        throws Exception
    {
        final Resources resources =
            new Resources( "org.realityforge.salt.i18n.MockResourceBundle",
                           Locale.getDefault(),
                           MockResourceBundle.class.getClassLoader() );
        MockResourceBundle.addResource( "rez", "0" );
        assertEquals( "GetLong", 0, resources.getLong( "rez" ) );
    }

    public void testGetLongWithDefault()
        throws Exception
    {
        final Resources resources =
            new Resources( "org.realityforge.salt.i18n.MockResourceBundle",
                           Locale.getDefault(),
                           MockResourceBundle.class.getClassLoader() );
        assertEquals( "GetLong", 2, resources.getLong( "rez", 2 ) );
    }

    public void testGetShort()
        throws Exception
    {
        final Resources resources =
            new Resources( "org.realityforge.salt.i18n.MockResourceBundle",
                           Locale.getDefault(),
                           MockResourceBundle.class.getClassLoader() );
        MockResourceBundle.addResource( "rez", "0" );
        assertEquals( "GetShort", 0, resources.getShort( "rez" ) );
    }

    public void testGetShortWithDefault()
        throws Exception
    {
        final Resources resources =
            new Resources( "org.realityforge.salt.i18n.MockResourceBundle",
                           Locale.getDefault(),
                           MockResourceBundle.class.getClassLoader() );
        assertEquals( "GetShort", 2, resources.getShort( "rez", (short)2 ) );
    }

    public void testGetChar()
        throws Exception
    {
        final Resources resources =
            new Resources( "org.realityforge.salt.i18n.MockResourceBundle",
                           Locale.getDefault(),
                           MockResourceBundle.class.getClassLoader() );
        MockResourceBundle.addResource( "rez", "0" );
        assertEquals( "GetChar", 0, resources.getChar( "rez" ) );
    }

    public void testGetCharWithDefault()
        throws Exception
    {
        final Resources resources =
            new Resources( "org.realityforge.salt.i18n.MockResourceBundle",
                           Locale.getDefault(),
                           MockResourceBundle.class.getClassLoader() );
        assertEquals( "GetChar", 'a', resources.getChar( "rez", 'a' ) );
    }
 }
