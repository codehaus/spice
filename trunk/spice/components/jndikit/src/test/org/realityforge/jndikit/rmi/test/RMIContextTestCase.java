/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 * This product includes software developed by the
 * Apache Software Foundation (http://www.apache.org/).
 */
package org.realityforge.jndikit.rmi.test;

import java.lang.reflect.Method;
import java.util.Hashtable;
import javax.naming.Context;
import org.realityforge.jndikit.rmi.RMIInitialContextFactory;
import org.realityforge.jndikit.rmi.server.Main;
import org.realityforge.jndikit.test.AbstractContextTestCase;

/**
 * Unit testing for JNDI system
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.3 $
 */
public class RMIContextTestCase
    extends AbstractContextTestCase
{
    private static int c_numTests = 0;
    private static int c_id = 0;
    private static Main c_server = new Main();
    private static Thread c_serverThread;
    private static boolean c_setUp = false;
    private Context m_rootContext;

    static
    {
        Class testCase = AbstractContextTestCase.class;

        final Method[] methods = testCase.getMethods();
        for( int i = 0; i < methods.length; i++ )
        {
            if( methods[ i ].getName().startsWith( "test" ) )
            {
                c_numTests++;
            }
        }
    }

    public RMIContextTestCase( String name )
    {
        super( name );
    }

    public void setUp() throws Exception
    {
        try
        {
            if( !c_setUp )
            {
                c_server.start();

                c_serverThread = new Thread( c_server );
                c_serverThread.start();
                c_setUp = true;
            }

            final RMIInitialContextFactory factory = new RMIInitialContextFactory();
            m_rootContext = factory.getInitialContext( new Hashtable() );

            m_context = m_rootContext.createSubcontext( "test" + c_id++ );
        }
        catch( final Exception e )
        {
            System.out.println( "Failed test initialisation " + e );
            e.printStackTrace();
            throw e;
        }
    }

    public void tearDown() throws Exception
    {
        try
        {
            m_context.close();
            m_context = null;
            m_rootContext.close();

            if( c_id >= c_numTests )
            {
                c_server.stop();
                c_server.dispose();
                c_serverThread.interrupt();
            }
        }
        catch( final Exception e )
        {
            System.out.println( "Failed test destruction" + e );
            e.printStackTrace();
            throw e;
        }
    }
}
