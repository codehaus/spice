/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.introspector;

import org.realityforge.metaclass.model.ClassDescriptor;

/**
 * This Accessor implementation will wrap another MetaClassAccessor.
 * The purpose of the wrapping is to make it impossible for users to
 * get to the underlying accessor implementation.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-10-28 06:41:56 $
 */
public class WrapperMetaClassAccessor
    implements MetaClassAccessor
{
    /**
     * The underlying MetaClassAccessor.
     */
    private final MetaClassAccessor m_accessor;

   /**
    * Create an accessor for specified accessor.
    *
    * @param accessor the accessor
    */
   public WrapperMetaClassAccessor( final MetaClassAccessor accessor )
   {
      if( null == accessor )
      {
         throw new NullPointerException( "accessor" );
      }
      m_accessor = accessor;
   }

    /**
     * @see MetaClassAccessor#getClassDescriptor
     */
    public synchronized ClassDescriptor getClassDescriptor( final String classname,
                                                            final ClassLoader classLoader )
        throws MetaClassException
    {
        return m_accessor.getClassDescriptor( classname, classLoader );
    }
}
