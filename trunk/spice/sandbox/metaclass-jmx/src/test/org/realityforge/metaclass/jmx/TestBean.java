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
 * @version $Revision: 1.4 $ $Date: 2003-10-15 02:09:07 $
 */
public class TestBean
    implements TestMxInterface
{
    private int m_value;
    private int m_otherValue;

    public TestBean()
    {
    }

    public TestBean( int value )
    {
        m_value = value;
    }

    public int getValue()
    {
        return m_value;
    }

    public void setValue( int value )
    {
        m_value = value;
    }

    public int getOtherValue()
    {
        return m_otherValue;
    }

    public void setOtherValue( int otherValue )
    {
        m_otherValue = otherValue;
    }
}