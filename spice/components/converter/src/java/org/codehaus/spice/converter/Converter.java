/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.converter;

/**
 * Instances of this interface are used to convert between different types.
 *
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2003-12-02 08:37:56 $
 */
public interface Converter
{
    /**
     * Convert original to destination type.
     * Destination is passed so that one converter can potentiall
     * convert to multiple different types.
     *
     * @param destination the destinaiton type
     * @param original the original type
     * @param context the context in which to convert
     * @return the converted object
     * @exception ConverterException if an error occurs
     */
    Object convert( Class destination, Object original, Object context )
        throws ConverterException;
}
