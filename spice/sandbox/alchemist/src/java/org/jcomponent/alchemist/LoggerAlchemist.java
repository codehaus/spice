/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.alchemist;

import org.jcomponent.alchemist.impl.AvalonLogger;
import org.jcomponent.alchemist.impl.DNALogger;
import org.jcontainer.dna.Logger;

/**
 * Utility class containing methods to transform Logger objects.
 *
 * @author <a href="mailto:mauro.talevi at aquilonia.org">Mauro Talevi</a>
 * @version $Revision: 1.1 $ $Date: 2003-10-25 13:31:44 $
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
