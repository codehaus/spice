/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.loggerstore;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * Class that extends base formatter to provide raw text output.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-05-23 04:41:46 $
 */
public class JDK14RawFormatter
    extends Formatter
{
    public String format( final LogRecord record )
    {
        return formatMessage( record );
    }
}
