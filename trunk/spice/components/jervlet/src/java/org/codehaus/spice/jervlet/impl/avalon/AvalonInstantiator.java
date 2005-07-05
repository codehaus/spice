/*

============================================================================
                  The Apache Software License, Version 1.1
============================================================================

Copyright (C) 2002,2003 The Apache Software Foundation. All rights reserved.

Redistribution and use in source and binary forms, with or without modifica-
tion, are permitted provided that the following conditions are met:

1. Redistributions of  source code must  retain the above copyright  notice,
   this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice,
   this list of conditions and the following disclaimer in the documentation
   and/or other materials provided with the distribution.

3. The end-user documentation included with the redistribution, if any, must
   include  the following  acknowledgment:  "This product includes  software
   developed  by the  Apache Software Foundation  (http://www.apache.org/)."
   Alternately, this  acknowledgment may  appear in the software itself,  if
   and wherever such third-party acknowledgments normally appear.

4. The names "Jakarta", "Avalon", "Excalibur" and "Apache Software Foundation"
   must not be used to endorse or promote products derived from this  software
   without  prior written permission. For written permission, please contact
   apache@apache.org.

5. Products  derived from this software may not  be called "Apache", nor may
   "Apache" appear  in their name,  without prior written permission  of the
   Apache Software Foundation.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS  FOR A PARTICULAR  PURPOSE ARE  DISCLAIMED.  IN NO  EVENT SHALL  THE
APACHE SOFTWARE  FOUNDATION  OR ITS CONTRIBUTORS  BE LIABLE FOR  ANY DIRECT,
INDIRECT, INCIDENTAL, SPECIAL,  EXEMPLARY, OR CONSEQUENTIAL  DAMAGES (INCLU-
DING, BUT NOT LIMITED TO, PROCUREMENT  OF SUBSTITUTE GOODS OR SERVICES; LOSS
OF USE, DATA, OR  PROFITS; OR BUSINESS  INTERRUPTION)  HOWEVER CAUSED AND ON
ANY  THEORY OF LIABILITY,  WHETHER  IN CONTRACT,  STRICT LIABILITY,  OR TORT
(INCLUDING  NEGLIGENCE OR  OTHERWISE) ARISING IN  ANY WAY OUT OF THE  USE OF
THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

This software  consists of voluntary contributions made  by many individuals
on  behalf of the Apache Software  Foundation. For more  information on the
Apache Software Foundation, please see <http://www.apache.org/>.

*/

/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.jervlet.impl.avalon;

import org.apache.avalon.framework.context.Context;
import org.apache.avalon.framework.logger.Logger;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.avalon.framework.container.ContainerUtil;

import org.codehaus.spice.jervlet.Instantiator;

//TODO: Should this instantiator extend AbstractInstantiator? Would it be nice to use setter injection too? Perhaps create different Instantiators for all possibilities? At least an AvalonSetterInstantiator should be created.
//TODO: Should we remove the context? It could be easier for back compatibility in the future. Not sure a Servlet really needs the Avalon context.

/**
 * Instantiator for Apache Avalon style Servlet and Filter components
 * <br/><br/>
 * If a component is <code>LogEnabled</code> then a
 * <code>Logger</code> is set on it, if it is <code>Servicable</code>
 * then a <code>ServiceManager</code> is set on it and if it is
 * <code>Contextualizable</code> then a <code>Context</code> is set
 * on it. Even though the <code>Intializable</code> life cycle
 * interface overlaps a servlet's <code>init()</code> method it is
 * called here. Note that no other lifecycle interfaces are
 * supported.
 *
 * @author Johan Sjoberg
 * @author Ben Hogan
 */
public class AvalonInstantiator implements Instantiator
{
    /** The service manager */
    private ServiceManager m_serviceManager;

    /** The logger */
    private Logger m_logger;

    /** The context */
    private Context m_context;

    /**
     * Create a new Avalon style Servlet and Filter instantiator
     * 
     * @param context The Avalon context to give to new servlets
     * @param serviceManager The service manager to give to new servlets 
     * @param logger The logger to give to new servlets.
     * @throws IllegalArgumentException if an argument was null.
     */
    public AvalonInstantiator( final Context context,
                               final ServiceManager serviceManager,
                               final Logger logger )
    {
        if( null == context || null == serviceManager || null == logger )
        {
            throw new IllegalArgumentException( "An argument was null." );
        }        
        m_context = context;
        m_serviceManager = serviceManager;
        m_logger = logger;
   }

    /**
     * Instantiate an Avalon style servlet or filter class.
     *
     * @param clazz The Avalon style class to instantiate
     * @return The instatiated avalon servlet component.
     * @throws InstantiationException If the class couldn't be instantiated
     * @throws IllegalAccessException if access to the class was restricted
     */
    public Object instantiate( Class clazz )
        throws InstantiationException, IllegalAccessException
    {
        Object instance = clazz.newInstance();
        try
        {
            ContainerUtil.enableLogging( instance, m_logger );
            ContainerUtil.contextualize( instance, m_context );
            ContainerUtil.service( instance, m_serviceManager );
            ContainerUtil.initialize( instance );
        }
        catch( Exception e )
        {
           final String msg = "Exception during lifecycle processing for class ["
             + clazz.getName() + "]. Exception message: " + e.getMessage();
           throw new InstantiationException( msg );
       }
       return instance;
   }
}
