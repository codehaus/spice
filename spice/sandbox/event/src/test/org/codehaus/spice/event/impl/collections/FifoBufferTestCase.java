/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.event.impl.collections;

import java.util.List;
import junit.framework.TestCase;

/**
 * @author Peter Donald
 * @version $Revision: 1.2 $ $Date: 2004-04-19 08:09:52 $
 */
public class FifoBufferTestCase
    extends TestCase
{
    public void testNegativeSizedBuffer() throws Exception
    {
        try
        {
            new BoundedFifoBuffer( -1 );
        }
        catch( final IllegalArgumentException iae )
        {
            assertEquals( "iae.getMessage()", "size <= 0", iae.getMessage() );
            return;
        }
        fail( "Exected to fail due to negative buffer size " );
    }

    public void testCreate() throws Exception
    {
        final AbstractFifoBuffer buffer = new BoundedFifoBuffer( 3 );
        assertEquals( "buffer.size()", 0, buffer.size() );
        assertEquals( "buffer.m_buffer.length", 3, buffer.m_buffer.length );
        assertEquals( "buffer.m_head", 0, buffer.m_head );
        assertEquals( "buffer.m_tail", 0, buffer.m_tail );
        assertEquals( "buffer.m_isWrappedBuffer",
                      false,
                      buffer.m_isWrappedBuffer );
    }

    public void testSimpleAdd() throws Exception
    {
        final AbstractFifoBuffer buffer = new BoundedFifoBuffer( 3 );
        final Object object = new Object();
        assertEquals( "buffer.add( object )", true, buffer.add( object ) );
        assertEquals( "buffer.size()", 1, buffer.size() );
        assertEquals( "buffer.m_head", 0, buffer.m_head );
        assertEquals( "buffer.m_tail", 1, buffer.m_tail );
        assertEquals( "buffer.m_isWrappedBuffer",
                      false,
                      buffer.m_isWrappedBuffer );
        assertEquals( "buffer.m_buffer[ 0 ]", object, buffer.m_buffer[ 0 ] );
        assertEquals( "buffer.add( object )", true, buffer.add( object ) );
        assertEquals( "buffer.size()", 2, buffer.size() );
        assertEquals( "buffer.m_head", 0, buffer.m_head );
        assertEquals( "buffer.m_tail", 2, buffer.m_tail );
        assertEquals( "buffer.m_isWrappedBuffer",
                      false,
                      buffer.m_isWrappedBuffer );
        assertEquals( "buffer.m_buffer[ 1 ]", object, buffer.m_buffer[ 1 ] );
    }

    public void testAddWithWrap() throws Exception
    {
        final AbstractFifoBuffer buffer = new BoundedFifoBuffer( 3 );
        final Object object1 = new Object();
        final Object object2 = new Object();
        final Object object3 = new Object();
        final Object object4 = new Object();

        assertEquals( "buffer.add( object1 )", true, buffer.add( object1 ) );
        assertEquals( "buffer.add( object2 )", true, buffer.add( object2 ) );
        assertEquals( "buffer.add( object3 )", true, buffer.add( object3 ) );
        assertEquals( "buffer.size()", 3, buffer.size() );
        assertEquals( "buffer.m_head", 0, buffer.m_head );
        assertEquals( "buffer.m_tail", 0, buffer.m_tail );
        assertEquals( "buffer.m_isWrappedBuffer",
                      true,
                      buffer.m_isWrappedBuffer );

        assertEquals( "buffer.peek()", object1, buffer.peek() );
        assertEquals( "buffer.size() After Peek", 3, buffer.size() );
        assertEquals( "buffer.m_head After Peek", 0, buffer.m_head );
        assertEquals( "buffer.m_tail After Peek", 0, buffer.m_tail );
        assertEquals( "buffer.m_isWrappedBuffer After Peek",
                      true,
                      buffer.m_isWrappedBuffer );

        assertEquals( "buffer.pop()", object1, buffer.pop() );
        assertEquals( "buffer.size() After Pop", 2, buffer.size() );
        assertEquals( "buffer.m_head After Pop", 1, buffer.m_head );
        assertEquals( "buffer.m_tail After Pop", 0, buffer.m_tail );
        assertEquals( "buffer.m_isWrappedBuffer After Pop",
                      true,
                      buffer.m_isWrappedBuffer );

        assertEquals( "buffer.add( object4 )", true, buffer.add( object4 ) );
        assertEquals( "buffer.size() After Add", 3, buffer.size() );
        assertEquals( "buffer.m_head After Add", 1, buffer.m_head );
        assertEquals( "buffer.m_tail After Add", 1, buffer.m_tail );
        assertEquals( "buffer.m_isWrappedBuffer After Add",
                      true,
                      buffer.m_isWrappedBuffer );
        assertEquals( "buffer.m_buffer[ 0 ]", object4, buffer.m_buffer[ 0 ] );
        assertEquals( "buffer.m_buffer[ 1 ]", object2, buffer.m_buffer[ 1 ] );
        assertEquals( "buffer.m_buffer[ 2 ]", object3, buffer.m_buffer[ 2 ] );
    }

    public void testSimpleAddAll() throws Exception
    {
        final AbstractFifoBuffer buffer = new BoundedFifoBuffer( 3 );
        final Object object = new Object();
        final Object[] objects = new Object[]{object};
        assertEquals( "buffer.addAll( objects )",
                      true,
                      buffer.addAll( objects ) );
        assertEquals( "buffer.size()", 1, buffer.size() );
        assertEquals( "buffer.m_head", 0, buffer.m_head );
        assertEquals( "buffer.m_tail", 1, buffer.m_tail );
        assertEquals( "buffer.m_isWrappedBuffer",
                      false,
                      buffer.m_isWrappedBuffer );
        assertEquals( "buffer.m_buffer[ 0 ]", object, buffer.m_buffer[ 0 ] );
    }

    public void testFillingAddAll() throws Exception
    {
        final AbstractFifoBuffer buffer = new BoundedFifoBuffer( 3 );
        final Object object1 = new Object();
        final Object object2 = new Object();
        final Object object3 = new Object();
        final Object[] objects = new Object[]{object1, object2, object3};
        assertEquals( "buffer.addAll( objects )",
                      true,
                      buffer.addAll( objects ) );
        assertEquals( "buffer.size()", 3, buffer.size() );
        assertEquals( "buffer.m_head", 0, buffer.m_head );
        assertEquals( "buffer.m_tail", 0, buffer.m_tail );
        assertEquals( "buffer.m_isWrappedBuffer",
                      true,
                      buffer.m_isWrappedBuffer );
        assertEquals( "buffer.m_buffer[ 0 ]", object1, buffer.m_buffer[ 0 ] );
        assertEquals( "buffer.m_buffer[ 1 ]", object2, buffer.m_buffer[ 1 ] );
        assertEquals( "buffer.m_buffer[ 2 ]", object3, buffer.m_buffer[ 2 ] );
    }

    public void testAddAllWithWrap() throws Exception
    {
        final AbstractFifoBuffer buffer = new BoundedFifoBuffer( 3 );
        final Object object1 = new Object();
        final Object object2 = new Object();
        final Object object3 = new Object();
        final Object object4 = new Object();

        assertEquals( "buffer.add( object1 )", true, buffer.add( object1 ) );
        assertEquals( "buffer.add( object2 )", true, buffer.add( object2 ) );
        assertEquals( "buffer.add( object3 )", true, buffer.add( object3 ) );
        assertEquals( "buffer.size()", 3, buffer.size() );
        assertEquals( "buffer.m_head", 0, buffer.m_head );
        assertEquals( "buffer.m_tail", 0, buffer.m_tail );
        assertEquals( "buffer.m_isWrappedBuffer",
                      true,
                      buffer.m_isWrappedBuffer );

        assertEquals( "buffer.peek()", object1, buffer.peek() );
        assertEquals( "buffer.size() After Peek", 3, buffer.size() );
        assertEquals( "buffer.m_head After Peek", 0, buffer.m_head );
        assertEquals( "buffer.m_tail After Peek", 0, buffer.m_tail );
        assertEquals( "buffer.m_isWrappedBuffer After Peek",
                      true,
                      buffer.m_isWrappedBuffer );

        assertEquals( "buffer.pop()", object1, buffer.pop() );
        assertEquals( "buffer.size() After Pop", 2, buffer.size() );
        assertEquals( "buffer.m_head After Pop", 1, buffer.m_head );
        assertEquals( "buffer.m_tail After Pop", 0, buffer.m_tail );
        assertEquals( "buffer.m_isWrappedBuffer After Pop",
                      true,
                      buffer.m_isWrappedBuffer );

        assertEquals( "buffer.addAll( new Object[]{object4} )",
                      true,
                      buffer.addAll( new Object[]{object4} ) );
        assertEquals( "buffer.size() After Add", 3, buffer.size() );
        assertEquals( "buffer.m_head After Add", 1, buffer.m_head );
        assertEquals( "buffer.m_tail After Add", 1, buffer.m_tail );
        assertEquals( "buffer.m_isWrappedBuffer After Add",
                      true,
                      buffer.m_isWrappedBuffer );

        assertEquals( "buffer.m_buffer[ 0 ]", object4, buffer.m_buffer[ 0 ] );
        assertEquals( "buffer.m_buffer[ 1 ]", object2, buffer.m_buffer[ 1 ] );
        assertEquals( "buffer.m_buffer[ 2 ]", object3, buffer.m_buffer[ 2 ] );
    }

    public void testObjectsPassedIntoAddAll() throws Exception
    {
        final AbstractFifoBuffer buffer = new BoundedFifoBuffer( 3 );
        try
        {
            buffer.addAll( null );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "npe.getMessage()", "objects", npe.getMessage() );
            return;
        }
        fail( "Expected a NPE when passing objects into AddAll" );
    }

    public void testObjectPassedIntoAdd() throws Exception
    {
        final AbstractFifoBuffer buffer = new BoundedFifoBuffer( 3 );
        try
        {
            buffer.add( null );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "npe.getMessage()", "object", npe.getMessage() );
            return;
        }
        fail( "Expected a NPE when passing object into Add" );
    }

    public void testPopOnEmpty() throws Exception
    {
        final AbstractFifoBuffer buffer = new BoundedFifoBuffer( 3 );
        final Object element = buffer.pop();
        assertEquals( "element", null, element );
    }

    public void testToList()
    {
        final AbstractFifoBuffer buffer = new BoundedFifoBuffer( 3 );
        final Object object1 = new Object();
        final Object object2 = new Object();
        final Object object3 = new Object();
        buffer.add( object1 );
        buffer.add( object2 );
        buffer.add( object3 );

        final List list = buffer.toList();

        assertEquals( "list.size()", 3, list.size() );
        assertEquals( "list.get(0)", object1, list.get( 0 ) );
        assertEquals( "list.get(0)", object2, list.get( 1 ) );
        assertEquals( "list.get(0)", object3, list.get( 2 ) );
    }

    public void testPopAndWrap() throws Exception
    {
        final AbstractFifoBuffer buffer = new BoundedFifoBuffer( 3 );
        final Object object1 = new Object();
        final Object object2 = new Object();
        final Object object3 = new Object();
        final Object object4 = new Object();
        buffer.add( object1 );
        buffer.add( object2 );
        buffer.add( object3 );
        final Object element1 = buffer.pop();
        final Object element2 = buffer.pop();
        final Object element3 = buffer.pop();
        buffer.add( object4 );
        final Object element4 = buffer.pop();

        assertEquals( "element1", object1, element1 );
        assertEquals( "element2", object2, element2 );
        assertEquals( "element3", object3, element3 );
        assertEquals( "element4", object4, element4 );
    }

    public void testOverAddOnBoundedBuffer() throws Exception
    {
        final AbstractFifoBuffer buffer = new BoundedFifoBuffer( 3 );
        final Object object1 = new Object();
        final Object object2 = new Object();
        final Object object3 = new Object();
        final Object object4 = new Object();

        assertEquals( "buffer.add( object1 )", true, buffer.add( object1 ) );
        assertEquals( "buffer.add( object2 )", true, buffer.add( object2 ) );
        assertEquals( "buffer.add( object3 )", true, buffer.add( object3 ) );
        assertEquals( "buffer.add( object4 )", false, buffer.add( object4 ) );
    }

    public void testOverAddAllOnBoundedBuffer() throws Exception
    {
        final AbstractFifoBuffer buffer = new BoundedFifoBuffer( 3 );
        final Object object1 = new Object();
        final Object object2 = new Object();
        final Object object3 = new Object();
        final Object object4 = new Object();
        final Object[] objects = new Object[]{object1,
                                              object2,
                                              object3,
                                              object4};

        assertEquals( "buffer.addAll( objects )",
                      false,
                      buffer.addAll( objects ) );
    }

    public void testOverAddAllOnUnBoundedBuffer() throws Exception
    {
        final AbstractFifoBuffer buffer = new UnboundedFifoBuffer( 3 );
        final Object object1 = new Object();
        final Object object2 = new Object();
        final Object object3 = new Object();
        final Object object4 = new Object();
        final Object[] objects = new Object[]{object1,
                                              object2,
                                              object3,
                                              object4};

        assertEquals( "buffer.addAll( objects )",
                      true,
                      buffer.addAll( objects ) );
        assertEquals( "buffer.m_buffer.length", 5, buffer.m_buffer.length );
        assertEquals( "buffer.m_buffer[ 0 ]", object1, buffer.m_buffer[ 0 ] );
        assertEquals( "buffer.m_buffer[ 1 ]", object2, buffer.m_buffer[ 1 ] );
        assertEquals( "buffer.m_buffer[ 2 ]", object3, buffer.m_buffer[ 2 ] );
        assertEquals( "buffer.m_buffer[ 3 ]", object4, buffer.m_buffer[ 3 ] );
        assertEquals( "buffer.m_buffer[ 4 ]", null, buffer.m_buffer[ 4 ] );
    }

    public void testOverAddAllOnUnBoundedBufferWithNonZeroHead()
        throws Exception
    {
        final AbstractFifoBuffer buffer = new UnboundedFifoBuffer( 3 );
        final Object start = new Object();
        assertEquals( "buffer.add( start )", true, buffer.add( start ) );
        assertEquals( "buffer.pop()", start, buffer.pop() );

        final Object object1 = new Object();
        final Object object2 = new Object();
        final Object object3 = new Object();
        final Object object4 = new Object();
        assertEquals( "buffer.add( object1 )", true, buffer.add( object1 ) );
        assertEquals( "buffer.add( object2 )", true, buffer.add( object2 ) );
        assertEquals( "buffer.add( object3 )", true, buffer.add( object3 ) );
        assertEquals( "buffer.add( object4 )", true, buffer.add( object4 ) );

        assertEquals( "buffer.m_buffer.length", 5, buffer.m_buffer.length );
        assertEquals( "buffer.m_buffer[ 0 ]", object1, buffer.m_buffer[ 0 ] );
        assertEquals( "buffer.m_buffer[ 1 ]", object2, buffer.m_buffer[ 1 ] );
        assertEquals( "buffer.m_buffer[ 2 ]", object3, buffer.m_buffer[ 2 ] );
        assertEquals( "buffer.m_buffer[ 3 ]", object4, buffer.m_buffer[ 3 ] );
        assertEquals( "buffer.m_buffer[ 4 ]", null, buffer.m_buffer[ 4 ] );
    }
}
