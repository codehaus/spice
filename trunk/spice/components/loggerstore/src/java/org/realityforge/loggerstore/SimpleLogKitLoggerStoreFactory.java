/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.loggerstore;

import java.io.File;
import java.util.Map;

import org.apache.avalon.excalibur.logger.LoggerManager;
import org.apache.avalon.excalibur.logger.SimpleLogKitManager;
import org.apache.avalon.framework.context.Context;
import org.apache.avalon.framework.context.ContextException;
import org.apache.avalon.framework.context.DefaultContext;

/**
 * SimpleLogKitLoggerStoreFactory specialises the LogKitLoggerStoreFactory
 * to use the SimpleLogKitManager.
 *
 * @author <a href="mailto:mauro.talevi at aquilonia.org">Mauro Talevi</a>
 */
public class SimpleLogKitLoggerStoreFactory
    extends LogKitLoggerStoreFactory
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
        final LoggerManager loggerManager = new SimpleLogKitManager();
        config.put( LoggerManager.class.getName(), loggerManager );
        
        Context context = (Context)config.get( Context.class.getName() );
        if ( context == null ) {
            context = new DefaultContext();
        }
		try
		{
			File file = (File) context.get("app.home");
		} catch (ContextException e)
		{
			if ( context instanceof DefaultContext )
			{
				final File file = new File("./");
				((DefaultContext) context).put("app.home", file);
			}
		}
        config.put( Context.class.getName(), context );
        return super.doCreateLoggerStore( config );            
    }
}
