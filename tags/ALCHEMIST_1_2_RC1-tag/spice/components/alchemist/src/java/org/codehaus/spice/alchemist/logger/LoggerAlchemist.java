/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.alchemist.logger;

import org.codehaus.dna.LogEnabled;
import org.codehaus.dna.Logger;

/**
 * Utility class containing methods to transform Logger objects.
 *
 * @author Mauro Talevi
 * @version $Revision: 1.3 $ $Date: 2004-06-20 12:48:45 $
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
    
    /**
     * Determines if an object is Avalon LogEnabled
     * 
     * @param object the Object to check
     * @return A boolean <code>true</code> if the object is an instance of
     * 		   {@link org.apache.avalon.framework.logger.LogEnabled}
     */
    public static boolean isAvalonLogEnabled( final Object object ){
        if ( object instanceof org.apache.avalon.framework.logger.LogEnabled )
        {
            return true;
        }
        return false;
    }
    
    /**
     * Determines if an object is DNA LogEnabled
     * 
     * @param object the Object to check
     * @return A boolean <code>true</code> if the object is an instance of
     * 		   {@link LogEnabled}
     */
    public static boolean isDNALogEnabled( final Object object ){
        if ( object instanceof LogEnabled )
        {
            return true;
        }
        return false;
    }

    /**
     * Casts an object to Avalon LogEnabled if possible.
     * 
     * @param object the Object to cast
     * @return A {@link org.apache.avalon.framework.logger.LogEnabled}
     * @throws IllegalArgumentException if not Avalon LogEnabled.
     */
    public static org.apache.avalon.framework.logger.LogEnabled toAvalonLogEnabled( final Object object ){
        if ( isAvalonLogEnabled( object ) )
        {
            return (org.apache.avalon.framework.logger.LogEnabled)object; 
        }
        final String message = ( object != null ? object.getClass().getName() : "Object" )
        					 	+ " is not Avalon LogEnabled";
        throw new IllegalArgumentException( message );
    }

    /**
     * Casts an object to DNA LogEnabled if possible.
     * 
     * @param object the Object to cast
     * @return A {@link LogEnabled}
     * @throws IllegalArgumentException if not DNA LogEnabled.
     */
    public static LogEnabled toDNALogEnabled( final Object object ){
        if ( isDNALogEnabled( object ) )
        {
            return (LogEnabled)object; 
        }
        final String message = ( object != null ? object.getClass().getName() : "Object" )
        					 	+ " is not DNA LogEnabled";
        throw new IllegalArgumentException( message );
    }
}
