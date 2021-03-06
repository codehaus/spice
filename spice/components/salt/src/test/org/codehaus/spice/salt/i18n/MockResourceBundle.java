/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.salt.i18n;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.ResourceBundle;

/**
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2003-12-02 02:15:05 $
 */
public class MockResourceBundle
    extends ResourceBundle
{
    private static final Hashtable c_resources = new Hashtable();

    public static final void cleanResourceSet()
    {
        c_resources.clear();
    }

    public static final void addResource( final String key,
                                          final Object resource )
    {
        c_resources.put( key, resource );
    }

    protected Object handleGetObject( final String key )
    {
        return c_resources.get( key );
    }

    public Enumeration getKeys()
    {
        return c_resources.keys();
    }
}
