/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.alchemist.instrument;

import org.apache.avalon.framework.activity.Disposable;
import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.logger.LogEnabled;
import org.apache.avalon.framework.logger.Logger;
import org.apache.excalibur.instrument.InstrumentManager;
import org.apache.excalibur.instrument.Instrumentable;

class MockAvalonInstrumentManager
    implements InstrumentManager, LogEnabled, Configurable, Initializable, Disposable
{
    
    private String methodCalled;
    
    /* (non-Javadoc)
     * @see org.apache.excalibur.instrument.InstrumentManager#registerInstrumentable(org.apache.excalibur.instrument.Instrumentable, java.lang.String)
     */
    public void registerInstrumentable( Instrumentable arg0, String arg1 ) throws Exception {
        methodCalled = "registerInstrumentable";
    }

    /* (non-Javadoc)
     * @see org.apache.avalon.framework.logger.LogEnabled#enableLogging(org.apache.avalon.framework.logger.Logger)
     */
    public void enableLogging( Logger arg0 ) {
        methodCalled = "enableLogging";
    }

    /* (non-Javadoc)
     * @see org.apache.avalon.framework.configuration.Configurable#configure(org.apache.avalon.framework.configuration.Configuration)
     */
    public void configure( Configuration arg0 ) throws ConfigurationException {
        methodCalled = "configure";
    }

    /* (non-Javadoc)
     * @see org.apache.avalon.framework.activity.Disposable#dispose()
     */
    public void dispose() {
        methodCalled = "dispose";
    }

    /* (non-Javadoc)
     * @see org.apache.avalon.framework.activity.Initializable#initialize()
     */
    public void initialize() throws Exception {
        methodCalled = "initialize";
    }
    
    public String getMethodCalled(){
        return methodCalled;
    }    

}
