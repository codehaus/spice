/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.alchemist.configuration;

import org.codehaus.dna.Configuration;

/**
 * Utility class containing methods to transform Configuration objects.
 *
 * @author Mauro Talevi
 * @version $Revision: 1.1 $ $Date: 2004-06-13 13:35:14 $
 */
public class ConfigurationAlchemist
{
    /**
     * Convert Avalon Configuration to DNA Configuration
     *
     * @param configuration the Avalon Configuration
     * @return the DNA Configuration
     */
    public static Configuration toDNAConfiguration( final org.apache.avalon.framework.configuration.Configuration configuration )
    {
        return new DNAConfiguration( configuration );
    }

    /**
     * Convert DNA Configuration to Avalon Configuration
     *
     * @param configuration the DNA  Configuration
     * @return the Avalon Configuration
     */
    public static org.apache.avalon.framework.configuration.Configuration toAvalonConfiguration( final Configuration configuration )
    {
        return new AvalonConfiguration( configuration );
    }
}
