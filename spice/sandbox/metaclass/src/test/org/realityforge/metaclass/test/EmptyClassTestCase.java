/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.test;

import java.lang.reflect.Modifier;

import org.realityforge.metaclass.model.Attribute;
import org.realityforge.metaclass.MetaClassIntrospector;
import org.realityforge.metaclass.test.data.EmptyClass;

public class EmptyClassTestCase
   extends AbstractFeatureTestCase
{
   protected void setUp()
      throws Exception
   {
      setClassDescriptor( MetaClassIntrospector.getClassDescriptor( EmptyClass.class ) );
   }

   public void testGetName()
   {
      assertEquals( "org.realityforge.metaclass.test.data.EmptyClass",
                    getClassDescriptor().getName() );
   }

   public void testGetModifiers()
   {
      assertEquals( Modifier.PUBLIC,
                    getClassDescriptor().getModifiers() );
   }

   public void testGetAttributes()
   {
      final Attribute[] attributes = getClassDescriptor().getAttributes();
      checkAttributesMatchExpected( Attribute.EMPTY_SET, attributes,
                                    "Class: getAttributes" );
   }
}
