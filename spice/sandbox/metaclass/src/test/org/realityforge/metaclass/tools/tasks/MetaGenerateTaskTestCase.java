/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.tools.tasks;

import junit.framework.TestCase;
import org.apache.tools.ant.BuildException;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-08-23 13:25:07 $
 */
public class MetaGenerateTaskTestCase
    extends TestCase
{

    public void testNullDestDir()
        throws Exception
    {
        final MockMetaGenerateTask task = new MockMetaGenerateTask();
        try
        {
            task.execute();
        }
        catch( BuildException e )
        {
            e.printStackTrace();
            return;
        }
        fail( "Expected execute to fail as no destdir specified." );
    }
}
