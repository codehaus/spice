/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.tools.tasks;

import java.io.File;
import java.io.IOException;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-10-04 02:13:20 $
 */
class FailingMockMetaGenerateTask
    extends MockMetaGenerateTask
{
    File getOutputFileForClass( final String classname )
        throws IOException
    {
        throw new IOException( "Can not get output file" );
    }
}
