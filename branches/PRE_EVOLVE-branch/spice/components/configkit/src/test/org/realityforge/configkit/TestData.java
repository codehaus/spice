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
 * @version $Revision: 1.3 $ $Date: 2003-04-05 09:41:16 $
 */
class TestData
{
    static final String PUBLIC_ID = "-//PHOENIX/Assembly DTD Version 1.0//EN";
    static final String SYSTEM_ID = "http://jakarta.apache.org/phoenix/assembly_1_0.dtd";
    static final String RESOURCE = "org/realityforge/configkit/test/empty.txt";
    static final String SCHEMA = "org/realityforge/configkit/test/relax-schema.xml";
    static final String DTD = "org/realityforge/configkit/test/assembly.dtd";
    static final String ASSEMBLY_DATA = "org/realityforge/configkit/test/assembly1.xml";
    static final String XML_DATA = "org/realityforge/configkit/test/data1.xml";
    static final String XML_DATA2 = RESOURCE;
    static final String XML_DATA3 = "org/realityforge/configkit/test/data2.xml";

    static final String SCHEMA_PUBLIC_ID = "-//TEST/Blah DTD Version 1.0//EN";
    static final String SCHEMA_SYSTEM_ID = "http://www.realityforge.org/test/test.xml";

    static final EntityInfo INFO = new EntityInfo( PUBLIC_ID, SYSTEM_ID, RESOURCE );
    static final String CATALOG_JAR = "aTestCatalog.jar";
}
