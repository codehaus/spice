/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.jndikit.test;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.ContextNotEmptyException;
import javax.naming.NameClassPair;
import javax.naming.NameNotFoundException;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.NotContextException;
import javax.naming.Reference;
import javax.naming.Referenceable;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

/**
 * Unit testing for JNDI system
 *
 * @author Peter Donald
 * @version $Revision: 1.3 $
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

    protected Context m_context;
    protected Context m_root;
    private static int c_id = 0;


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

            final NamingEnumeration enum = m_context.list( "" );

            if( enum.hasMoreElements() )
            {
                fail( "Failed to unbind all test elements: ie " +
                      enum.nextElement() );
            }

            try
            {
                enum.nextElement();
                fail( "Expected nextElement() to throw NoSuchElementException" );
            }
            catch( final NoSuchElementException nsee )
            {
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

            try
            {
                enum.nextElement();
                fail( "Expected nextElement() to throw NoSuchElementException" );
            }
            catch( final NoSuchElementException nsee )
            {
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

            m_context.bind( "O1", O1 );
            try
            {
                // could potentially throw NotContextException (for O1) or
                // NameNotFoundException (for O2)
                m_context.bind( "O1/O2/O3", O3 );
                fail( "Expected bind to non-context to throw NamingException" );
            }
            catch( final NamingException expected )
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

            try
            {
                enum.nextElement();
                fail( "Expected nextElement() to throw NoSuchElementException" );
            }
            catch( final NoSuchElementException nsee )
            {
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

            m_context.bind( "x/y/O2", O2 );
            try
            {
                m_context.unbind( "x/y/O2/bogus" );
                fail(
                    "Expected unbind from non-context to throw NamingException" );
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

    /**
     * Verifies that attempting to bind to self throws NamingException.
     *
     * @throws AssertionFailedError if the test fails
     */
    public void testBindSelf()
        throws AssertionFailedError
    {
        try
        {
            m_context.bind( "", O1 );
            fail( "Expected bind to self to throw NamingException" );
        }
        catch( NamingException ne )
        {
        }
    }

    /**
     * Verifies that attempting to unbind self throws NamingException.
     *
     * @throws AssertionFailedError if the test fails
     */
    public void testUnbindSelf()
        throws AssertionFailedError
    {
        try
        {
            m_context.unbind( "" );
            fail( "Expected unbind of self to throw NamingException" );
        }
        catch( NamingException ne )
        {
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

            try
            {
                m_context.createSubcontext( "" );
                fail( "Created a subcontext with empty name" );
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
            catch( final ContextNotEmptyException ne )
            {
            }
            catch( final NamingException ne )
            {
                fail( "Expected ContextNotEmptyException but got " + ne );
            }

            try
            {
                m_context.destroySubcontext( "x/y/" );
                assertTrue( "destroySubContext with empty subContext name.", false );
            }
            catch( final NamingException ne )
            {
            }

            try
            {
                m_context.destroySubcontext( "x/a/y" );
                fail(
                    "destroySubcontext suceeded for non-existent intermediary subcontext" );
            }
            catch( final NameNotFoundException nnf )
            {
            }
            catch( final NamingException ne )
            {
                fail( "Expected NameNotFoundException but got " + ne );
            }

            m_context.destroySubcontext( "x/y" );

            // destroy non-existent context - OK
            m_context.destroySubcontext( "x/y" );


            m_context.destroySubcontext( "x" );

            try
            {
                m_context.lookup( "x" );
                assertTrue( "subContext exists after delete.", false );
            }
            catch( final NamingException ne )
            {
            }

            // destroy non-existent context - OK
            m_context.destroySubcontext( "x" );


            try
            {
                m_context.destroySubcontext( "" );
                fail( "destroySubcontext destroyed self" );
            }
            catch( final NamingException ne )
            {
            }

            try
            {
                m_context.bind( "x", O1 );
                m_context.destroySubcontext( "x" );
                fail( "destroySubcontext destroyed non-context" );
            }
            catch( final NotContextException nce )
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

            m_context.bind( "05", O5 );
            try
            {
                m_context.rename( "O5", "O5" );
            }
            catch( final NamingException ne )
            {
            }

            try
            {
                // rename self invalid
                m_context.rename( "", "" );
            }
            catch( final NamingException ne )
            {
            }

            try
            {
                m_context.rename( "", "O5" );
            }
            catch( final NamingException ne )
            {
            }

            try
            {
                m_context.rename( "O5", "" );
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
            m_context.createSubcontext( "x" );
            m_context.bind( "x/O8", O8 );
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
            assertTrue( "Make sure lookup x/O8 returns correct object",
                        m_context.lookup( "x/O8" ).equals( O8 ) );
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
            m_context.rebind( "x/O8", O1 );
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
            assertTrue( "Rebind of x/O8 returns correct object",
                        m_context.lookup( "x/O8" ).equals( O1 ) );

            m_context.bind( "y", O1 );
            assertTrue( "Make sure lookup x returns correct object",
                        m_context.lookup( "y" ).equals( O1 ) );
            m_context.rebind( "y", O8 );
            assertTrue( "Rebind of y returns correct object",
                        m_context.lookup( "y" ).equals( O8 ) );
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

    /**
     * Create a subcontext, bind to it, and verify that the objects
     * can be looked up from it
     */
    public void testSubcontextBindAndLookup() throws AssertionFailedError
    {
        try
        {
            m_context.createSubcontext( "x" );
            Context context = ( Context ) m_context.lookup( "x" );

            context.bind( "o1", O1 );

            assertTrue( "Make sure lookup o1 returns correct object",
                        context.lookup( "o1" ).equals( O1 ) );
            assertTrue( "Make sure lookup o1 from root returns correct object",
                        m_context.lookup( "x/o1" ).equals( O1 ) );
        }
        catch( final NamingException ne )
        {
            throw new AssertionFailedError( ne.toString() );
        }
    }

    /**
     * Create a subcontext, bind to it, and verify that: <ul> <li>can list the
     * binding names from the root context</li> <li>can list the binding names
     * from the subcontext</li> </ul>
     */
    public void testSubcontextBindAndList1() throws AssertionFailedError
    {
        Map map = new HashMap();
        map.put( "o1", O1 );
        map.put( "o2", O2 );
        map.put( "o3", O3 );
        Set names;

        try
        {
            m_context.createSubcontext( "x" );
            Context context = ( Context ) m_context.lookup( "x" );

            Iterator entries = map.entrySet().iterator();
            while( entries.hasNext() )
            {
                Map.Entry entry = ( Map.Entry ) entries.next();
                context.bind( ( String ) entry.getKey(), entry.getValue() );
            }
            names = listNames( m_context, "x" );
            assertEquals( "Make sure can list subcontext names from root",
                          map.keySet(), names );

            names = listNames( context, "" );
            assertEquals( "Make sure can list subcontext names",
                          map.keySet(), names );
        }
        catch( final NamingException ne )
        {
            throw new AssertionFailedError( ne.toString() );
        }
    }

    /**
     * Create a subcontext, bind to it via the root context, and verify that:
     * <ul> <li>can list the binding names from the root context</li> <li>can
     * list the binding names from the subcontext</li> </ul>
     */
    public void testSubcontextBindAndList2() throws AssertionFailedError
    {
        Map map = new HashMap();
        map.put( "o1", O1 );
        map.put( "o2", O2 );
        map.put( "o3", O3 );
        Set names;

        try
        {
            m_context.createSubcontext( "x" );

            Iterator entries = map.entrySet().iterator();
            while( entries.hasNext() )
            {
                Map.Entry entry = ( Map.Entry ) entries.next();
                String name = "x/" + entry.getKey();
                m_context.bind( name, entry.getValue() );
            }
            names = listNames( m_context, "x" );
            assertEquals( "Make sure can list subcontext names from root",
                          map.keySet(), names );

            Context context = ( Context ) m_context.lookup( "x" );
            names = listNames( context, "" );
            assertEquals( "Make sure can list subcontext names",
                          map.keySet(), names );
        }
        catch( final NamingException ne )
        {
            throw new AssertionFailedError( ne.toString() );
        }
    }

    public void testSubcontextListBindings() throws AssertionFailedError
    {
        Map map = new HashMap();
        map.put( "o1", O1 );
        map.put( "o2", O2 );
        map.put( "o3", O3 );
        Set names = new HashSet();

        try
        {
            m_context.createSubcontext( "x" );
            Context context = ( Context ) m_context.lookup( "x" );
            Iterator entries = map.entrySet().iterator();
            while( entries.hasNext() )
            {
                Map.Entry entry = ( Map.Entry ) entries.next();
                context.bind( ( String ) entry.getKey(), entry.getValue() );
            }
            NamingEnumeration bindings = context.listBindings( "" );
            while( bindings.hasMore() )
            {
                Binding binding = ( Binding ) bindings.nextElement();
                String name = binding.getName();
                Object expected = map.get( name );
                if( expected == null )
                {
                    throw new AssertionFailedError( "Invalid binding: name="
                                                    + name
                                                    + ", classname="
                                                    + binding.getClassName()
                                                    + ", object="
                                                    + binding.getObject() );
                }
                assertEquals( expected, binding.getObject() );
                names.add( name );
            }

            try
            {
                bindings.nextElement();
                fail( "Expected nextElement() to throw NoSuchElementException" );
            }
            catch( final NoSuchElementException nsee )
            {
            }

            bindings.close();
        }
        catch( final NamingException ne )
        {
            throw new AssertionFailedError( ne.toString() );
        }

        assertEquals( map.keySet(), names );
    }

    /**
     * Bind a tree of subcontexts, and ensure they can be listed
     *
     * @throws AssertionFailedError
     */
    public void testRecursiveListBindings() throws AssertionFailedError
    {
        Map expected = new HashMap();
        expected.put( "o1", O1 );
        expected.put( "o2", O2 );
        expected.put( "x/o3", O3 );
        expected.put( "x/o4", O4 );
        expected.put( "x/y/o5", O5 );
        Map result = new HashMap();

        try
        {
            m_context.bind( "o1", O1 );
            m_context.bind( "o2", O2 );
            m_context.createSubcontext( "x" );
            Context context = ( Context ) m_context.lookup( "x" );
            context.bind( "o3", O3 );
            context.bind( "o4", O4 );
            m_context.createSubcontext( "x/y" );
            context = ( Context ) m_context.lookup( "x/y" );
            context.bind( "o5", O5 );

            listRecursive( "", m_context, result );
        }
        catch( final NamingException ne )
        {
            throw new AssertionFailedError( ne.toString() );
        }

        assertEquals( expected, result );
    }

    /**
     * Tests the {@link Context#addToEnvironment(String, Object)} and {@link
     * Context#removeFromEnvironment(String)} methods.
     *
     * @throws AssertionFailedError if the test fails
     */
    public void testEnvironment() throws AssertionFailedError
    {
        final String key = "key";
        Object value = null;
        Object previous = null;

        try
        {
            value = m_context.getEnvironment().get( key );
            assertNull( value );

            previous = m_context.addToEnvironment( key, O1 );
            assertNull( previous );

            previous = m_context.addToEnvironment( key, O2 );
            assertEquals( O1, previous );

            previous = m_context.removeFromEnvironment( key );
            assertEquals( O2, previous );

            previous = m_context.removeFromEnvironment( key );
            assertNull( previous );
        }
        catch( final NamingException ne )
        {
            throw new AssertionFailedError( ne.toString() );
        }
    }

    public void testComposeName() throws AssertionFailedError
    {
        try
        {
            assertEquals( "a", m_context.composeName( "", "a" ) );
            assertEquals( "a/b", m_context.composeName( "b", "a" ) );
            assertEquals( "a/b/c", m_context.composeName( "c", "a/b" ) );
            assertEquals( "a/b/c", m_context.composeName( "b/c", "a" ) );
        }
        catch( final NamingException ne )
        {
            throw new AssertionFailedError( ne.toString() );
        }
    }

    /**
     * Bind a {@link Referenceable}, and ensure that its reference is used.
     *
     * @throws AssertionFailedError if the test fails
     */
    public void testReferenceable()
        throws AssertionFailedError
    {
        try
        {
            TestDataReferenceable initial = new TestDataReferenceable(
                "value1" );

            m_context.bind( "o1", initial );
            initial.setValue( "value2" );

            Object obj = m_context.lookup( "o1" );
            assertTrue( obj instanceof TestDataReferenceable );
            assertEquals( "value1", ((TestData) obj).getValue() );

            // ensure listBindings resolves the reference
            final NamingEnumeration bindings = m_context.listBindings( "" );
            assertTrue( bindings.hasMore() );
            Binding binding = ( Binding ) bindings.nextElement();
            obj = binding.getObject();
            assertTrue( obj instanceof TestDataReferenceable );
            assertEquals( "value1", ( ( TestData ) obj ).getValue() );
            assertFalse( bindings.hasMore() );
        }
        catch( final NamingException ne )
        {
            throw new AssertionFailedError( ne.toString() );
        }
    }

    /**
     * Bind a {@link Reference} and ensure that the class it refers to is
     * returned on subsequent lookup.
     *
     * @throws AssertionFailedError if the test fails
     */
    public void testReference()
        throws AssertionFailedError
    {
        final String expected = "foo";
        try
        {
            TestDataReferenceable value = new TestDataReferenceable( expected );
            Reference reference = value.getReference();
            m_context.bind( "o2", reference );

            Object obj = m_context.lookup( "o2" );
            assertTrue( obj instanceof TestDataReferenceable );
            assertEquals( expected, ( ( TestData ) obj ).getValue() );

            // ensure listBindings resolves the reference
            final NamingEnumeration bindings = m_context.listBindings( "" );
            assertTrue( bindings.hasMore() );
            Binding binding = ( Binding ) bindings.nextElement();
            obj = binding.getObject();
            assertTrue( obj instanceof TestDataReferenceable );
            assertEquals( expected, ( ( TestData ) obj ).getValue() );
            assertFalse( bindings.hasMore() );
        }
        catch( final NamingException ne )
        {
            throw new AssertionFailedError( ne.toString() );
        }
    }

    /**
     * Bind a {@link TestData} and ensure that {@link TestStateFactory} is
     * invoked to convert it to a {@link Reference}.
     *
     * @throws AssertionFailedError if the test fails.
     */
    public void testStateFactory() throws AssertionFailedError
    {
        final String expected = "bar";
        try
        {
            TestData value = new TestData( expected );
            m_context.bind( "o3", value );


            Object obj = m_context.lookup( "o3" );
            assertTrue( obj instanceof TestDataReferenceable );

            TestDataReferenceable current = ( TestDataReferenceable ) obj;
            assertEquals( expected, current.getValue() );
        }
        catch( final NamingException ne )
        {
            throw new AssertionFailedError( ne.toString() );
        }
    }

    /**
     * Verify that {@link NamingEnumeration#next()} throws {@link
     * NamingException} when a reference can't be resolved.
     *
     * @throws AssertionFailedError if the test fails
     */
    public void testNextForBadReference()
        throws AssertionFailedError
    {
        try
        {
            ExceptionReferenceable trigger = new ExceptionReferenceable();
            m_context.bind( "o4", trigger );

            NamingEnumeration enum = m_context.listBindings( "" );
            assertTrue( enum.hasMore() );
            try
            {
                Object result = enum.next();
                fail( "Expected nextElement to throw NamingExceptionn, but returned "
                      + result );
            }
            catch( final NamingException expected )
            {
            }
        }
        catch( final NamingException ne )
        {
            throw new AssertionFailedError( ne.toString() );
        }
    }

    /**
     * Verify that {@link NamingEnumeration#nextElement()} throws {@link
     * NoSuchElementException} when a reference can't be resolved.
     *
     * @throws AssertionFailedError if the test fails
     */
    public void testNextElementForBadReference()
        throws AssertionFailedError
    {
        try
        {
            ExceptionReferenceable trigger = new ExceptionReferenceable();
            m_context.bind( "o5", trigger );

            NamingEnumeration enum = m_context.listBindings( "" );
            assertTrue( enum.hasMoreElements() );
            try
            {
                Object result = enum.nextElement();
                fail( "Expected nextElement to throw NoSuchElementException, but returned "
                      + result );
            }
            catch( NoSuchElementException expected )
            {
            }
        }
        catch( final NamingException ne )
        {
            throw new AssertionFailedError( ne.toString() );
        }
    }

    protected void setUp() throws Exception
    {
        m_root = getRoot();
        m_context = m_root.createSubcontext( "test" + c_id++ );
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

    protected abstract Context getRoot() throws Exception;


    private Set listNames( Context context, String name )
        throws NamingException
    {
        Set result = new HashSet();
        NamingEnumeration names = context.list( name );
        while( names.hasMore() )
        {
            NameClassPair pair = ( NameClassPair ) names.next();
            result.add( pair.getName() );
        }
        names.close();
        return result;
    }

    private void listRecursive( String name, Context context, Map result )
        throws NamingException
    {
        NamingEnumeration bindings = context.listBindings( "" );
        while( bindings.hasMore() )
        {
            Binding binding = ( Binding ) bindings.next();
            Object object = binding.getObject();
            String subName = ( name.length() == 0 ) ? binding.getName() :
                name + "/" + binding.getName();
            if( object instanceof Context )
            {
                listRecursive( subName, ( Context ) object, result );
            }
            else
            {
                result.put( subName, object );
            }
        }
        bindings.close();
    }

}
