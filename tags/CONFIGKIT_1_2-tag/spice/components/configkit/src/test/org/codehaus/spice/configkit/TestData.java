/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included  with this distribution in
 * the LICENSE.txt file.
 */
package org.codehaus.spice.configkit;

/**
 * Data for unit tests.
 *
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2003-12-03 03:19:29 $
 */
class TestData
{
    private static final String DATA_DIR = "org/codehaus/spice/configkit/test/";

    static final String PUBLIC_ID = "-//PHOENIX/Assembly DTD Version 1.0//EN";
    static final String SYSTEM_ID = "http://jakarta.apache.org/phoenix/assembly_1_0.dtd";
    static final String RESOURCE = DATA_DIR + "empty.txt";
    static final String SCHEMA = DATA_DIR + "relax-schema.xml";
    static final String DTD = DATA_DIR + "assembly.dtd";
    static final String ASSEMBLY_DATA = DATA_DIR + "assembly1.xml";
    static final String XML_DATA = DATA_DIR + "data1.xml";
    static final String XML_DATA2 = RESOURCE;
    static final String XML_DATA3 = DATA_DIR + "data2.xml";

    static final String SCHEMA_PUBLIC_ID = "-//TEST/Blah DTD Version 1.0//EN";
    static final String SCHEMA_SYSTEM_ID = "http://www.realityforge.org/test/test.xml";

    static final EntityInfo INFO = new EntityInfo( PUBLIC_ID,
                                                   SYSTEM_ID,
                                                   RESOURCE );
    static final String CATALOG_JAR = "aTestCatalog.jar";
}
