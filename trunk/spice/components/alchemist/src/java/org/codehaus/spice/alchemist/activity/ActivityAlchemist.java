/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.alchemist.activity;

/**
 * Utility class containing methods to transform Activity objects.
 * 
 * @author Mauro Talevi
 * @version $Revision: 1.1 $ $Date: 2004-06-17 22:27:22 $
 */
public class ActivityAlchemist {

    /**
     * Determines if an object is Avalon Initializable
     * 
     * @param object the Object to check
     * @return A boolean <code>true</code> if the object is an instance of
     * 		   {@link org.apache.avalon.framework.activity.Initializable}
     */
    public static boolean isAvalonInitializable( final Object object ){
        if ( object instanceof org.apache.avalon.framework.activity.Initializable )
        {
            return true;
        }
        return false;
    }
    
    /**
     * Casts an object to Avalon Initializable if possible.
     * 
     * @param object the Object to cast
     * @return A {@link org.apache.avalon.framework.activity.Initializable}
     * @throws IllegalArgumentException if not Avalon Initializable.
     */
    public static org.apache.avalon.framework.activity.Initializable toAvalonInitializable( final Object object ){
        if ( isAvalonInitializable( object ) )
        {
            return (org.apache.avalon.framework.activity.Initializable)object; 
        }
        final String message = ( object != null ? object.getClass().getName() : "Object" )
        						+ " is not Avalon Initializable";
        throw new IllegalArgumentException( message );
    }

    /**
     * Determines if an object is Avalon Disposable
     * 
     * @param object the Object to check
     * @return A boolean <code>true</code> if the object is an instance of
     * 		   {@link org.apache.avalon.framework.activity.Disposable}
     */
    public static boolean isAvalonDisposable( final Object object ){
        if ( object instanceof org.apache.avalon.framework.activity.Disposable )
        {
            return true;
        }
        return false;
    }
    
    /**
     * Casts an object to Avalon Disposable if possible.
     * 
     * @param object the Object to cast
     * @return A {@link org.apache.avalon.framework.activity.Disposable}
     * @throws IllegalArgumentException if not Avalon Disposable.
     */
    public static org.apache.avalon.framework.activity.Disposable toAvalonDisposable( final Object object ){
        if ( isAvalonDisposable( object ) )
        {
            return (org.apache.avalon.framework.activity.Disposable)object; 
        }
        final String message = ( object != null ? object.getClass().getName() : "Object" )
								+ " is not Avalon Disposable";
        throw new IllegalArgumentException( message );
    }

}
