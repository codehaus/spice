/*
 * Copyright  The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 */
package org.realityforge.configkit;

import java.io.File;
import java.io.ByteArrayInputStream;
import java.net.URL;

import junit.framework.TestCase;
import org.xml.sax.InputSource;

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

   public void testGetSchemaInputSource()
      throws Exception
   {
      final String resource = "foo.dtd";
      final URL url = new File( "." ).toURL();
      final ByteArrayInputStream stream =
         new ByteArrayInputStream( new byte[ 0 ] );
      final MockClassLoader classLoader = new MockClassLoader( url, stream );
      final InputSource source =
         ComponentConfigUtil.getSchemaInputSource( resource, classLoader );
      assertNotNull( source );
      assertEquals( "systemID", url.toExternalForm(), source.getSystemId() );
   }

   public void testGetSchemaInputSourceWithNonExistent()
      throws Exception
   {
      final String resource = "foo.dtd";
      final MockClassLoader classLoader = new MockClassLoader( null, null );
      final InputSource source =
         ComponentConfigUtil.getSchemaInputSource( resource, classLoader );
      assertNull( source );
   }

   public void testGetComponentConfigValidator()
      throws Exception
   {
      final ConfigValidator validator =
         ComponentConfigUtil.getComponentConfigValidator( "org.realityforge.configkit.test.Baz",
                                                          getClass().getClassLoader(),
                                                          "assembly.dtd",
                                                          null );
      assertNotNull( validator );
   }

   public void testGetComponentConfigValidatorWithNullClassLoader()
      throws Exception
   {
      try
      {
         ComponentConfigUtil.getComponentConfigValidator( "",
                                                          null,
                                                          "assembly.dtd",
                                                          null );
      }
      catch ( final NullPointerException npe )
      {
         assertEquals( "npe.message", "classLoader", npe.getMessage() );
         return;
      }
      fail( "Expected npe" );
   }

   public void testGetComponentConfigValidatorWithNullClassName()
      throws Exception
   {
      try
      {
         ComponentConfigUtil.getComponentConfigValidator( null,
                                                          getClass().getClassLoader(),
                                                          "assembly.dtd",
                                                          null );
      }
      catch ( final NullPointerException npe )
      {
         assertEquals( "npe.message", "classname", npe.getMessage() );
         return;
      }
      fail( "Expected npe" );
   }
}