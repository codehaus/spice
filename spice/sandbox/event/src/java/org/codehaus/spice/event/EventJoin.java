/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.event;

/**
 * A sink that passes events onto a source.
 * 
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2004-01-22 02:23:29 $
 */
public interface EventJoin
    extends EventSink, EventSource
{
}
