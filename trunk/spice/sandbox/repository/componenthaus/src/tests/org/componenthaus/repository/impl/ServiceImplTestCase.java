package org.componenthaus.repository.impl;

import junit.framework.TestCase;

import java.util.List;
import java.util.ArrayList;

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

    public void testCanGetSetVersion() {
        String version = "1234123";
        assertNull(impl.getVersion());
        impl.setVersion(version);
        assertSame(version,impl.getVersion());
    }

    public void testCanGetSetAuthors() {
        assertEquals(0,impl.getAuthors().size());
        List newAuthors = new ArrayList();
        final String authorName = "An author";
        newAuthors.add(authorName);
        impl.setAuthors(newAuthors);
        assertEquals(1,impl.getAuthors().size());
        assertTrue(impl.getAuthors().contains(authorName));
    }

    public void testCanGetSetOneLineDescription() {
        assertNull(impl.getOneLineDescription());
        String desc = "elkjlkjlkjlkj";
        impl.setOneLineDescription(desc);
        assertSame(desc, impl.getOneLineDescription());
    }

    public void testCanGetSetFullDescription() {
        assertNull(impl.getFullDescription());
        String desc = "elkjlkjlkjlkj";
        impl.setFullDescription(desc);
        assertSame(desc, impl.getFullDescription());
    }

    public void testCanGetSetFulllyQualifiedName() {
        assertNull(impl.getFullyQualifiedName());
        String name = "elkjlkjlkjlkj";
        impl.setFullyQualifiedName(name);
        assertSame(name, impl.getFullyQualifiedName());
    }
}
