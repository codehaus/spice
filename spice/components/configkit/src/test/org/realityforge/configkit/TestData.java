/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included  with this distribution in
 * the LICENSE.txt file.
 */
package org.realityforge.configkit;

/**
 * Data for unit tests.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-03-11 09:17:55 $
 */
class TestData
{
    static final String PUBLIC_ID = "-//PHOENIX/Assembly DTD Version 1.0//EN";
    static final String SYSTEM_ID = "http://jakarta.apache.org/phoenix/assembly_1_0.dtd";
    static final String RESOURCE = "org/realityforge/configkit/test/empty.txt";

    static final EntityInfo INFO = new EntityInfo( PUBLIC_ID, SYSTEM_ID, RESOURCE );
}
