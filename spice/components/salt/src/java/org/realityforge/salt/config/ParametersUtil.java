/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.salt.config;

import java.util.Iterator;
import java.util.Properties;

/**
 * Class containing utility methods to work with Parameters
 * objects.
 *
 * @version $Revision: 1.1 $ $Date: 2003-10-29 22:38:59 $
 */
public class ParametersUtil
{
    /**
     * Create a Parameters object from aproperties object.
     *
     * @param properties the properties object
     * @return the new Parameters object
     */
    public static Parameters fromProperties( final Properties properties )
    {
        final DefaultParameters parameters = new DefaultParameters();
        final Iterator iterator = properties.keySet().iterator();
        while( iterator.hasNext() )
        {
            final String name = (String)iterator.next();
            final String value = properties.getProperty( name );
            parameters.setParameter( name, value );
        }
        return parameters;
    }

    /**
     * Create a Parameters object that is the result of merging
     * the two parameters objects. If the same key appears in both
     * then the value will be the value in parameters2 parameter.
     *
     * @param parameters1 the first parameters object
     * @param parameters2 the second parameters object
     * @return the new Parameters object
     */
    public static Parameters merge( final Parameters parameters1,
                                    final Parameters parameters2 )
    {
        final DefaultParameters parameters = new DefaultParameters();
        copy( parameters, parameters1 );
        copy( parameters, parameters2 );
        return parameters;
    }

    /**
     * Copy parameters from input into output.
     *
     * @param output the output parameters
     * @param input the input parameters
     */
    static void copy( final DefaultParameters output,
                       final Parameters input )
    {
        final String[] names = input.getParameterNames();
        for( int i = 0; i < names.length; i++ )
        {
            final String name = names[ i ];
            final String value = input.getParameter( name, null );
            output.setParameter( name, value );
        }
    }
}
