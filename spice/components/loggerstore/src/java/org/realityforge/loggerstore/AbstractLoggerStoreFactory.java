/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.loggerstore;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import org.apache.avalon.framework.logger.AbstractLogEnabled;

/**
 * The abstract class that makes it easy to create LoggerStoreFactory
 * implementations.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @author <a href="mailto:mauro.talevi at aquilonia.org">Mauro Talevi</a>
 * @version $Revision: 1.1 $ $Date: 2003-05-24 21:30:52 $
 */
public abstract class AbstractLoggerStoreFactory
    extends AbstractLogEnabled
    implements LoggerStoreFactory
{
    /**
     * Creates a LoggerStore from a given set of configuration parameters.
     * The configuration map contains entrys specific to the
     * concrete implementation.
     *
     * @param config the parameter map to configuration of the store
     * @return the LoggerStore
     * @throws Exception if unable to create the LoggerStore
     */
    public LoggerStore createLoggerStore( final Map config )
        throws Exception
    {
        final LoggerStore loggerStore = doCreateLoggerStore( config );
        setupLogger( loggerStore );
        return loggerStore;
    }

    protected abstract LoggerStore doCreateLoggerStore( Map config ) 
        throws Exception;

    /**
     * Utility method to throw exception indicating input data
     * was invalid.
     *
     * @return never returns
     * @throws Exception indicating input data was invalid
     */
    protected LoggerStore missingConfiguration()
        throws Exception
    {
        throw new Exception( "Invalid configuration" );
    }

    /**
     * A utility method to retrieve a InputStream from input map.
     * It will systematically go through the following steps to
     * attempt to locate the InputStream stopping at success.
     *
     * <ul>
     *   <li>Lookup LoggerStoreFactory.URL_LOCATION for
     *       string defining URL location of input configuration.</li>
     *   <li>Lookup java.net.URL for URL object defining URL location
     *       of input configuration.</li>
     *   <li>Lookup LoggerStoreFactory.FILE_LOCATION for
     *       string defining File location of input configuration.</li>
     *   <li>Lookup java.io.File for File object defining File location
     *       of input configuration.</li>
     *   <li>Lookup java.io.InputStream for InputStream object.</li>
     * </ul>
     *
     * @param config the input map
     * @return the InputStream or null if no stream present
     * @throws Exception if there was a problem aquiring stream
     */
    protected InputStream getInputStream( final Map config )
        throws Exception
    {
        final String urlLocation = (String)config.get( URL_LOCATION );
        URL url = null;
        if( null != urlLocation )
        {
            url = new URL( urlLocation );
        }
        if( null == url )
        {
            url = (URL)config.get( URL.class.getName() );
        }
        if( null != url )
        {
            return url.openStream();
        }

        final String fileLocation = (String)config.get( URL_LOCATION );
        File file = null;
        if( null != fileLocation )
        {
            file = new File( fileLocation );
        }
        if( null == file )
        {
            file = (File)config.get( File.class.getName() );
        }
        if( null != file )
        {
            return new FileInputStream( file );
        }

        return (InputStream)config.get( InputStream.class.getName() );
    }
}
