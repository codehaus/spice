/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.loggerstore;

import java.util.Map;
import org.codehaus.spice.loggerstore.factories.LogKitLoggerStoreFactory;

/**
 * MockLogKitLoggerStoreFactory extends LogKitLoggerStoreFactory to allow
 * testing of protected methods, not otherwise testable
 *
 * @author Mauro Talevi
 * @version $Revision: 1.3 $ $Date: 2004-02-28 21:13:22 $
 */
public class MockLogKitLoggerStoreFactory
    extends LogKitLoggerStoreFactory
{

    protected ClassLoader getClassLoader( final Map data )
    {
        return super.getClassLoader( data );
    }

}
