/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.alchemist.logger;

import org.codehaus.dna.LogEnabled;
import org.codehaus.dna.Logger;



/**
 * @author Mauro Talevi
 */
public class MockDNALogEnabled implements LogEnabled {

    /**
     * @see org.codehaus.dna.LogEnabled#enableLogging(org.codehaus.dna.Logger)
     */
    public void enableLogging( Logger arg0 ) {
        // no-op
    }
}
