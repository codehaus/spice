/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.tools.tasks;

import junit.framework.TestCase;
import java.util.Collection;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.Reference;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.2 $ $Date: 2003-11-20 10:00:39 $
 */
public class PluginSetTestCase
    extends TestCase
{
    public void testAddPluginWithNullName()
        throws Exception
    {
        final PluginElement element = new PluginElement();
        final FilterSet set = new FilterSet();
        try
        {
            set.addFilter( element );
        }
        catch( BuildException e )
        {
            return;
        }
        fail( "Expected to fail due to badly specified element" );
    }

    public void testAddSinglePlugin()
        throws Exception
    {
        final PluginElement element = new PluginElement();
        element.setName( "foo" );
        final FilterSet set = new FilterSet();
        set.addFilter( element );
        final Collection collection = set.toPlugins();
        assertEquals( "collection.size()", 1, collection.size() );
        assertEquals( "collection(0)",
                      element,
                      collection.iterator().next() );
    }

    public void testAddSinglePluginSet()
        throws Exception
    {
        final PluginElement element = new PluginElement();
        element.setName( "foo" );
        final FilterSet set = new FilterSet();
        set.addFilter( element );
        final FilterSet parent = new FilterSet();
        parent.addFilterSet( set );
        final Collection collection = parent.toPlugins();
        assertEquals( "collection.size()", 1, collection.size() );
        assertEquals( "collection(0)",
                      element,
                      collection.iterator().next() );
    }

    public void testSetRefID()
        throws Exception
    {
        final PluginElement element = new PluginElement();
        element.setName( "foo" );
        final FilterSet set = new FilterSet();
        set.addFilter( element );
        final FilterSet parent = new FilterSet();
        final MockProject project = new MockProject();
        project.bindReference( "bar", set );
        parent.setProject( project );
        parent.setRefid( new Reference( "bar" ) );

        final Collection collection = parent.toPlugins();
        assertEquals( "collection.size()", 1, collection.size() );
        assertEquals( "collection(0)",
                      element,
                      collection.iterator().next() );
    }

    public void testSetRefIDOnBadType()
        throws Exception
    {
        final FilterSet parent = new FilterSet();
        final MockProject project = new MockProject();
        project.bindReference( "bar", new Object() );
        parent.setProject( project );
        try
        {
            parent.setRefid( new Reference( "bar" ) );
        }
        catch( final BuildException be )
        {
            return;
        }
        fail( "Expected build exception due to bad type" );
    }

    public void testSetRefIDWithPluginAdded()
        throws Exception
    {

        final InterceptorSet parent = new InterceptorSet();
        final PluginElement element = new PluginElement();
        element.setName( "foo" );
        parent.addInterceptor( element );
        parent.addInterceptorSet( new InterceptorSet() );
        final MockProject project = new MockProject();
        project.bindReference( "bar", new InterceptorSet() );
        parent.setProject( project );
        try
        {
            parent.setRefid( new Reference( "bar" ) );
        }
        catch( final BuildException be )
        {
            return;
        }
        fail( "Expected build exception due to too many attributes" );
    }
}
