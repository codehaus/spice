package org.realityforge.metaclass.introspector;

import java.util.Map;
import java.util.WeakHashMap;

import org.realityforge.metaclass.model.ClassDescriptor;

/**
 * Map backed MetaClassRegistry implementation.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-10-28 06:18:43 $
 */
public class DefaultMetaClassRegistry
   implements MetaClassRegistry
{
   /**
    * Class used to access the MetaData.
    */
   private MetaClassAccessor m_accessor = new DefaultMetaClassAccessor();

   /**
    * The cache in which info objects are stored.
    * This cache stores maps for ClassLoaders which in
    * turn stores info for particular classes in
    * classloader.
    */
   private final Map c_cache = new WeakHashMap();

   /**
    * Set the MetaClassAccessor to use to locate
    * ClassDescriptor objects.
    *
    * @param accessor the MetaClassAccessor
    */
   public void setAccessor( final MetaClassAccessor accessor )
   {
      if ( null == accessor )
      {
         throw new NullPointerException( "accessor" );
      }
      m_accessor = accessor;
   }

   /**
    * @see MetaClassRegistry#getClassDescriptor
    */
   public ClassDescriptor getClassDescriptor( final String classname,
                                              final ClassLoader classLoader )
      throws MetaClassException
   {
      final Map cache = getClassLoaderCache( classLoader );
      ClassDescriptor descriptor = (ClassDescriptor) cache.get( classname );
      if ( null != descriptor )
      {
         return descriptor;
      }
      else
      {
         descriptor = m_accessor.getClassDescriptor( classname, classLoader );
         cache.put( classname, descriptor );
         return descriptor;
      }
   }

   /**
    * @see MetaClassRegistry#registerDescriptor
    */
   public void registerDescriptor( final ClassDescriptor descriptor,
                                   final ClassLoader classLoader )
   {
      final Map cache = getClassLoaderCache( classLoader );
      cache.put( descriptor.getName(), descriptor );
   }

   /**
    * Get Cache for specified ClassLoader.
    *
    * @param classLoader the ClassLoader to get cache for
    * @return the Map/Cache for ClassLoader
    */
   private synchronized Map getClassLoaderCache( final ClassLoader classLoader )
   {
      Map map = (Map) c_cache.get( classLoader );
      if ( null == map )
      {
         map = new WeakHashMap();
         c_cache.put( classLoader, map );
      }
      return map;
   }

}
