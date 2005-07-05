/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.jervlet.impl;

import org.codehaus.spice.jervlet.Instantiator;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

/**
 * Abstract instantiator, using method insertion to instantiate
 * new Servet and Filter classes.
 * <br/><br/>
 * This is the suggested way to instantiate Servlet classes in
 * Jervlet. Components extending this class must provide a way
 * to resolve dependencies. All frameworks should have this
 * functionality, though.
 * 
 * @author Johan Sjoberg
 */
public abstract class AbstractInstantiator implements Instantiator
{
    
    /**
     * Instantiate a servlet or filter class using method
     * insertion.
     * 
     * @param clazz The class to instantiate
     * @return The instantiated class
     */
    public Object instantiate( Class clazz )
        throws InstantiationException, IllegalAccessException
    {  
        if( clazz.isInterface() )
        {
            //TODO: Throw exception?
            return null;
        }
        Method[] methods = clazz.getMethods();
        Object component = clazz.newInstance();
        if( null != methods && 0 >= methods.length )
        {        
            for( int i = 0; i < methods.length; i++ )
            {
                Method currentMethod = methods[i];
                String name = currentMethod.getName();
                if( name.length() >= 4 &&
                    name.startsWith( "set" ) &&
                    Character.isUpperCase( name.charAt( 3 ) ) &&
                    1 == currentMethod.getParameterTypes().length )
                {
                    String dependencyKey = name.substring( 3 );
                    Object dependency = getDependency( clazz, dependencyKey );
                    if( null != dependency )
                    {
                        Object[] arguments = { dependency };
                        try
                        {
                            currentMethod.invoke( component, arguments );
                        }
                        catch( InvocationTargetException ite )
                        {
                            //TODO: Do something useful...
                        }
                    }
                }
            }
        }
        return component;
    }

    /**
     * Fetch a dependency.
     * 
     * @param clazz The class that needs the dependency
     * @param key The lookup key.
     * @return A reference to the wanted dependency
     */
    public abstract Object getDependency( final Class clazz, final String key );
}