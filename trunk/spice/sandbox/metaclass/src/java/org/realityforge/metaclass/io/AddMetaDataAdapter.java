/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassVisitor;
import org.realityforge.metaclass.model.ClassDescriptor;

/**
 * Create a ClassAdapter that simply adds the MetaClass metadata to .class
 * file.
 *
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2003-12-10 23:46:08 $
 */
class AddMetaDataAdapter
    extends ClassAdapter
{
    /** The class descriptor to add. */
    final ClassDescriptor m_descriptor;

    /** The IOE exception thrown when serializing descriptor. */
    private IOException m_ioe;

    /**
     * Create the adapter.
     *
     * @param cv the visitor to delegate to.
     * @param descriptor the descriptor to add
     */
    AddMetaDataAdapter( final ClassVisitor cv,
                        final ClassDescriptor descriptor )
    {
        super( cv );
        m_descriptor = descriptor;
    }

    /**
     * @see ClassAdapter#visitAttribute(Attribute)
     */
    public void visitAttribute( final Attribute attr )
    {
        if( !MetaClassIOASM.ATTRIBUTE_NAME.equals( attr.type ) )
        {
            super.visitAttribute( attr );
        }
    }

    /**
     * @see ClassAdapter#visit(int, String, String, String[], String)
     */
    public void visit( int access,
                       String name,
                       String superName,
                       String[] interfaces,
                       String sourceFile )
    {
        super.visit( access, name, superName, interfaces, sourceFile );
        try
        {
            final ByteArrayOutputStream output = new ByteArrayOutputStream();
            MetaClassIOBinary.IO.serializeClass( output, m_descriptor );
            final byte[] bytes = output.toByteArray();
            final Attribute attr =
                new Attribute( MetaClassIOASM.ATTRIBUTE_NAME,
                               bytes,
                               0,
                               bytes.length );
            super.visitAttribute( attr );
        }
        catch( final IOException ioe )
        {
            m_ioe = ioe;
        }
    }

    /**
     * Return the IOException thrown during serialization.
     *
     * @return the IOException thrown during serialization.
     */
    IOException getIoe()
    {
        return m_ioe;
    }
}
