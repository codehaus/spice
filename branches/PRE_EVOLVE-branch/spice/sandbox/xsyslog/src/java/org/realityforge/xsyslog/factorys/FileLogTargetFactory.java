/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.xsyslog.factorys;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.log.Hierarchy;
import org.apache.log.LogTarget;
import org.apache.log.format.ExtendedPatternFormatter;
import org.apache.log.format.Formatter;
import org.apache.log.format.XMLFormatter;
import org.realityforge.xsyslog.runtime.LogTargetFactory;
import java.io.File;

/**
 * &lt;file id="foo"&gt;
 *  &lt;filename&gt;${context-key}/real-name/...&lt;/filename&gt;
 *  &lt;format type="raw|pattern|extended"&gt;pattern to be used if needed&lt;/format&gt;
 *  &lt;append&gt;true|false&lt;/append&gt;
 *  &lt;rotation type="revolving" init="5" max="10"&gt;
 *
 * or
 *
 *  &lt;rotation type="unique" pattern="yyyy-MM-dd-hh-mm-ss" suffix=".log"&gt;
 *   &lt;or&gt;
 *    &lt;size&gt;10000000&lt;/size&gt;
 *    &lt;time&gt;24:00:00&lt;/time&gt;
 *    &lt;time&gt;12:00:00&lt;/time&gt;
 *   &lt;/or&gt;
 *  &lt;/rotation&gt;
 * &lt;/file&gt;
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-06-27 03:27:51 $
 */
public class FileLogTargetFactory
    implements LogTargetFactory
{
    public LogTarget createTarget( final Configuration configuration )
        throws ConfigurationException
    {
        final String filename = configuration.getChild( "filename" ).getValue();
        final Configuration formatConfig = configuration.getChild( "format", false );
        final Formatter formatter = createFormatter( formatConfig );

        final File file = new File( filename );

        return null;
    }

    protected Formatter createFormatter( final Configuration configuration )
    {
        if( null == configuration )
        {
            return null;
        }
        final String type = configuration.getAttribute( "type", "pattern" );
        if( "pattern".equals( type ) )
        {
            final String format = configuration.getValue( Hierarchy.DEFAULT_FORMAT );
            return new ExtendedPatternFormatter( format, 1 );
        }
        else if( "xml".equals( type ) )
        {
            return new XMLFormatter();
        }
        else
        {
            throw new IllegalArgumentException( "Unknown formatter type " + type );
        }
    }
}
