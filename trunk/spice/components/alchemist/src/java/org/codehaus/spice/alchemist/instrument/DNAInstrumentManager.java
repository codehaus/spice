/*
 * Copyright (C) The Loom Group. All rights reserved.
 *
 * This software is published under the terms of the Loom
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.alchemist.instrument;

import org.apache.excalibur.instrument.InstrumentManager;
import org.apache.excalibur.instrument.Instrumentable;
import org.apache.excalibur.instrument.manager.DefaultInstrumentManager;
import org.codehaus.dna.Active;
import org.codehaus.dna.Configurable;
import org.codehaus.dna.Configuration;
import org.codehaus.dna.ConfigurationException;
import org.codehaus.dna.LogEnabled;
import org.codehaus.dna.Logger;
import org.codehaus.spice.alchemist.configuration.ConfigurationAlchemist;
import org.codehaus.spice.alchemist.logger.LoggerAlchemist;


/**
 * DNA facade for the Excalibur <code>DefaultInstrumentManager</code>
 *
 * @author Johan Sjoberg
 * @author Mauro Talevi
 * @version $Revision: 1.1 $ $Date: 2004-06-13 13:35:14 $
 */
public class DNAInstrumentManager 
    implements InstrumentManager, LogEnabled, Configurable, Active
{

    /** The Excalibur DefaultInstrumentManager */
    private final DefaultInstrumentManager _manager;
    
    /**
     * Create a DNAInstrumentManager
     *
     * @param manager the Excalibur DefaultInstrumentManager
     */
    public DNAInstrumentManager( final DefaultInstrumentManager manager )
    {
        _manager = manager;
    }

    /**
     * @see LogEnabled#enableLogging(Logger)
     */
     public void enableLogging( Logger logger )
     {
         _manager.enableLogging( LoggerAlchemist.toAvalonLogger( logger ) );
     }


    /**
     * @see Configurable#configure(Configuration)
     */
    public void configure( Configuration configuration ) throws ConfigurationException
    {
        try
        {
            _manager.configure( ConfigurationAlchemist.toAvalonConfiguration( configuration ) );
        }
        catch( Exception e )
        {
            throw new ConfigurationException( e.getMessage(), e );
        }
    }


    /**
     * @see org.codehaus.dna.Active#initialize()
     */
    public void initialize() throws Exception
    {
        _manager.initialize();
    }


    /**
     * @see org.codehaus.dna.Active#dispose()
     */
    public void dispose()
    {
        _manager.dispose();
    }


    /**
     * @see org.apache.excalibur.instrument.InstrumentManager#registerInstrumentable(org.apache.excalibur.instrument.Instrumentable, java.lang.String)
     */
    public void registerInstrumentable( Instrumentable arg0, String arg1 ) throws Exception {
        _manager.registerInstrumentable( arg0, arg1 );
    }
}
