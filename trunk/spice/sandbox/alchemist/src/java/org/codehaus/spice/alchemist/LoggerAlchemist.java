/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.alchemist;

import org.codehaus.spice.alchemist.impl.AvalonLogger;
import org.codehaus.spice.alchemist.impl.DNALogger;
import org.codehaus.dna.Logger;

/**
 * Utility class containing methods to transform Logger objects.
 *
 * @author Mauro Talevi
 * @version $Revision: 1.3 $ $Date: 2004-04-18 22:59:05 $
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
