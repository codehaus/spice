/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.salt.lang;

/**
 * Type safe wrapper class for Java Version enums.
 * {@link JavaVersion#CURRENT} defines the current Java version
 * and JAVA1_* define allowable versions. You can also compare
 * different versions via constructions such as;
 * <pre>
 *    JavaVersion.CURRENT.isLessThanOrEqual( JavaVersion.JAVA1_3 );
 * </pre>
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.3 $ $Date: 2003-07-16 04:44:51 $
 */
public final class JavaVersion
{
    /**
     * Enum for JDK1.0 JVM.
     */
    public static final JavaVersion JAVA1_0 = new JavaVersion( "Java 1.0", 100 );

    /**
     * Enum for JDK1.1 JVM.
     */
    public static final JavaVersion JAVA1_1 = new JavaVersion( "Java 1.1", 110 );

    /**
     * Enum for JDK1.2 JVM.
     */
    public static final JavaVersion JAVA1_2 = new JavaVersion( "Java 1.2", 120 );

    /**
     * Enum for JDK1.3 JVM.
     */
    public static final JavaVersion JAVA1_3 = new JavaVersion( "Java 1.3", 130 );

    /**
     * Enum for JDK1.4 JVM.
     */
    public static final JavaVersion JAVA1_4 = new JavaVersion( "Java 1.4", 140 );

    /**
     * Enum for JDK1.5 JVM.
     */
    public static final JavaVersion JAVA1_5 = new JavaVersion( "Java 1.5", 150 );

    /**
     * Enum for Current JDK.
     */
    public static final JavaVersion CURRENT = determineCurrentJavaVersion();

    /**
     * The name of version.
     */
    private final String m_name;

    /**
     * The code for version.
     */
    private final int m_code;

    /**
     * Private constructor so no instance except here can be defined.
     *
     * @param name the java version name
     * @param code the version * 100
     */
    JavaVersion( final String name, final int code )
    {
        m_name = name;
        m_code = code;
    }

    /**
     * Return true if the version object is is less than or equal to specified version.
     *
     * @return true if the version object is is less than or equal to specified version.
     */
    public boolean isLessThanOrEqual( final JavaVersion version )
    {
        return m_code <= version.m_code;
    }

    /**
     * Return the name of of JavaVersion.
     *
     * @return the name of of JavaVersion.
     */
    public String toString()
    {
        return m_name;
    }

    /**
     * Helper method to retrieve current JVM version.
     *
     * @return the current JVM version
     */
    private static final JavaVersion determineCurrentJavaVersion()
    {
        JavaVersion version = JavaVersion.JAVA1_0;

        try
        {
            Class.forName( "java.lang.Void" );
            version = JAVA1_1;
            Class.forName( "java.lang.ThreadLocal" );
            version = JAVA1_2;
            Class.forName( "java.lang.StrictMath" );
            version = JAVA1_3;
            Class.forName( "java.lang.CharSequence" );
            version = JAVA1_4;
        }
        catch( final ClassNotFoundException cnfe )
        {
        }

        return version;
    }
}
