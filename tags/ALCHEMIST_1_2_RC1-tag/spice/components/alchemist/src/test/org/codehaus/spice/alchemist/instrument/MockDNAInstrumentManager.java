/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
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


class MockDNAInstrumentManager
    implements InstrumentManager, LogEnabled, Configurable, Active
{
    
    private String methodCalled;
    
    /* (non-Javadoc)
     * @see org.apache.excalibur.instrument.InstrumentManager#registerInstrumentable(org.apache.excalibur.instrument.Instrumentable, java.lang.String)
     */
    public void registerInstrumentable( Instrumentable arg0, String arg1 ) throws Exception {
        methodCalled = "registerInstrumentable";
    }

    /* (non-Javadoc)
     * @see org.codehaus.dna.LogEnabled#enableLogging(org.codehaus.dna.Logger)
     */
    public void enableLogging( Logger arg0 ) {
        methodCalled = "enableLogging";
    }

    /* (non-Javadoc)
     * @see org.codehaus.dna.Configurable#configure(org.codehaus.dna.Configuration)
     */
    public void configure( Configuration arg0 ) throws ConfigurationException {
        methodCalled = "configure";
    }

    /* (non-Javadoc)
     * @see org.codehaus.dna.Disposable#dispose()
     */
    public void dispose() {
        methodCalled = "dispose";
    }

    /* (non-Javadoc)
     * @see org.codehaus.dna.Initializable#initialize()
     */
    public void initialize() throws Exception {
        methodCalled = "initialize";
    }
    
    public String getMethodCalled(){
        return methodCalled;
    }    

}
