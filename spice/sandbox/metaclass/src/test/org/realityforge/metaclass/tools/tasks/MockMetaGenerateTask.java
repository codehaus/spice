/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.tools.tasks;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.2 $ $Date: 2003-09-07 10:21:54 $
 */
class MockMetaGenerateTask
    extends GenerateClassDescriptorsTask
{
    public void log( final String message )
    {
        System.out.println( message );
    }

    public void log( final String message,
                     final int level )
    {
        log( message );
    }
}
