/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.loggerstore;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

/**
 * Jdk14LoggerStoreFactory is an implementation of LoggerStoreFactory
 * for the JDK14 Logger.
 *
 * @author <a href="mailto:mauro.talevi at aquilonia.org">Mauro Talevi</a>
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.4 $ $Date: 2003-05-24 22:29:25 $
 */
public class Jdk14LoggerStoreFactory
    extends AbstractLoggerStoreFactory
{
    /**
     * Creates a LoggerStore from a given set of configuration parameters.
     * The configuration Map must contain:
     * <ol>
     * <li> <code>InputStream</code> object keyed on <code>LoggerStoreFactory.CONFIGURATION</code>
     * encoding the configuration resource</li>
     * </ol>
     *
     * @param config the Map of parameters for the configuration of the store
     * @return the LoggerStore
     * @throws Exception if unable to create the LoggerStore
     */
    protected LoggerStore doCreateLoggerStore( final Map config )
        throws Exception
    {
        final Properties properties = (Properties)config.get( Properties.class.getName() );
        if( null != properties )
        {
            final ByteArrayOutputStream output = new ByteArrayOutputStream();
            properties.store( output, "" );
            final ByteArrayInputStream input = new ByteArrayInputStream( output.toByteArray() );
            return new Jdk14LoggerStore( input );
        }
        final InputStream resource = getInputStream( config );
        if( null != resource )
        {
            return new Jdk14LoggerStore( resource );
        }
        return missingConfiguration();
    }
}
