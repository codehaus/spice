/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.alchemist.activity;

import org.codehaus.dna.Active;
import junit.framework.TestCase;

public class ActivityAlchemistTestCase
    extends TestCase
{

    public void testIsAvalonInitializable()
    {
        MockAvalonInitializable avalonInitializable = new MockAvalonInitializable();
        MockDNAActive dnaActive = new MockDNAActive();
        assertTrue("Avalon Initializable", ActivityAlchemist.isAvalonInitializable(avalonInitializable));
        assertTrue("!Avalon Initializable", !ActivityAlchemist.isAvalonInitializable(dnaActive));
    }

    public void testToAvalonInitializable()
    {
        MockAvalonInitializable avalonInitializable = new MockAvalonInitializable();
        MockDNAActive dnaActive = new MockDNAActive();
        assertTrue("Avalon Initializable", 
                (ActivityAlchemist.toAvalonInitializable(avalonInitializable) 
                 instanceof org.apache.avalon.framework.activity.Initializable ));
        try {
            ActivityAlchemist.toAvalonInitializable( null );
            fail("Expected IllegalArgumentException");
        } catch ( IllegalArgumentException e ) {
            final String message = "Object is not Avalon Initializable";
            assertEquals( "IAE message", message, e.getMessage() );
        }
        try {
            ActivityAlchemist.toAvalonInitializable( dnaActive );
            fail("Expected IllegalArgumentException");
        } catch ( IllegalArgumentException e ) {
            final String message = dnaActive.getClass().getName()+ " is not Avalon Initializable";
            assertEquals( "IAE message", message, e.getMessage() );
        }
    }
    
    public void testIsAvalonDisposable()
    {
        MockAvalonDisposable avalonDisposable = new MockAvalonDisposable();
        MockDNAActive dnaActive = new MockDNAActive();
        assertTrue("Avalon Disposable", ActivityAlchemist.isAvalonDisposable(avalonDisposable));
        assertTrue("!Avalon Disposable", !ActivityAlchemist.isAvalonDisposable(dnaActive));
    }

    public void testToAvalonDisposable()
    {
        MockAvalonDisposable avalonDisposable = new MockAvalonDisposable();
        MockDNAActive dnaActive = new MockDNAActive();
        assertTrue("Avalon Disposable", 
                (ActivityAlchemist.toAvalonDisposable(avalonDisposable) 
                 instanceof org.apache.avalon.framework.activity.Disposable ));
        try {
            ActivityAlchemist.toAvalonDisposable( null );
            fail("Expected IllegalArgumentException");
        } catch ( IllegalArgumentException e ) {
            final String message = "Object is not Avalon Disposable";
            assertEquals( "IAE message", message, e.getMessage() );
        }
        try {
            ActivityAlchemist.toAvalonDisposable( dnaActive );
            fail("Expected IllegalArgumentException");
        } catch ( IllegalArgumentException e ) {
            final String message = dnaActive.getClass().getName()+ " is not Avalon Disposable";
            assertEquals( "IAE message", message, e.getMessage() );
        }
    }
    
    public void testIsDNAActive()
    {
        MockAvalonInitializable avalonInitializable = new MockAvalonInitializable();
        MockDNAActive dnaActive = new MockDNAActive();
        assertTrue("DNA Initializable", ActivityAlchemist.isDNAActive(dnaActive));
        assertTrue("!DNA Initializable", !ActivityAlchemist.isDNAActive(avalonInitializable));
    }

    public void testToDNAActive()
    {
        MockAvalonInitializable avalonInitializable = new MockAvalonInitializable();
        MockDNAActive dnaActive = new MockDNAActive();
        assertTrue("DNA Active", 
                (ActivityAlchemist.toDNAActive(dnaActive) instanceof Active ));
        try {
            ActivityAlchemist.toDNAActive( null );
            fail("Expected IllegalArgumentException");
        } catch ( IllegalArgumentException e ) {
            final String message = "Object is not DNA Active";
            assertEquals( "IAE message", message, e.getMessage() );
        }
        try {
            ActivityAlchemist.toDNAActive( avalonInitializable );
            fail("Expected IllegalArgumentException");
        } catch ( IllegalArgumentException e ) {
            final String message = avalonInitializable.getClass().getName()+ " is not DNA Active";
            assertEquals( "IAE message", message, e.getMessage() );
        }
    }
}