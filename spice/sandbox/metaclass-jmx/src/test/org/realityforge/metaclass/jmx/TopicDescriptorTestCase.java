/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.jmx;

import junit.framework.TestCase;
import javax.management.modelmbean.ModelMBeanInfo;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-10-17 08:14:48 $
 */
public class TopicDescriptorTestCase
    extends TestCase
{
    public void testNameAndInfoSet()
        throws Exception
    {
        final ModelInfoCreationHelper helper = new ModelInfoCreationHelper();
        helper.setClassname( "X" );
        helper.setDescription( "Y" );
        final ModelMBeanInfo info = helper.toModelMBeanInfo();

        final TopicDescriptor descriptor = new TopicDescriptor( "X", info );
        assertEquals( "name", "X", descriptor.getName() );
        assertEquals( "info", info, descriptor.getInfo() );
    }

    public void testInfoSet()
        throws Exception
    {
        final ModelInfoCreationHelper helper = new ModelInfoCreationHelper();
        helper.setClassname( "X" );
        helper.setDescription( "Y" );
        final ModelMBeanInfo info = helper.toModelMBeanInfo();

        final TopicDescriptor descriptor = new TopicDescriptor( null, info );
        assertEquals( "name", null, descriptor.getName() );
        assertEquals( "info", info, descriptor.getInfo() );
    }

    public void testInfoNotSet()
        throws Exception
    {
        try
        {
            new TopicDescriptor( null, null );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "npe.getMessage()", "info", npe.getMessage() );
            return;
        }
        fail( "Expected NPE in ctor due to null info" );
    }
}
