/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.cli;

/**
 * Class to inherit from so when in future when new controls are added
 * clients will no have to implement them.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.5 $ $Date: 2003-06-27 03:45:15 $
 * @since 4.0
 * @see ParserControl
 */
public abstract class AbstractParserControl
    implements ParserControl
{
    /**
     * By default always continue parsing by returning false.
     *
     * @param lastOptionCode the code of last option parsed
     * @return return true to halt, false to continue parsing
     * @see ParserControl#isFinished(int)
     */
    public boolean isFinished( int lastOptionCode )
    {
        return false;
    }
}
