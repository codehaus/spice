/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.xinvoke.spi.impl;

/**
 * This is a adapter that exposes some class related selector
 * expressions to the Jexl expression language.
 *
 * @author <a href="mailto:peter at www.stocksoftware.com.au">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-04-16 10:47:01 $
 */
public class JexlClassHelper
{
    /**
     * The class this helper accesses information from.
     */
    private Class m_class;

    /**
     * Set the class that is used by helper.
     *
     * @param clazz the class that is used by helper.
     */
    public void setClass( final Class clazz )
    {
        m_class = clazz;
    }

    /**
     * Return true if the class has specified name.
     *
     * @return true if the class has specified name.
     */
    public boolean isClass( final String name )
    {
        return m_class.getName().equals( name );
    }

    /**
     * Return true if the class has name matching regex pattern.
     *
     * @return true if the class has name matching regex pattern.
     */
    public boolean isClassMatching( final String pattern )
    {
        final String name = m_class.getName();
        return MatcherUtil.match( pattern, name );
    }

    /**
     * Return true if the class has specified Class as superclass.
     *
     * @return true if the class has specified Class as superclass.
     */
    public boolean isSubClassOf( final String name )
    {
        return isSubClassOf( m_class, name );
    }

    /**
     * Return true if the class is in specified package.
     *
     * @return true if the class is in specified package.
     */
    public boolean inPackage( final String name )
    {
        return getPackageName().equals( name );
    }

    /**
     * Return true if the class is in package with name matching regex pattern.
     *
     * @return true if the class is in package with name matching regex pattern.
     */
    public boolean inPackageMatching( final String pattern )
    {
        return MatcherUtil.match( pattern, getPackageName() );
    }

    /**
     * Return true if the class has the specified Attribute.
     *
     * @return true if the class has the specified Attribute.
     */
    public boolean withClassAttribute( final String name )
    {
        return false;//Attributes.getClassAttribute( m_class, name );
    }

    /**
     * Recursive method to check if specified class, or
     * any of its interfaces or superclasses, implement specified
     * classname.
     *
     * @param clazz the class to test
     * @param classname the classname to look for
     * @return true if subclass, false otherwise
     */
    private boolean isSubClassOf( final Class clazz,
                                  final String classname )
    {
        if( null == clazz )
        {
            return false;
        }

        if( clazz.getName().equals( classname ) )
        {
            return true;
        }

        final Class[] interfaces = clazz.getInterfaces();
        for( int i = 0; i < interfaces.length; i++ )
        {
            if( isSubClassOf( interfaces[ i ], classname ) )
            {
                return true;
            }
        }

        return isSubClassOf( clazz.getSuperclass(), classname );
    }

    /**
     * Return name of package.
     * Note that this method does not invoke getPackage()
     * as it is not always set.
     *
     * @return the name of package class is in.
     */
    private String getPackageName()
    {
        final String classname = m_class.getName();
        final int index = classname.lastIndexOf( '.' );
        String pkgName = "";
        if( -1 != index )
        {
            pkgName = classname.substring( 0, index );
        }
        return pkgName;
    }
}
