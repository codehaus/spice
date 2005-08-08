package org.codehaus.spice.jervlet.impl.pico

import org.nanocontainer.script.groovy.NanoContainerBuilder;

builder = new NanoContainerBuilder( new JervletNanoContainerBuilderDecorationDelegate() );
pico = builder.container {
     webserver()
     webapp( context : "foo/", warPath : "foo.war" )
     component( StringBuffer )
}