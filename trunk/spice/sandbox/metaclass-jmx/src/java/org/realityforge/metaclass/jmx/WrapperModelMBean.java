/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.jmx;

import javax.management.modelmbean.RequiredModelMBean;
import javax.management.modelmbean.ModelMBeanInfo;
import javax.management.modelmbean.InvalidTargetObjectTypeException;
import javax.management.MBeanException;
import javax.management.RuntimeOperationsException;
import javax.management.ObjectName;
import javax.management.MBeanServer;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistration;

public class WrapperModelMBean
   extends RequiredModelMBean
{
   private Object m_resource;

   public WrapperModelMBean( final ModelMBeanInfo info )
      throws MBeanException, RuntimeOperationsException
   {
      super( info );
   }

   public ObjectName preRegister( final MBeanServer server,
                                  final ObjectName name )
      throws Exception
   {
      final ObjectName objectName = super.preRegister( server, name );
      if ( m_resource instanceof MBeanRegistration )
      {
         ( (MBeanRegistration) m_resource ).preRegister( server, name );
      }
      return objectName;
   }

   public void postRegister( final Boolean registrationDone )
   {
      super.postRegister( registrationDone );
      if ( m_resource instanceof MBeanRegistration )
      {
         ( (MBeanRegistration) m_resource ).postRegister( registrationDone );
      }
   }

   public void preDeregister()
      throws Exception
   {
      super.preDeregister();
      if ( m_resource instanceof MBeanRegistration )
      {
         ( (MBeanRegistration) m_resource ).preDeregister();
      }
   }

   public void postDeregister()
   {
      super.postDeregister();
      if ( m_resource instanceof MBeanRegistration )
      {
         ( (MBeanRegistration) m_resource ).postDeregister();
      }
   }

   public void setManagedResource( final Object resource,
                                   final String resourceType )
      throws MBeanException, RuntimeOperationsException, InstanceNotFoundException, InvalidTargetObjectTypeException
   {
      super.setManagedResource( resource, resourceType );
      m_resource = resource;
   }
}
