/*
 * Copyright (C) The Spice Group. All rights reserved. This software is
 * published under the terms of the Spice Software License version 1.1, a copy
 * of which has been included with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.alchemist.instrument;

import org.apache.excalibur.instrument.InstrumentManager;
import org.apache.excalibur.instrument.manager.DefaultInstrumentManager;

/**
 * Utility class containing methods to transform Instrument objects.
 * 
 * @author Mauro Talevi
 * @version $Revision: 1.1 $ $Date: 2004-06-13 13:35:13 $
 */
public class InstrumentAlchemist
{
    /**
     * Convert Excalibur DefaultInstrumentManager to use DNA lifecycle methods
     * 
     * @param manager the Excalibur DefaultInstrumentManager
     * @return An InstrumentManager object
     */
    public static InstrumentManager toDNAInstrumentManager( final DefaultInstrumentManager manager )
    {
        return new DNAInstrumentManager(manager);
    }

}
