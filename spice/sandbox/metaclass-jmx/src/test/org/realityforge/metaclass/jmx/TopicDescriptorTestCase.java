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
 * @author Peter Donald
 * @version $Revision: 1.2 $ $Date: 2003-11-28 03:13:45 $
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
