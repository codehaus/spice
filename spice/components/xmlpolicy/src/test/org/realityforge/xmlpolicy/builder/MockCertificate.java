/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.xmlpolicy.builder;

import java.security.Certificate;
import java.security.Principal;
import java.security.PublicKey;
import java.security.KeyException;
import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-06-05 09:40:31 $
 */
class MockCertificate
    implements Certificate
{
    public Principal getGuarantor()
    {
        return null;
    }

    public Principal getPrincipal()
    {
        return null;
    }

    public PublicKey getPublicKey()
    {
        return null;
    }

    public void encode( OutputStream stream )
        throws KeyException, IOException
    {
    }

    public void decode( InputStream stream )
        throws KeyException, IOException
    {
    }

    public String getFormat()
    {
        return null;
    }

    public String toString( boolean detailed )
    {
        return null;
    }
}
