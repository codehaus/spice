/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.loggerstore.factories;

import java.io.InputStream;
import java.util.Map;
import org.codehaus.spice.loggerstore.LoggerStore;
import org.codehaus.spice.loggerstore.stores.Log4JLoggerStore;
import org.w3c.dom.Element;

/**
 * DOMLog4JLoggerStoreFactory is an implementation of LoggerStoreFactory for the
 * Log4J Logger using a DOM configuration resource.
 *
 * @author <a href="mailto:mauro.talevi at aquilonia.org">Mauro Talevi</a>
 * @author Peter Donald
 * @version $Revision: 1.2 $ $Date: 2003-12-03 06:32:01 $
 */
public class DOMLog4JLoggerStoreFactory
    extends AbstractLoggerStoreFactory
{
    /**
     * Creates a LoggerStore from a given set of configuration parameters.
     *
     * @param config the Map of parameters for the configuration of the store
     * @return the LoggerStore
     * @throws Exception if unable to create the LoggerStore
     */
    protected LoggerStore doCreateLoggerStore( final Map config )
        throws Exception
    {
        final Element element = (Element)config.get( Element.class.getName() );
        if( null != element )
        {
            return new Log4JLoggerStore( element );
        }

        final InputStream resource = getInputStream( config );
        if( null != resource )
        {
            return new Log4JLoggerStore( resource );
        }
        return missingConfiguration();
    }
}
