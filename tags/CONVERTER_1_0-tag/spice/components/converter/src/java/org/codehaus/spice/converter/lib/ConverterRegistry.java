/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.converter.lib;

import org.codehaus.spice.converter.ConverterFactory;

/**
 * The work interface for a registry of converters. This gives the
 * client the ability to register and deregister converters as well as
 * listing converters currently registered.
 *
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2003-12-02 08:37:56 $
 */
public interface ConverterRegistry
{
    /**
     * Registers a converter.
     *
     * @param source the source classname
     * @param destination the destination classname
     * @param factory the factory to use to create a converter instance.
     */
    void registerConverter( String source,
                            String destination,
                            ConverterFactory factory );

    /**
     * Deregisters a converter.  If no converter is
     * registered then method will silently return.
     *
     * @param source the source classname
     * @param destination the destination classname
     */
    //void deregisterConverter( String source, String destination );

    /**
     * Deregisters a converter.  If no converter is
     * registered then method will silently return.
     *
     * @param source the source classname
     * @param destination the destination classname
     * @return true if converter is registered, false otherwise
     */
    //boolean isConverterRegistered( String source, String destination );
}
