/*
 * Copyright  The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 */
package org.realityforge.configkit;

import junit.framework.TestCase;

/**
 * Basic unit tests for the catalog handler class.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 */
public final class ComponentConfigUtilTestCase
   extends TestCase
{
   public void testInstantiateInstance()
   {
      new ComponentConfigUtil();
   }

   public void testCalculatedLocationThatIsSpecified()
      throws Exception
   {
      final String resource =
         ComponentConfigUtil.calculateLocation( "com.biz.Foo", "Schema.xml" );
      assertEquals( "/com/biz/Schema.xml", resource );
   }

   public void testCalculatedLocationThatIsNotSpecified()
      throws Exception
   {
      final String resource =
         ComponentConfigUtil.calculateLocation( "com.biz.Foo", null );
      assertEquals( "/com/biz/Foo-schema.xml", resource );
   }

   public void testCalculatedLocationThatIsSpecifiedAsAnAbsolute()
      throws Exception
   {
      final String resource =
         ComponentConfigUtil.calculateLocation( "com.biz.Foo", "/Foo.xml" );
      assertEquals( "/Foo.xml", resource );
   }

   public void testCalculatedLocationThatIsSpecifiedForClassInBasePackage()
      throws Exception
   {
      final String resource =
         ComponentConfigUtil.calculateLocation( "Baz", "Foo.xml" );
      assertEquals( "/Foo.xml", resource );
   }
}

