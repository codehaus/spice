/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.xmlpolicy.metadata;

/**
 * This class defines a keystore that is used when locating
 * signers of a codebase.
 *
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2003-12-02 09:16:06 $
 */
public class PermissionMetaData
{
    /**
     * The class name of permission.
     */
    private final String m_classname;

    /**
     * The target of permission. The interpretation of this is
     * determined by underlying permission classname.
     */
    private final String m_target;

    /**
     * The action(s) associated with permission.
     * The interpretation of this field is relative to
     * the permission and target.
     */
    private final String m_action;

    /**
     * The signer of the permission.
     * (ie who signed the permission class).
     */
    private final String m_signedBy;

    /**
     * The keyStore to load signer from. May be null but if
     * null then signedBy must also be null.
     */
    private final String m_keyStore;

    /**
     * Construct the permission meta data.
     *
     * @param classname the name of permission class
     * @param target the target of permission (may be null)
     * @param action the action of permission (may be null)
     */
    public PermissionMetaData( final String classname,
                               final String target,
                               final String action,
                               final String signedBy,
                               final String keyStore )
    {
        if( null == classname )
        {
            throw new NullPointerException( "classname" );
        }
        if( null == signedBy && null != keyStore )
        {
            throw new NullPointerException( "signedBy" );
        }
        if( null == keyStore && null != signedBy )
        {
            throw new NullPointerException( "keyStore" );
        }

        m_classname = classname;
        m_target = target;
        m_action = action;
        m_signedBy = signedBy;
        m_keyStore = keyStore;
    }

    /**
     * Return the name of permission class.
     *
     * @return the name of permission class.
     */
    public String getClassname()
    {
        return m_classname;
    }

    /**
     * Return the action of permission (may be null).
     *
     * @return the action of permission (may be null).
     */
    public String getTarget()
    {
        return m_target;
    }

    /**
     * Return the action of permission (may be null).
     *
     * @return the action of permission (may be null)
     */
    public String getAction()
    {
        return m_action;
    }

    /**
     * Return the principle name who signed the permission.
     *
     * @return the the principle name who signed the permission.
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
}
