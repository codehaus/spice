/*
 * Copyright (C) The Spice Group. All rights reserved. This software is
 * published under the terms of the Spice Software License version 1.1, a copy
 * of which has been included with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.alchemist.instrument;

import org.apache.excalibur.instrument.InstrumentManager;
import org.apache.excalibur.instrument.Instrumentable;
import org.codehaus.dna.Active;
import org.codehaus.dna.Configurable;
import org.codehaus.dna.Configuration;
import org.codehaus.dna.ConfigurationException;
import org.codehaus.dna.LogEnabled;
import org.codehaus.dna.Logger;
import org.codehaus.spice.alchemist.activity.ActivityAlchemist;
import org.codehaus.spice.alchemist.configuration.ConfigurationAlchemist;
import org.codehaus.spice.alchemist.logger.LoggerAlchemist;

/**
 * DNA facade for the Excalibur <code>InstrumentManager</code>.
 * It assumes the InstrumentManager honours the Avalon lifecycle methods
 * and attempts to honour the DNA lifecycle methods via the appropriate
 * Alchemist.
 * 
 * @author Johan Sjoberg
 * @author Mauro Talevi
 * @version $Revision: 1.3 $ $Date: 2004-06-19 17:51:35 $
 */
public class DNAInstrumentManager implements InstrumentManager, LogEnabled,
        Configurable, Active {

    /** The Excalibur InstrumentManager */
    private final InstrumentManager _manager;

    /**
     * Create a DNAInstrumentManager
     * 
     * @param manager the Excalibur InstrumentManager
     */
    public DNAInstrumentManager( final InstrumentManager manager ) {
        if( null == manager )
        {
            throw new NullPointerException( "manager" );
        }
        _manager = manager;
    }

    /**
     * @see LogEnabled#enableLogging(Logger)
     */
    public void enableLogging( Logger logger ) {
        if ( LoggerAlchemist.isAvalonLogEnabled( _manager ) ) {
            LoggerAlchemist.toAvalonLogEnabled( _manager ).enableLogging(
                    LoggerAlchemist.toAvalonLogger( logger ) );
        }
    }

    /**
     * @see Configurable#configure(Configuration)
     */
    public void configure( Configuration configuration )
            throws ConfigurationException {
        try {
            ConfigurationAlchemist.toAvalonConfigurable( _manager )
                    .configure( ConfigurationAlchemist
                            .toAvalonConfiguration( configuration ) );
        } catch ( Exception e ) {
            throw new ConfigurationException( e.getMessage(), e );
        }
    }

    /**
     * @see org.codehaus.dna.Active#initialize()
     */
    public void initialize() throws Exception {
        try {
            ActivityAlchemist.toAvalonInitializable( _manager ).initialize();
        } catch ( IllegalArgumentException e ){
            // _manager is not Avalon Initialisable
        }
    }

    /**
     * @see org.codehaus.dna.Active#dispose()
     */
    public void dispose() {
        try {
            ActivityAlchemist.toAvalonDisposable( _manager ).dispose();
        } catch ( IllegalArgumentException e ){
            // _manager is not Avalon Disposable
        }
    }

    /**
     * @see org.apache.excalibur.instrument.InstrumentManager#registerInstrumentable(org.apache.excalibur.instrument.Instrumentable,
     *      java.lang.String)
     */
    public void registerInstrumentable( Instrumentable arg0, String arg1 )
            throws Exception {
        _manager.registerInstrumentable( arg0, arg1 );
    }
}