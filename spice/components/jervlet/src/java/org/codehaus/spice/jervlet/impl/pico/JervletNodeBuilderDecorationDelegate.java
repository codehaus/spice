/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.jervlet.impl.pico;

import org.codehaus.spice.jervlet.containers.jetty.pico.PicoJettyContainer;
import org.nanocontainer.NanoContainer;
import org.nanocontainer.script.NanoContainerMarkupException;
import org.nanocontainer.script.NullNodeBuilderDecorationDelegate;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.Parameter;
import org.picocontainer.defaults.ComponentParameter;
import org.picocontainer.defaults.ConstantParameter;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Map;

/**
 * @author <a href="mailto:peter.royal@pobox.com">peter royal</a>
 */
public class JervletNodeBuilderDecorationDelegate extends NullNodeBuilderDecorationDelegate
{
    public Object createNode( Object name, Map attributes, Object parent )
    {
        //TODO check parent is a NanoContainer TDD-style
        if( "webserver".equals( name ) )
        {
            MutablePicoContainer pico = ( (NanoContainer)parent ).getPico();

            pico.registerComponentImplementation( PicoJettyContainer.class );
            pico.registerComponentImplementation( PicoDefaultContextHandler.class );

            return null;
        }
        else if( "webapp".equals( name ) )
        {
            MutablePicoContainer pico = ( (NanoContainer)parent ).getPico();
            String context = (String)attributes.remove( "context" );
            //TODO should likely attempt as a URL, then if that fails, treat as a file (relative to working dir)
            String warPath = (String)attributes.remove( "warPath" );

            try
            {
                pico.registerComponentImplementation(
                    PicoContext.class,
                    PicoContext.class,
                    new Parameter[]{new ComponentParameter(),
                                    new ConstantParameter( context ),
                                    new ConstantParameter( new File( warPath ).toURL() ),
                                    new ConstantParameter( new PicoInstantiator( pico ) )} );
            }
            catch( MalformedURLException e )
            {
                throw new NanoContainerMarkupException("Malformed warPath: " + warPath, e );
            }

            return null;
        }

        return super.createNode( name, attributes, parent );
    }
}