/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.threadpool;

/**
 * This class is the public frontend for the thread pool code.
 *
 * @author Peter Donald
 * @version $Revision: 1.2 $ $Date: 2004-03-21 23:43:00 $
 */
public interface ThreadPool
{
    /**
     * Run work in separate thread.
     * Return a valid ThreadControl to control work thread.
     *
     * @param work the work to be executed.
     * @return the ThreadControl
     */
    ThreadControl execute( Runnable work );

    /**
     * Run work in separate thread.
     * Return a valid ThreadControl to control work thread.
     *
     * @param work the work to be executed.
     * @return the ThreadControl
     */
    ThreadControl execute( Executable work );
}
