/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.salt.lang;

import junit.framework.TestCase;

/**
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2003-12-02 02:15:05 $
 */
public class JavaVersionTestCase
    extends TestCase
{
    public JavaVersionTestCase( final String name )
    {
        super( name );
    }

    public void testCurrentVersion()
    {
        assertNotNull( "JavaVersion.CURRENT",
                       JavaVersion.CURRENT );
    }

    public void testIsLessThanOrEqual()
    {
        final JavaVersion javaVersion = new JavaVersion( "test", 125 );
        assertEquals( "javaVersion.toString()",
                      "test",
                      javaVersion.toString() );
        assertTrue( "javaVersion.isLessThanOrEqual()",
                    javaVersion.isLessThanOrEqual( javaVersion ) );
        assertTrue( "JAVA1_0.isLessThanOrEqual()",
                    JavaVersion.JAVA1_0.isLessThanOrEqual( javaVersion ) );
        assertTrue( "isLessThanOrEqual(JAVA1_0)",
                    !javaVersion.isLessThanOrEqual( JavaVersion.JAVA1_0 ) );
        assertTrue( "JAVA1_1.isLessThanOrEqual()",
                    JavaVersion.JAVA1_1.isLessThanOrEqual( javaVersion ) );
        assertTrue( "isLessThanOrEqual(JAVA1_1)",
                    !javaVersion.isLessThanOrEqual( JavaVersion.JAVA1_1 ) );
        assertTrue( "JAVA1_2.isLessThanOrEqual()",
                    JavaVersion.JAVA1_2.isLessThanOrEqual( javaVersion ) );
        assertTrue( "isLessThanOrEqual(JAVA1_2)",
                    !javaVersion.isLessThanOrEqual( JavaVersion.JAVA1_2 ) );
        assertTrue( "JAVA1_3.isLessThanOrEqual()",
                    !JavaVersion.JAVA1_3.isLessThanOrEqual( javaVersion ) );
        assertTrue( "isLessThanOrEqual(JAVA1_3)",
                    javaVersion.isLessThanOrEqual( JavaVersion.JAVA1_3 ) );
        assertTrue( "JAVA1_4.isLessThanOrEqual()",
                    !JavaVersion.JAVA1_3.isLessThanOrEqual( javaVersion ) );
        assertTrue( "isLessThanOrEqual(JAVA1_4)",
                    javaVersion.isLessThanOrEqual( JavaVersion.JAVA1_3 ) );
    }
}
