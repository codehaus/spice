/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.jndikit.test;

import java.util.Enumeration;
import javax.naming.Context;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

/**
 * Unit testing for JNDI system
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.6 $
 */
public abstract class AbstractContextTestCase
    extends TestCase
{
    protected static final Object O1 = "iO1";
    protected static final Object O2 = "iO2";
    protected static final Object O3 = "iO3";
    protected static final Object O4 = "iO4";
    protected static final Object O5 = "iO5";
    protected static final Object O6 = "iO6";
    protected static final Object O7 = "iO7";
    protected static final Object O8 = "iO8";

    private Context m_context;
    private Context m_root;

    public AbstractContextTestCase( String name )
    {
        super( name );
    }

    protected void tearDown() throws Exception
    {
        if( null != m_context )
        {
            m_context.close();
        }
        if( null != m_root )
        {
            m_root.close();
        }
    }

    public void testBindToDirectContext()
        throws AssertionFailedError
    {
        try
        {
            m_context.bind( "O1", O1 );
            assertTrue( "Make sure lookup returns correct object",
                        m_context.lookup( "O1" ).equals( O1 ) );

            m_context.bind( "O2", O2 );
            m_context.bind( "O3", O3 );
            m_context.bind( "O4", O4 );
            m_context.bind( "O5", O5 );
            m_context.bind( "O6", O6 );
            m_context.bind( "O7", O7 );
            m_context.bind( "O8", O8 );

            assertTrue( "Make sure lookup O2 returns correct object",
                        m_context.lookup( "O2" ).equals( O2 ) );
            assertTrue( "Make sure lookup O3 returns correct object",
                        m_context.lookup( "O3" ).equals( O3 ) );
            assertTrue( "Make sure lookup O4 returns correct object",
                        m_context.lookup( "O4" ).equals( O4 ) );
            assertTrue( "Make sure lookup O5 returns correct object",
                        m_context.lookup( "O5" ).equals( O5 ) );
            assertTrue( "Make sure lookup O6 returns correct object",
                        m_context.lookup( "O6" ).equals( O6 ) );
            assertTrue( "Make sure lookup O7 returns correct object",
                        m_context.lookup( "O7" ).equals( O7 ) );
            assertTrue( "Make sure lookup O8 returns correct object",
                        m_context.lookup( "O8" ).equals( O8 ) );
        }
        catch( final NamingException ne )
        {
            throw new AssertionFailedError( ne.toString() );
        }
    }

    public void testUnBindFromDirectContext()
        throws AssertionFailedError
    {
        testBindToDirectContext();

        try
        {
            m_context.unbind( "O1" );
            m_context.unbind( "O2" );
            m_context.unbind( "O3" );
            m_context.unbind( "O4" );
            m_context.unbind( "O5" );
            m_context.unbind( "O6" );
            m_context.unbind( "O7" );
            m_context.unbind( "O8" );

            final Enumeration enum = m_context.list( "" );

            if( enum.hasMoreElements() )
            {
                fail( "Failed to unbind all test elements: ie " +
                      enum.nextElement() );
            }
        }
        catch( final NamingException ne )
        {
            throw new AssertionFailedError( ne.toString() );
        }
    }

    public void testBindToDirectSubContext()
        throws AssertionFailedError
    {
        try
        {
            m_context.createSubcontext( "x" );
            m_context.bind( "x/O1", O1 );
            assertTrue( "Make sure lookup x/O1 returns correct object",
                        m_context.lookup( "x/O1" ).equals( O1 ) );
            assertTrue( "Make sure lookup x/ returns correct object",
                        m_context.lookup( "x/" ) instanceof Context );
            assertTrue( "Make sure lookup x returns correct object",
                        m_context.lookup( "x" ) instanceof Context );

            m_context.bind( "x/O2", O2 );
            assertTrue( "Make sure lookup x/O2 returns correct object",
                        m_context.lookup( "x/O2" ).equals( O2 ) );
            assertTrue( "Make sure lookup x/ returns correct object",
                        m_context.lookup( "x/" ) instanceof Context );
            assertTrue( "Make sure lookup x returns correct object",
                        m_context.lookup( "x" ) instanceof Context );

            m_context.bind( "x/O3", O3 );
            assertTrue( "Make sure lookup x/O3 returns correct object",
                        m_context.lookup( "x/O3" ).equals( O3 ) );
            assertTrue( "Make sure lookup x/ returns correct object",
                        m_context.lookup( "x/" ) instanceof Context );
            assertTrue( "Make sure lookup x returns correct object",
                        m_context.lookup( "x" ) instanceof Context );
        }
        catch( final NamingException ne )
        {
            throw new AssertionFailedError( ne.toString() );
        }
    }

    public void testUnBindFromDirectSubContext()
        throws AssertionFailedError
    {
        testBindToDirectSubContext();

        try
        {
            m_context.unbind( "x/O1" );
            m_context.unbind( "x/O2" );
            m_context.unbind( "x/O3" );

            final Enumeration enum = m_context.list( "x/" );

            if( enum.hasMoreElements() )
            {
                fail( "Failed to unbind all test elements: ie " +
                      enum.nextElement() );
            }

            //unbind a unbound name - OK
            m_context.unbind( "a" );
            m_context.unbind( "x/a" );
        }
        catch( final NamingException ne )
        {
            throw new AssertionFailedError( ne.toString() );
        }
    }

    public void testBindToArbitarySubContexts()
        throws AssertionFailedError
    {
        try
        {
            m_context.createSubcontext( "x" );
            m_context.createSubcontext( "x/y" );
            m_context.bind( "x/y/O1", O1 );
            assertTrue( "Make sure lookup x/y/O1 returns correct object",
                        m_context.lookup( "x/y/O1" ).equals( O1 ) );
            assertTrue( "Make sure lookup x/y/ returns correct object",
                        m_context.lookup( "x/y/" ) instanceof Context );
            assertTrue( "Make sure lookup x/y returns correct object",
                        m_context.lookup( "x/y" ) instanceof Context );
            assertTrue( "Make sure lookup x returns correct object",
                        m_context.lookup( "x" ) instanceof Context );

            try
            {
                m_context.bind( "x/y", O2 );
                assertTrue( "Bound object to directory x/y.", false );
            }
            catch( final NamingException ne )
            {
            }

            try
            {
                m_context.bind( "x/y/", O2 );
                assertTrue( "Bound object to directory x/y/.", false );
            }
            catch( final NamingException ne )
            {
            }

            try
            {
                m_context.bind( "x/", O2 );
                assertTrue( "Bound object to directory x/.", false );
            }
            catch( final NamingException ne )
            {
            }

            try
            {
                m_context.bind( "x", O2 );
                assertTrue( "Bound object to directory x.", false );
            }
            catch( final NamingException ne )
            {
            }

            try
            {
                m_context.createSubcontext( "z" );
                m_context.bind( "z/", O2 );
                assertTrue( "Bound object to empty name z/.", false );
            }
            catch( final NamingException ne )
            {
            }
        }
        catch( final NamingException ne )
        {
            throw new AssertionFailedError( ne.toString() );
        }
    }

    public void testUnBindFromArbitarySubContext()
        throws AssertionFailedError
    {
        testBindToArbitarySubContexts();

        try
        {
            m_context.unbind( "x/y/O1" );

            //unbind non-existants - OK
            m_context.unbind( "x/O2" );
            m_context.unbind( "x/O3" );

            final Enumeration enum = m_context.list( "x/y" );

            if( enum.hasMoreElements() )
            {
                fail( "Failed to unbind all test elements: ie " +
                      enum.nextElement() );
            }

            //Not sure if the next is legal????
            /*
            try
            {
                m_context.unbind("x");
                assertTrue( "Unbound acontext!", false );
            }
            catch( final NamingException ne ) {}
            */

            //unbind a unbound name - OK
            m_context.unbind( "a" );
            m_context.unbind( "x/a" );
        }
        catch( final NamingException ne )
        {
            throw new AssertionFailedError( ne.toString() );
        }
    }

    public void testCreateSubContext()
        throws AssertionFailedError
    {
        try
        {
            m_context.createSubcontext( "x" );
            assertTrue( "Make sure lookup x returns correct object",
                        m_context.lookup( "x" ) instanceof Context );
            m_context.createSubcontext( "x/y" );
            assertTrue( "Make sure lookup x/y returns correct object",
                        m_context.lookup( "x/y" ) instanceof Context );

            try
            {
                m_context.createSubcontext( "z/x/y" );
                assertTrue( "Created a subcontext when intermediate contexts not created", false );
            }
            catch( final NamingException ne )
            {
            }

            try
            {
                m_context.createSubcontext( "x/y" );
                assertTrue( "createSubContext when context alreadty exists.", false );
            }
            catch( final NamingException ne )
            {
            }
        }
        catch( final NamingException ne )
        {
            throw new AssertionFailedError( ne.toString() );
        }
    }

    public void testDestroySubContext()
        throws AssertionFailedError
    {
        testCreateSubContext();

        try
        {
            try
            {
                m_context.destroySubcontext( "x" );
                assertTrue( "destroySubContext with existing subContexts.", false );
            }
            catch( final NamingException ne )
            {
            }

            try
            {
                m_context.destroySubcontext( "x/y/" );
                assertTrue( "destroySubContext with empty subContext name.", false );
            }
            catch( final NamingException ne )
            {
            }

            m_context.destroySubcontext( "x/y" );
            m_context.destroySubcontext( "x" );

            try
            {
                m_context.lookup( "z" );
                assertTrue( "subContext exists after delete.", false );
            }
            catch( final NamingException ne )
            {
            }
        }
        catch( final NamingException ne )
        {
            throw new AssertionFailedError( ne.toString() );
        }
    }

    public void testRenameToDirectContext()
        throws AssertionFailedError
    {
        try
        {
            m_context.bind( "O1", O1 );
            m_context.rename( "O1", "+O1" );
            assertTrue( "Make sure lookup not null",
                        m_context.lookup( "+O1" ) != null );
            assertTrue( "Make sure lookup +O1 returns correct object",
                        m_context.lookup( "+O1" ).equals( O1 ) );

            try
            {
                m_context.lookup( "O1" );
                assertTrue( "Old name still bound after rename", false );
            }
            catch( final NameNotFoundException nnfe )
            {
            }

            m_context.bind( "O2", O2 );
            m_context.rename( "O2", "+O2" );
            assertTrue( "Make sure lookup not null",
                        m_context.lookup( "+O2" ) != null );
            assertTrue( "Make sure lookup +O2 returns correct object",
                        m_context.lookup( "+O2" ).equals( O2 ) );

            try
            {
                m_context.lookup( "O2" );
                assertTrue( "Old name O2 still bound after rename", false );
            }
            catch( final NameNotFoundException nnfe )
            {
            }

            m_context.bind( "O3", O3 );
            m_context.rename( "O3", "+O3" );
            assertTrue( "Make sure lookup not null",
                        m_context.lookup( "+O3" ) != null );
            assertTrue( "Make sure lookup +O3 returns correct object",
                        m_context.lookup( "+O3" ).equals( O3 ) );
            try
            {
                m_context.lookup( "O3" );
                assertTrue( "Old name O3 still bound after rename", false );
            }
            catch( final NameNotFoundException nnfe )
            {
            }

            m_context.bind( "O4", O4 );
            m_context.rename( "O4", "+O4" );
            assertTrue( "Make sure lookup not null",
                        m_context.lookup( "+O4" ) != null );
            assertTrue( "Make sure lookup +04 returns correct object",
                        m_context.lookup( "+O4" ).equals( O4 ) );

            try
            {
                m_context.lookup( "O3" );
                assertTrue( "Old name O3 still bound after rename", false );
            }
            catch( final NameNotFoundException nnfe )
            {
            }
        }
        catch( final NamingException ne )
        {
            throw new AssertionFailedError( ne.toString() );
        }
    }

    public void testReBind()
        throws AssertionFailedError
    {
        try
        {
            m_context.bind( "O1", O1 );
            assertTrue( "Make sure lookup returns correct object",
                        m_context.lookup( "O1" ).equals( O1 ) );

            m_context.bind( "O2", O2 );
            m_context.bind( "O3", O3 );
            m_context.bind( "O4", O4 );
            m_context.bind( "O5", O5 );
            m_context.bind( "O6", O6 );
            m_context.bind( "O7", O7 );
            m_context.bind( "O8", O8 );
            assertTrue( "Make sure lookup O2 returns correct object",
                        m_context.lookup( "O2" ).equals( O2 ) );
            assertTrue( "Make sure lookup O3 returns correct object",
                        m_context.lookup( "O3" ).equals( O3 ) );
            assertTrue( "Make sure lookup O4 returns correct object",
                        m_context.lookup( "O4" ).equals( O4 ) );
            assertTrue( "Make sure lookup O5 returns correct object",
                        m_context.lookup( "O5" ).equals( O5 ) );
            assertTrue( "Make sure lookup O6 returns correct object",
                        m_context.lookup( "O6" ).equals( O6 ) );
            assertTrue( "Make sure lookup O7 returns correct object",
                        m_context.lookup( "O7" ).equals( O7 ) );
            assertTrue( "Make sure lookup O8 returns correct object",
                        m_context.lookup( "O8" ).equals( O8 ) );
        }
        catch( final NamingException ne )
        {
            throw new AssertionFailedError( ne.toString() );
        }

        try
        {
            m_context.rebind( "O1", O2 );
            assertTrue( "Rebind of O1 returns correct object",
                        m_context.lookup( "O1" ).equals( O2 ) );

            m_context.rebind( "O2", O3 );
            m_context.rebind( "O3", O4 );
            m_context.rebind( "O4", O5 );
            m_context.rebind( "O5", O6 );
            m_context.rebind( "O6", O7 );
            m_context.rebind( "O7", O8 );
            m_context.rebind( "O8", O1 );
            assertTrue( "Rebind of O2 returns correct object",
                        m_context.lookup( "O2" ).equals( O3 ) );
            assertTrue( "Rebind of O3 returns correct object",
                        m_context.lookup( "O3" ).equals( O4 ) );
            assertTrue( "Rebind of O4 returns correct object",
                        m_context.lookup( "O4" ).equals( O5 ) );
            assertTrue( "Rebind of O5 returns correct object",
                        m_context.lookup( "O5" ).equals( O6 ) );
            assertTrue( "Rebind of O6 returns correct object",
                        m_context.lookup( "O6" ).equals( O7 ) );
            assertTrue( "Rebind of O7 returns correct object",
                        m_context.lookup( "O7" ).equals( O8 ) );
            assertTrue( "Rebind of O8 returns correct object",
                        m_context.lookup( "O8" ).equals( O1 ) );

            m_context.bind( "x", O1 );
            assertTrue( "Make sure lookup x returns correct object",
                        m_context.lookup( "x" ).equals( O1 ) );
            m_context.rebind( "x", O8 );
            assertTrue( "Rebind of x returns correct object",
                        m_context.lookup( "x" ).equals( O8 ) );
        }
        catch( final NamingException ne )
        {
            throw new AssertionFailedError( ne.toString() );
        }

        try
        {
            m_context.createSubcontext( "x" );
            m_context.rebind( "x/", O1 );
            assertTrue( "Able to rebind empty name", false );
        }
        catch( final NamingException ne )
        {
        }
    }

    protected void setRoot( Context root )
    {
        m_root = root;
    }

    protected void setContext( Context context )
    {
        m_context = context;
    }
}
