/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.swingactions;

import java.io.InputStream;

import org.jcomponent.swingactions.reader.ConfigReader;

/**
 * XMLActionManager is an implementation of <code>ActionManager</code> which
 * supports configuration via XML.
 *
 * @author <a href="mailto:mauro.talevi at aquilonia.org">Mauro Talevi</a>
 */
public class XMLActionManager
    extends AbstractActionManager
{
    final ConfigReader reader;

    public XMLActionManager( final InputStream resource ) 
        throws Exception
    {
        reader = new ConfigReader();
        reader.parse( resource );
    }
}
