/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included  with this distribution in
 * the LICENSE.txt file.
 */
package org.realityforge.xinvoke.spi.impl.test;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-04-16 10:47:03 $
 */
public class SimpleRunnable
    implements Runnable
{
    private int m_runCount;

    public void run()
    {
        System.out.println( "SimpleRunnable.run" );
        m_runCount++;
    }

    public int getRunCount()
    {
        return m_runCount;
    }
}
