/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.converter.lib;

import org.realityforge.converter.ConverterFactory;
import org.realityforge.converter.Converter;

/**
 * A converter factory that just returns value supplied in ctor.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-04-04 11:26:44 $
 */
public class SingletonConverterFactory
    implements ConverterFactory
{
    /**
     * The converter managed by factory.
     */
    private Converter m_converter;

    /**
     * Create factory with specified converter as singleton returned.
     *
     * @param converter the converter
     */
    public SingletonConverterFactory( final Converter converter )
    {
        if( null == converter )
        {
            throw new NullPointerException( "converter" );
        }
        m_converter = converter;
    }

    /**
     * Return the singleton converter.
     *
     * @return the singleton converter.
     */
    public Converter createConverter()
    {
        return m_converter;
    }
}
