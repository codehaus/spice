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
package org.realityforge.converter;

/**
 * A factory used to create converter instances.
 *
 * @author <a href="mailto:adammurdoch@apache.org">Adam Murdoch</a>
 * @version $Revision: 1.1 $ $Date: 2003-01-29 00:14:36 $
 */
public interface ConverterFactory
{
    /**
     * Creates an instance of a converter.
     */
    Converter createConverter() throws Exception;
}
