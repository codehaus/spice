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
package org.jcomponent.jervlet.blocks.jetty;

import org.jcomponent.jervlet.JervletContext;
import org.apache.avalon.framework.container.ContainerUtil;
import org.apache.avalon.framework.logger.Logger;
import org.apache.avalon.framework.service.ServiceManager;
import org.mortbay.jetty.servlet.ServletHolder;

/**
 *
 * Override for Jetty's ServletHolder allow custom servlet handling
 *
 *
 * @see <a href="http://jetty.mortbay.com/">Jetty Project Page</a>
 *
 * @author  Paul Hammant
 * @version 1.0
 */
class JervletServletHolder extends ServletHolder
{
    private JervletContext m_jervletContext;

    /**
     * Construct a Servlet Holder
     * @param jervletContext the context that is applied to the servlet on instantiation.
     * @param handler the handler
     * @param name the name
     * @param className the class name
     * @param forcedPath the forced path
     */
    public JervletServletHolder( JervletContext jervletContext,
                               JervletWebApplicationHandler handler,
                               String name,
                               String className,
                               String forcedPath )
    {
        // this constructor public or protected...
        super( handler, name, className, forcedPath );
        m_jervletContext = jervletContext;
    }

    /**
     * Create a new instance of a servlet, currently applying the following lifecycle:
     * If a servlet is Servicable then the serviceManager is set on the servlet.
     * If a servlet is LogEnabled then the logger is set on the servlet.
     * If a servlet is Initializable then the initialize method is invoked.
     * @return the instance
     * @throws InstantiationException if a prob
     * @throws IllegalAccessException if a prob
     */
    public synchronized Object newInstance() throws InstantiationException, IllegalAccessException
    {
        if( _class == null )
        {
            throw new InstantiationException( "No class for " + this );
        }
        else
        {
            final Logger logger = m_jervletContext.getLogger();
            Object instance = _class.newInstance();

            try
            {
                ContainerUtil.enableLogging( instance, logger );
                ContainerUtil.contextualize( instance, m_jervletContext.getContext() );

                final ServiceManager manager = m_jervletContext.getServiceManager();
                if( null != manager )
                {
                    ContainerUtil.service( instance, manager );
                }

                ContainerUtil.initialize( instance );
            }
            catch( Exception e )
            {
                final String msg = "Exception during lifecycle processing for servlet "
                    + _class.getName() + ":" + e.getMessage();

                logger.error( msg, e );

                throw new InstantiationException( msg );
            }

            return instance;
        }
    }
}
