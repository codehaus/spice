/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.jndikit;

import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.spi.ObjectFactory;
import javax.naming.spi.StateFactory;

/**
 * This is the class to extend that provides
 * basic facilities for Namespace management.
 *
 * @author <a href="mailto:peter@apache.org">Peter Donald</a>
 * @version $Revision: 1.1 $
 */
public abstract class AbstractNamespace
    implements Namespace
{
    protected ObjectFactory[] m_objectFactorySet;
    protected StateFactory[] m_stateFactorySet;

    public Object getStateToBind( final Object object,
                                  final Name name,
                                  final Context parent,
                                  final Hashtable environment )
        throws NamingException
    {
        //for thread safety so that member variable can be updated
        //at any time
        final StateFactory[] stateFactorySet = m_stateFactorySet;

        for( int i = 0; i < stateFactorySet.length; i++ )
        {
            final Object result =
                stateFactorySet[ i ].getStateToBind( object, name, parent, environment );

            if( null != result )
            {
                return result;
            }
        }

        return object;
    }

    public Object getObjectInstance( final Object object,
                                     final Name name,
                                     final Context parent,
                                     final Hashtable environment )
        throws Exception
    {
        //for thread safety so that member variable can be updated
        //at any time
        final ObjectFactory[] objectFactorySet = m_objectFactorySet;

        for( int i = 0; i < objectFactorySet.length; i++ )
        {
            final Object result =
                objectFactorySet[ i ].getObjectInstance( object, name, parent, environment );

            if( null != result )
            {
                return result;
            }
        }

        return object;
    }

    /**
     * Utility method for subclasses to add factorys.
     *
     * @param stateFactory the StateFactory to add
     */
    protected synchronized void addStateFactory( final StateFactory stateFactory )
    {
        //create new array of factory objects
        final StateFactory[] stateFactorySet =
            new StateFactory[ m_stateFactorySet.length + 1 ];

        //copy old factory objects to new array
        System.arraycopy( m_stateFactorySet, 0, stateFactorySet, 0, m_stateFactorySet.length );

        //add in new factory at end
        stateFactorySet[ m_stateFactorySet.length ] = stateFactory;

        //update factory set
        m_stateFactorySet = stateFactorySet;
    }

    /**
     * Utility method for subclasses to add factorys.
     *
     * @param objectFactory the ObjectFactory to add
     */
    protected synchronized void addObjectFactory( final ObjectFactory objectFactory )
    {
        //create new array of factory objects
        final ObjectFactory[] objectFactorySet =
            new ObjectFactory[ m_objectFactorySet.length + 1 ];

        //copy old factory objects to new array
        System.arraycopy( m_objectFactorySet, 0, objectFactorySet, 0, m_objectFactorySet.length );

        //add in new factory at end
        objectFactorySet[ m_objectFactorySet.length ] = objectFactory;

        //update factory set
        m_objectFactorySet = objectFactorySet;
    }
}
