/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.tools.tasks;

import junit.framework.TestCase;
import org.apache.tools.ant.BuildException;
import java.util.Collection;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-11-20 10:00:39 $
 */
public class AddToPluginSetTaskTestCase
    extends TestCase
{
    public void testNullId()
        throws Exception
    {
        final AddToFilterSetTask task = new AddToFilterSetTask();
        task.setId( null );
        task.setRefid( "boo" );
        try
        {
            task.validate();
        }
        catch( final BuildException e )
        {
            return;
        }
        fail( "Expected to fail due to null id" );
    }

    public void testNullRefid()
        throws Exception
    {
        final AddToInterceptorSetTask task = new AddToInterceptorSetTask();
        task.setId( "blah" );
        task.setRefid( null );
        try
        {
            task.validate();
        }
        catch( final BuildException e )
        {
            return;
        }
        fail( "Expected to fail due to null id" );
    }

    public void testBadTypeofID()
        throws Exception
    {
        final AddToInterceptorSetTask task = new AddToInterceptorSetTask();
        final MockProject project = new MockProject();
        project.bindReference( "myid", new FilterSet() );
        project.bindReference( "myrefid", new InterceptorSet() );
        task.setProject( project );
        task.setId( "myid" );
        task.setRefid( "myrefid" );
        try
        {
            task.execute();
        }
        catch( final BuildException e )
        {
            return;
        }
        fail( "Expected to fail due to id referencing bad type" );
    }

    public void testBadTypeofRefID()
        throws Exception
    {
        final AddToInterceptorSetTask task = new AddToInterceptorSetTask();
        final MockProject project = new MockProject();
        project.bindReference( "myid", new InterceptorSet() );
        project.bindReference( "myrefid", new FilterSet() );
        task.setProject( project );
        task.setId( "myid" );
        task.setRefid( "myrefid" );
        try
        {
            task.execute();
        }
        catch( final BuildException e )
        {
            return;
        }
        fail( "Expected to fail due to refid referencing bad type" );
    }

    public void testSuccess()
        throws Exception
    {
        final AddToInterceptorSetTask task = new AddToInterceptorSetTask();
        final MockProject project = new MockProject();
        final InterceptorSet id = new InterceptorSet();
        project.bindReference( "myid", id );
        final InterceptorSet refid = new InterceptorSet();
        final PluginElement element = new PluginElement();
        element.setName( "foo" );
        refid.addInterceptor( element );
        project.bindReference( "myrefid", refid );
        task.setProject( project );
        task.setId( "myid" );
        task.setRefid( "myrefid" );
        task.execute();

        final Collection collection = id.toPlugins();
        assertEquals( "plugins.length", 1, collection.size() );
        assertEquals( "plugins(0)",
                      element,
                      collection.iterator().next() );
    }
}
