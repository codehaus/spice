/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 * Portions of this software are based upon software originally
 * developed as part of the Apache Myrmidon project under
 * the Apache 1.1 License.
 */
package org.realityforge.converter.lib;

import org.realityforge.converter.Converter;
import org.realityforge.converter.ConverterFactory;

/**
 * A ConverterFactory that creates converter instances using reflection.
 *
 * @author <a href="mailto:adammurdoch@apache.org">Adam Murdoch</a>
 * @version $Revision: 1.1 $ $Date: 2003-01-29 00:14:32 $
 */
public class SimpleConverterFactory
    implements ConverterFactory
{
    private Class m_converterClass;

    public SimpleConverterFactory( final Class converterClass )
    {
        m_converterClass = converterClass;
    }

    /**
     * Creates an instance of a converter.
     */
    public Converter createConverter() throws Exception
    {
        return (Converter)m_converterClass.newInstance();
    }
}
