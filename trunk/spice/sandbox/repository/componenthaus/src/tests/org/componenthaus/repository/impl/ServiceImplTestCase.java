package org.componenthaus.repository.impl;

import junit.framework.TestCase;

public class ServiceImplTestCase extends TestCase {
    private ServiceImpl impl = null;

    protected void setUp() throws Exception {
        impl = new ServiceImpl();
    }

    public void testDoesNotEqualAPlainObject() {
        assertFalse(impl.equals(new Object()));
    }

    public void testDoesNotEqualNull() {
        assertFalse(impl.equals(null));
    }

    public void testEqualsSameInstance() {
        assertTrue(impl.equals(impl));
    }

    public void testEqualsSimilarEmptyInstance() {
        assertTrue(impl.equals(new ServiceImpl()));
    }

    public void testEqualSimilarNonEmptyInstance() {
        impl.setName("name");
        ServiceImpl secondImpl = new ServiceImpl();
        secondImpl.setName("name");
        assertEquals(impl, secondImpl);
    }
}
