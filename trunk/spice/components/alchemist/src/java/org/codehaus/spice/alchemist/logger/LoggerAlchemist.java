/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.alchemist.logger;

import org.codehaus.dna.Logger;

/**
 * Utility class containing methods to transform Logger objects.
 *
 * @author Mauro Talevi
 * @version $Revision: 1.1 $ $Date: 2004-06-13 13:35:14 $
 */
public class LoggerAlchemist
{
    /**
     * Convert Avalon Logger to DNA Logger
     *
     * @param logger the Avalon Logger
     * @return the DNA Logger
     */
    public static Logger toDNALogger( final org.apache.avalon.framework.logger.Logger logger )
    {
        return new DNALogger( logger );
    }

    /**
     * Convert DNA Logger to Avalon Logger
     *
     * @param logger the DNA  Logger
     * @return the Avalon  Logger
     */
    public static org.apache.avalon.framework.logger.Logger toAvalonLogger( final Logger logger )
    {
        return new AvalonLogger( logger );
    }
}
