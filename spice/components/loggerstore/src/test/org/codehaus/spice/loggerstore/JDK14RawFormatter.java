/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.loggerstore;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * Class that extends base formatter to provide raw text output.
 *
 * @author Peter Donald
 * @version $Revision: 1.2 $ $Date: 2003-12-03 06:32:02 $
 */
public class JDK14RawFormatter
    extends Formatter
{
    public String format( final LogRecord record )
    {
        return formatMessage( record ) + "\n";
    }
}
