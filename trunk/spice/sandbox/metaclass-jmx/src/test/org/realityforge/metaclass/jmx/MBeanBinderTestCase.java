/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.jmx;

import com.mockobjects.dynamic.C;
import com.mockobjects.dynamic.Mock;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import junit.framework.TestCase;
import org.realityforge.metaclass.introspector.MetaClassIntrospector;
import org.realityforge.metaclass.model.Attribute;
import org.realityforge.metaclass.model.ClassDescriptor;
import org.realityforge.metaclass.model.FieldDescriptor;
import org.realityforge.metaclass.model.MethodDescriptor;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.2 $ $Date: 2003-11-18 23:13:38 $
 */
public class MBeanBinderTestCase
    extends TestCase
{
    public void testCreateObjectNameWithNullTopic()
        throws Exception
    {
        final MBeanBinder binder = new MBeanBinder();
        final ObjectName baseName = new ObjectName( "MXTest:name=foo" );
        final ObjectName result =
            binder.createObjectName( baseName, null );
        assertEquals( "name", "MXTest:name=foo", result.toString() );
    }

    public void testCreateObjectNameWithNonNullTopic()
        throws Exception
    {
        final MBeanBinder binder = new MBeanBinder();
        final ObjectName baseName = new ObjectName( "MXTest:name=foo" );
        final ObjectName result =
            binder.createObjectName( baseName, "Blah" );
        assertEquals( "name", "MXTest:name=foo,topic=Blah", result.toString() );
    }

    public void testBindMBean()
        throws Exception
    {
        final Mock mock = new Mock( MBeanServer.class );
        mock.expectAndReturn( "registerMBean", C.anyArgs( 2 ), null );
        final MBeanServer mBeanServer = (MBeanServer)mock.proxy();

        final Attribute[] attributes =
            new Attribute[]{new Attribute( "mx.component" )};
        final ClassDescriptor classDescriptor =
            new ClassDescriptor( TestBean.class.getName(),
                                 attributes,
                                 attributes,
                                 FieldDescriptor.EMPTY_SET,
                                 MethodDescriptor.EMPTY_SET );
        final MockAccessor accessor = new MockAccessor( classDescriptor );
        MetaClassIntrospector.clearCompleteCache();
        MetaClassIntrospector.setAccessor( accessor );

        final MBeanBinder binder = new MBeanBinder();
        final ObjectName baseName = new ObjectName( "MXTest:name=foo" );
        binder.bindMBean( new TestBean(), baseName, mBeanServer );
        mock.verify();
    }

    public void testUnBindMBean()
        throws Exception
    {
        final Mock mock = new Mock( MBeanServer.class );
        mock.expectAndReturn( "isRegistered", C.anyArgs( 1 ), true );
        mock.expect( "unregisterMBean", C.anyArgs( 1 ) );
        final MBeanServer mBeanServer = (MBeanServer)mock.proxy();

        final Attribute[] attributes =
            new Attribute[]{new Attribute( "mx.component" )};
        final ClassDescriptor classDescriptor =
            new ClassDescriptor( TestBean.class.getName(),
                                 attributes,
                                 attributes,
                                 FieldDescriptor.EMPTY_SET,
                                 MethodDescriptor.EMPTY_SET );
        final MockAccessor accessor = new MockAccessor( classDescriptor );
        MetaClassIntrospector.clearCompleteCache();
        MetaClassIntrospector.setAccessor( accessor );

        final MBeanBinder binder = new MBeanBinder();
        final ObjectName baseName = new ObjectName( "MXTest:name=foo" );
        binder.unbindMBean( new TestBean(), baseName, mBeanServer );
        mock.verify();
    }

    public void testUnBindUnregisteredMBean()
        throws Exception
    {
        final Mock mock = new Mock( MBeanServer.class );
        mock.expectAndReturn( "isRegistered", C.anyArgs( 1 ), false );
        final MBeanServer mBeanServer = (MBeanServer)mock.proxy();

        final Attribute[] attributes =
            new Attribute[]{new Attribute( "mx.component" )};
        final ClassDescriptor classDescriptor =
            new ClassDescriptor( TestBean.class.getName(),
                                 attributes,
                                 attributes,
                                 FieldDescriptor.EMPTY_SET,
                                 MethodDescriptor.EMPTY_SET );
        final MockAccessor accessor = new MockAccessor( classDescriptor );
        MetaClassIntrospector.clearCompleteCache();
        MetaClassIntrospector.setAccessor( accessor );

        final MBeanBinder binder = new MBeanBinder();
        final ObjectName baseName = new ObjectName( "MXTest:name=foo" );
        binder.unbindMBean( new TestBean(), baseName, mBeanServer );
        mock.verify();
    }
}
