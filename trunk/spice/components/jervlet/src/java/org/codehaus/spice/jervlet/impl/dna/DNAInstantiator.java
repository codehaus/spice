/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.jervlet.impl.dna;

import org.codehaus.dna.ResourceLocator;
import org.codehaus.dna.Logger;
import org.codehaus.dna.impl.ContainerUtil;

import org.codehaus.spice.jervlet.Instantiator;

/**
 * Instantiator for DNA style servlet and filter components.
 * <br/><br/>
 * If a class is <code>LogEnabled</code> then a <code>Logger</code>
 * is set on the class, if it is <code>Composable</code> then a
 * <code>ResourceLocator</code> is set on it. Even though the
 * <code>Active</code> life cycle interface overlaps a servlet's
 * init() method, <code>initialize</code> is called. Note that no
 * other lifecycle interfaces are supported, including destruction.
 *
 * @author Johan Sjoberg
 */
public class DNAInstantiator implements Instantiator
{
    /** The service manager */
    private ResourceLocator m_resourceLocator;

    /** The logger */
    private Logger m_logger;

    /**
     * Create a new DNA style instantiator.
     * 
     * @param resourceLocator The resource locator to give to new servlets
     * @param logger The logger to give to new servlets.
     * @throws IllegalArgumentException if some argument is null.
     */
    public DNAInstantiator( final ResourceLocator resourceLocator,
                            final Logger logger )
    {
        if( null == resourceLocator || null == logger )
        {
            throw new IllegalArgumentException( "An argument was null." );
        }        
        m_resourceLocator = resourceLocator;
        m_logger = logger;
   }

    /**
     * Instantiate a DNA style Servlet or Filter class.
     *
     * @param clazz the DNA style servlet or filter class to instantiate
     * @return the instatiated class
     * @throws InstantiationException if the class couldn't be instantiated
     * @throws IllegalAccessException if access to the class was restricted
     */
    public Object instantiate( Class clazz )
        throws InstantiationException, IllegalAccessException
    {
        Object instance = clazz.newInstance();
        try
        {
            ContainerUtil.enableLogging( instance, m_logger );
            ContainerUtil.compose( instance, m_resourceLocator );
            ContainerUtil.initialize( instance );
        }
        catch( Exception e )
        {
           final String msg = "Exception during lifecycle processing for servlet ["
             + clazz.getName() + "]. Exception message: " + e.getMessage();
           throw new InstantiationException( msg );
       }
       return instance;
   }
}
