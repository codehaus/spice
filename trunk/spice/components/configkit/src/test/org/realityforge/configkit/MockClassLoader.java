package org.realityforge.configkit;

import java.net.URL;
import java.io.InputStream;

class MockClassLoader
   extends ClassLoader
{
   private final URL m_url;
   private final InputStream m_inputStream;

   MockClassLoader( final URL url,
                           final InputStream inputStream )
   {
      m_url = url;
      m_inputStream = inputStream;
   }

   public URL getResource( String name )
   {
      return m_url;
   }

   public InputStream getResourceAsStream( String name )
   {
      return m_inputStream;
   }
}
