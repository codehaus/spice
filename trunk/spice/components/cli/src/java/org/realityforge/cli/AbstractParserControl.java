/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 * This product includes software developed by the
 * Apache Software Foundation (http://www.apache.org/).
 */
package org.realityforge.cli;

/**
 * Class to inherit from so when in future when new controls are added
 * clients will no have to implement them.
 *
 * @author <a href="mailto:peter at apache.org">Peter Donald</a>
 * @version $Revision: 1.3 $ $Date: 2003-03-17 18:39:41 $
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
