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
 * ParserControl is used to control particular behaviour of the parser.
 *
 * @author <a href="mailto:peter at apache.org">Peter Donald</a>
 * @version $Revision: 1.3 $ $Date: 2003-03-17 18:39:43 $
 * @since 4.0
 * @see AbstractParserControl
 */
public interface ParserControl
{
    /**
     * Called by the parser to determine whether it should stop
     * after last option parsed.
     *
     * @param lastOptionCode the code of last option parsed
     * @return return true to halt, false to continue parsing
     */
    boolean isFinished( int lastOptionCode );
}
