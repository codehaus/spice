/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.threadpool.impl;

import org.codehaus.spice.threadpool.ThreadControl;

/**
 * simpler holder for test data.
 *
 * @author Peter Donald
 * @version $Revision: 1.2 $ $Date: 2004-03-21 23:42:58 $
 */
class ThreadPoolEntry
{
    ThreadControl m_control;
    Work m_work;
}
