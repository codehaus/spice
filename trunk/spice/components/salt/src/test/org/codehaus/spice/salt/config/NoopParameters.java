/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.salt.config;

class NoopParameters
    implements Parameters
{
    public String[] getParameterNames()
    {
        return new String[ 0 ];
    }

    public boolean isParameter( String name )
    {
        return false;
    }

    public String getParameter( String name )
        throws ParameterException
    {
        return null;
    }

    public String getParameter( String name, String defaultValue )
    {
        return null;
    }

    public int getParameterAsInteger( String name )
        throws ParameterException
    {
        return 0;
    }

    public int getParameterAsInteger( String name, int defaultValue )
    {
        return 0;
    }

    public long getParameterAsLong( String name )
        throws ParameterException
    {
        return 0;
    }

    public long getParameterAsLong( String name, long defaultValue )
    {
        return 0;
    }

    public boolean getParameterAsBoolean( String name )
        throws ParameterException
    {
        return false;
    }

    public boolean getParameterAsBoolean( String name, boolean defaultValue )
    {
        return false;
    }

    public float getParameterAsFloat( String name )
        throws ParameterException
    {
        return 0;
    }

    public float getParameterAsFloat( String name, float defaultValue )
    {
        return 0;
    }

    public Parameters getChildParameters( String prefix )
    {
        return null;
    }
}
