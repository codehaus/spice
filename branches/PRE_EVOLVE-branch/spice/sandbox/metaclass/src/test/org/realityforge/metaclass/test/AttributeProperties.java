/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.test;

import java.util.Properties;

public class AttributeProperties
{
    private Properties _properties;

    public AttributeProperties( final String[][] properties )
    {
        _properties = new Properties();
        for ( int i = 0; i < properties.length; i++ )
        {
            final String[] property = properties[ i ];
            if ( property.length != 2 )
            {
                throw new IllegalArgumentException( "Error initialising AttributeProperties: " +
                                                    "Properties must be defined in " +
                                                    "name=\"value\" pairs." );
            }

            final String propertyName = property[ 0 ];
            final String propertyValue = property[ 1 ];
            _properties.setProperty( propertyName, propertyValue );
        }
    }

    public Properties getProperties()
    {
        return _properties;
    }
}
