/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.swingactions;

import java.io.InputStream;

import org.w3c.dom.Element;

/**
 * XMLActionManager is an implementation of <code>ActionManager</code> which
 * supports configuration via XML.
 *
 * @author <a href="mailto:mauro.talevi at aquilonia.org">Mauro Talevi</a>
 */
public class XMLActionManager
   extends AbstractActionManager
{
   public XMLActionManager( final Element element )
   {
   }

   public XMLActionManager( final InputStream resource )
   {
   }
}
