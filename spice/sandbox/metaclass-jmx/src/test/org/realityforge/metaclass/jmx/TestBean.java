/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.jmx;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-10-14 00:42:13 $
 */
public class TestBean
{
    private int m_value;

    public int getValue()
    {
        return m_value;
    }

    public void setValue( int value )
    {
        m_value = value;
    }
}
