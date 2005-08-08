/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.jervlet.impl.pico;

import java.util.Map;

import org.codehaus.spice.jervlet.containers.jetty.DefaultJettyContainer;
import org.codehaus.spice.jervlet.ContextHandler;
import org.nanocontainer.script.NullNanoContainerBuilderDecorationDelegate;
import org.nanocontainer.NanoContainer;
import org.picocontainer.MutablePicoContainer;

/**
 * @author <a href="mailto:peter.royal@pobox.com">peter royal</a>
 */
public class JervletNanoContainerBuilderDecorationDelegate extends NullNanoContainerBuilderDecorationDelegate
{
    public Object createNode( Object name, Map attributes, Object parent )
    {
        //TODO check parent is a NanoContainer TDD-style
        if( "webserver".equals( name ))
        {
            final MutablePicoContainer pico = ( (NanoContainer)parent ).getPico();

            pico.registerComponentImplementation( DefaultJettyContainer.class );
            pico.registerComponentImplementation( PicoJettyContainer.class );

            final PicoJettyContainer jetty =
                (PicoJettyContainer)pico.getComponentInstance( PicoJettyContainer.class );

            pico.registerComponentInstance( jetty.createContextHandler() );

            return null;
        }
        else if( "webapp".equals( name ))
        {
            final MutablePicoContainer pico = ( (NanoContainer)parent ).getPico();
            final ContextHandler contextHandler = (ContextHandler)pico.getComponentInstance( ContextHandler.class );

            //contextHandler.addContext( new PicoContext() );

            return null;
        }

        return super.createNode( name, attributes, parent );
    }
}