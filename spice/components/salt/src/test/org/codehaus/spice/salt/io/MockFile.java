/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.salt.io;

import java.io.File;

/**
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2003-12-02 02:15:05 $
 */
class MockFile
    extends File
{
    boolean _isDirectory;
    File[] children;

    public MockFile( String pathname )
    {
        super( pathname );
        _isDirectory = false;
    }

    public MockFile( String pathname, boolean isDirectory )
    {
        super( pathname );
        _isDirectory = isDirectory;
    }

    public boolean isDirectory()
    {
        return _isDirectory;
    }

    public File[] listFiles()
    {
        return children;
    }
}
