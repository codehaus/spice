/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.xmlpolicy.metadata;

/**
 * This class defines a keystore that is used when locating
 * signers of a codebase.
 *
 * @author <a href="mailto:peter at apache.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-04-16 11:45:59 $
 */
public class GrantMetaData
{
    /**
     * The codebase that grant applies to.
     */
    private final String m_codebase;

    /**
     * The signer of codebase. May be null but if null then
     * keyStore must also be null.
     */
    private final String m_signedBy;

    /**
     * The keyStore to load signer from. May be null but if
     * null then signedBy must also be null.
     */
    private final String m_keyStore;

    /**
     * The set of permissions to grant codebase.
     */
    private final PermissionMetaData[] m_permissions;

    /**
     * Construct a grant.
     *
     * @param codebase the codebase grant is about
     * @param signedBy who signed the codebase
     * @param keyStore the name of the keystore the signer is loaded from
     * @param permissions the set of permissions associated with grant
     */
    public GrantMetaData( final String codebase,
                          final String signedBy,
                          final String keyStore,
                          final PermissionMetaData[] permissions )
    {
        if( null == permissions )
        {
            throw new NullPointerException( "permissions" );
        }
        if( null == signedBy && null != keyStore )
        {
            throw new NullPointerException( "signedBy" );
        }
        if( null == keyStore && null != signedBy )
        {
            throw new NullPointerException( "keyStore" );
        }

        m_codebase = codebase;
        m_signedBy = signedBy;
        m_keyStore = keyStore;
        m_permissions = permissions;
    }

    /**
     * Return the code base for grant.
     *
     * @return the code base for grant.
     */
    public String getCodebase()
    {
        return m_codebase;
    }

    /**
     * Return the signer for grant.
     *
     * @return the signer for grant.
     */
    public String getSignedBy()
    {
        return m_signedBy;
    }

    /**
     * Return the key store to load signer from.
     *
     * @return the key store to load signer from.
     */
    public String getKeyStore()
    {
        return m_keyStore;
    }

    /**
     * Return the set of permissions associated with grant.
     *
     * @return the set of permissions associated with grant.
     */
    public PermissionMetaData[] getPermissions()
    {
        return m_permissions;
    }
}
