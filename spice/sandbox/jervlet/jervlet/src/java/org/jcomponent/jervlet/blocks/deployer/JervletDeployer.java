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
package org.jcomponent.jervlet.blocks.deployer;

import java.io.File;
import org.jcomponent.jervlet.Jervlet;
import org.jcomponent.jervlet.JervletContext;
import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.context.Context;
import org.apache.avalon.framework.context.Contextualizable;
import org.apache.avalon.framework.logger.AbstractLogEnabled;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.avalon.framework.service.Serviceable;
import org.apache.avalon.phoenix.BlockContext;

/**
 * @phoenix:block
 *
 * Test Jervlet (Tomcat wrapper).
 *
 * @author  Vinay Chandran<vinayc77@yahoo.com>
 * @version 1.0
 */
public class JervletDeployer
    extends AbstractLogEnabled
    implements Contextualizable, Serviceable, Configurable, Initializable
{
    private BlockContext m_context;
    private Configuration m_configuration;
    private ServiceManager m_manager;
    private Jervlet m_jervlet;

    public void contextualize( final Context context )
    {
        m_context = (BlockContext)context;
    }

    /**
     * @phoenix:configuration-schema type="http://relaxng.org/ns/structure/1.0"
     */
    public void configure( final Configuration configuration )
        throws ConfigurationException
    {
        m_configuration = configuration;
    }

    /**
     * @see org.apache.avalon.framework.service.Serviceable
     * @phoenix:dependency name="org.jcomponent.jervlet.Jervlet"
     */
    public void service( final ServiceManager serviceManager )
        throws ServiceException
    {
        m_manager = serviceManager;
        m_jervlet = (Jervlet)m_manager.lookup( Jervlet.ROLE );
    }

    public void initialize() throws Exception
    {
        final JervletContext jervletContext = new JervletContext( m_context, m_manager, getLogger() );
        final Configuration[] contexts = m_configuration.getChildren( "Context" );

        for( int i = 0; i < contexts.length; i++ )
        {
            final String context = contexts[ i ].getAttribute( "docBase" );
            String path = contexts[ i ].getAttribute( "path" );

            path.replace( '/', File.separatorChar ).replace( '\\', File.separatorChar );

            final File pathFile = getAbsolutePath( path );

            if( !pathFile.exists() )
            {
                throw new ConfigurationException( "Path for webapp '" + context
                                                  + "' does not exist [path: " + pathFile + "]" );
            }
            else if( !pathFile.canRead() )
            {
                throw new ConfigurationException( "Path for webapp '" + context
                                                  + "' cannot be read [path: " + pathFile + "]" );
            }

            m_jervlet.deploy( context, pathFile, jervletContext );
        }
    }

    private File getAbsolutePath( String path )
    {
        if( path.startsWith( "file:" ) )
        {
            return new File( path.substring( "file:".length() ) );
        }
        else
        {
            return new File( m_context.getBaseDirectory().getAbsolutePath()
                             + File.separatorChar + path );
        }
    }
}
