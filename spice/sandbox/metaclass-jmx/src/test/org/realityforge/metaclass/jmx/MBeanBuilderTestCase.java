/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.jmx;

import junit.framework.TestCase;
import javax.management.modelmbean.ModelMBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import org.realityforge.metaclass.model.Attribute;
import org.realityforge.metaclass.model.ParameterDescriptor;
import java.util.Properties;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.4 $ $Date: 2003-10-13 23:48:54 $
 */
public class MBeanBuilderTestCase
    extends TestCase
{
    public void testParseImpactInfo()
        throws Exception
    {
        final MBeanBuilder builder = new MBeanBuilder();
        final int impact = builder.parseImpact( "INFO" );
        assertEquals( ModelMBeanOperationInfo.INFO, impact );
    }

    public void testParseImpactAction()
        throws Exception
    {
        final MBeanBuilder builder = new MBeanBuilder();
        final int impact = builder.parseImpact( "ACTION" );
        assertEquals( ModelMBeanOperationInfo.ACTION, impact );
    }

    public void testParseImpactActionInfo()
        throws Exception
    {
        final MBeanBuilder builder = new MBeanBuilder();
        final int impact = builder.parseImpact( "ACTION_INFO" );
        assertEquals( ModelMBeanOperationInfo.ACTION_INFO, impact );
    }

    public void testParseImpactUnknown()
        throws Exception
    {
        final MBeanBuilder builder = new MBeanBuilder();
        final int impact = builder.parseImpact( "UNKNOWN" );
        assertEquals( ModelMBeanOperationInfo.UNKNOWN, impact );
    }

    public void testParseParameterDescriptionWithoutAnyParameters()
        throws Exception
    {
        final MBeanBuilder builder = new MBeanBuilder();
        final String name = "myParam";
        final Attribute[] attributes = new Attribute[ 0 ];
        final String description =
            builder.parseParameterDescription( attributes, name );
        assertEquals( "", description );
    }

    public void testParseParameterDescriptionWithNonMatchingParameter()
        throws Exception
    {
        final MBeanBuilder builder = new MBeanBuilder();
        final String name = "myParam";
        final Properties parameters = new Properties();
        parameters.setProperty( "name", "myOtherParam" );
        parameters.setProperty( "description", "Blah!" );
        final Attribute[] attributes =
            new Attribute[]{new Attribute( "mx.parameter", parameters )};
        final String description =
            builder.parseParameterDescription( attributes, name );
        assertEquals( "", description );
    }

    public void testParseParameterDescriptionWithMatchingParameter()
        throws Exception
    {
        final MBeanBuilder builder = new MBeanBuilder();
        final String name = "myParam";
        final Properties parameters = new Properties();
        parameters.setProperty( "name", name );
        parameters.setProperty( "description", "Blah!" );
        final Attribute[] attributes =
            new Attribute[]{new Attribute( "mx.parameter", parameters )};
        final String description =
            builder.parseParameterDescription( attributes, name );
        assertEquals( "Blah!", description );
    }

    public void testBuildParametersViaReflection()
        throws Exception
    {
        final MBeanBuilder builder = new MBeanBuilder();
        final Class[] types = new Class[]{Integer.TYPE, String.class};
        final MBeanParameterInfo[] infos =
            builder.buildParametersViaReflection( types );
        assertEquals( "infos.length", 2, infos.length );
        assertEquals( "infos[0].type", "int", infos[ 0 ].getType() );
        assertEquals( "infos[0].description", "", infos[ 0 ].getDescription() );
        assertEquals( "infos[0].name", "", infos[ 0 ].getName() );
        assertEquals( "infos[1].type", "java.lang.String", infos[ 1 ].getType() );
        assertEquals( "infos[1].description", "", infos[ 1 ].getDescription() );
        assertEquals( "infos[1].name", "", infos[ 1 ].getName() );
    }

    public void testBuildParametersFromMetaData()
        throws Exception
    {
        final MBeanBuilder builder = new MBeanBuilder();
        final String name = "param1";
        final Properties parameters = new Properties();
        parameters.setProperty( "name", name );
        parameters.setProperty( "description", "Blah!" );
        final Attribute[] attributes =
            new Attribute[]{new Attribute( "mx.parameter", parameters )};
        final ParameterDescriptor descriptor1 = new ParameterDescriptor( "param1", "int" );
        final ParameterDescriptor descriptor2 = new ParameterDescriptor( "param2", "java.lang.String" );
        final ParameterDescriptor[] descriptors =
            new ParameterDescriptor[]{descriptor1, descriptor2};
        final MBeanParameterInfo[] infos =
            builder.buildParametersFromMetaData( attributes, descriptors );
        assertEquals( "infos.length", 2, infos.length );
        assertEquals( "infos[0].type", "int", infos[ 0 ].getType() );
        assertEquals( "infos[0].description", "Blah!", infos[ 0 ].getDescription() );
        assertEquals( "infos[0].name", "param1", infos[ 0 ].getName() );
        assertEquals( "infos[1].type", "java.lang.String", infos[ 1 ].getType() );
        assertEquals( "infos[1].description", "", infos[ 1 ].getDescription() );
        assertEquals( "infos[1].name", "param2", infos[ 1 ].getName() );
    }
}
