/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.converter.lib;

import org.realityforge.converter.AbstractMasterConverter;
import org.realityforge.converter.ConverterFactory;

/**
 * A very simple master converter that implements the ConverterRegistry
 * interface. Converters can be registered in this class via the
 * {@link ConverterRegistry} interface and will subsequently used
 * during conversion process.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.4 $ $Date: 2003-03-25 02:51:26 $
 * @avalon.service type="ConverterRegistry"
 */
public class DefaultMasterConverter
    extends AbstractMasterConverter
    implements ConverterRegistry
{
    /**
     * Registers a converter.
     *
     * @param source the source classname
     * @param destination the destination classname
     * @param factory the factory to use to create a converter instance.
     */
    public void registerConverter( final String source,
                                   final String destination,
                                   final ConverterFactory factory )
    {
        super.registerConverter( factory, source, destination );
    }
}
