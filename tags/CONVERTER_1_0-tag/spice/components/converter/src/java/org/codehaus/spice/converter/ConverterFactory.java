/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.converter;

/**
 * This factory is used to create instances of factory objects.
 * The Factory is used so we can lazily instantiate converters
 * which may be useful if there is a large number of them created
 * for some reason.
 *
 * @author <a href="mailto:adammurdoch at apache.org">Adam Murdoch</a>
 * @version $Revision: 1.1 $ $Date: 2003-12-02 08:37:56 $
 */
public interface ConverterFactory
{
    /**
     * Create an instance of converter.
     *
     * @return the Converter object
     * @throws Exception if unable to create converter
     */
    Converter createConverter()
        throws Exception;
}
