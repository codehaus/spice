/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.threadpool.impl;

import org.realityforge.threadpool.ThreadControl;

/**
 * simpler holder for test data.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-04-04 11:39:39 $
 */
class ThreadPoolEntry
{
    ThreadControl m_control;
    Work m_work;
}
