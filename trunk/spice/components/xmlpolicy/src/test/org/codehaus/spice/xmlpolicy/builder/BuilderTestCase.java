/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.xmlpolicy.builder;

import java.net.URL;
import java.security.AllPermission;
import java.security.CodeSource;
import java.security.PermissionCollection;
import java.security.Policy;
import java.security.UnresolvedPermission;
import java.security.cert.Certificate;
import java.util.Enumeration;
import java.io.FilePermission;
import junit.framework.TestCase;
import org.codehaus.spice.xmlpolicy.metadata.GrantMetaData;
import org.codehaus.spice.xmlpolicy.metadata.KeyStoreMetaData;
import org.codehaus.spice.xmlpolicy.metadata.PermissionMetaData;
import org.codehaus.spice.xmlpolicy.metadata.PolicyMetaData;

/**
 * TestCase for Builder package.
 *
 * @author Peter Donald
 */
public class BuilderTestCase
    extends TestCase
{
    public BuilderTestCase( final String name )
    {
        super( name );
    }

    public void testEmptyMetaData()
        throws Exception
    {
        final PolicyBuilder builder = new PolicyBuilder();
        final PolicyMetaData metaData =
            new PolicyMetaData( new KeyStoreMetaData[ 0 ], new GrantMetaData[ 0 ] );
        final TestResolver resolver = new TestResolver();
        try
        {
            final Policy policy = builder.buildPolicy( metaData, resolver );
            final PermissionCollection permissions =
                policy.getPermissions( new CodeSource( null, new Certificate[ 0 ] ) );
            assertEquals( "Expect no permissions for empty metaData",
                          false,
                          permissions.elements().hasMoreElements() );
        }
        catch( final Exception e )
        {
            fail( "Expected to be able to build Policy with empty metadata" );
        }
    }

    public void testNullResolverInBuildPolicy()
        throws Exception
    {
        final PolicyBuilder builder = new PolicyBuilder();
        final PolicyMetaData metaData =
            new PolicyMetaData( new KeyStoreMetaData[ 0 ], new GrantMetaData[ 0 ] );
        try
        {
            builder.buildPolicy( metaData, null );
            fail( "Expected to fail due to null pointer in buildPolicy" );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "NPE message",
                          "resolver",
                          npe.getMessage() );
        }
    }

    public void testNullMetaDataInBuildPolicy()
        throws Exception
    {
        final PolicyBuilder builder = new PolicyBuilder();
        try
        {
            builder.buildPolicy( null, new TestResolver() );
            fail( "Expected to fail due to null pointer in buildPolicy" );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "NPE message",
                          "policy",
                          npe.getMessage() );
        }

    }

    public void testMetaDataWithAPermission()
        throws Exception
    {
        final PolicyBuilder builder = new PolicyBuilder();
        final PermissionMetaData permission =
            new PermissionMetaData( AllPermission.class.getName(), null, null, null, null );
        final GrantMetaData grant =
            new GrantMetaData( "file:/", null, null,
                               new PermissionMetaData[]{permission} );
        final PolicyMetaData metaData =
            new PolicyMetaData( new KeyStoreMetaData[ 0 ], new GrantMetaData[]{grant} );
        final TestResolver resolver = new TestResolver();
        try
        {
            final Policy policy = builder.buildPolicy( metaData, resolver );
            final CodeSource codesource =
                new CodeSource( new URL( "file:/" ), new Certificate[ 0 ] );
            final PermissionCollection permissions = policy.getPermissions( codesource );
            final Enumeration enumeration = permissions.elements();
            while( enumeration.hasMoreElements() )
            {
                final Object perm = enumeration.nextElement();
                if( perm instanceof AllPermission )
                {
                    return;
                }
            }
            fail( "Expected to get permission set with ALlPermission contained" );
        }
        catch( final Exception e )
        {
            fail( "Expected to be able to build Policy with empty metadata" );
        }
    }

    public void testMetaDataWithAPermissionAndCerts()
        throws Exception
    {
        final PolicyBuilder builder = new TestPolicyBuilder();
        final PermissionMetaData permission =
            new PermissionMetaData( FilePermission.class.getName(), "/", "read",
                                    null, null );
        final GrantMetaData grant =
            new GrantMetaData( "file:/", "jenny", "default",
                               new PermissionMetaData[]{permission} );
        final KeyStoreMetaData keyStore =
            new KeyStoreMetaData( "default", "http://spice.sourceforge.net", "DoDgY" );
        final PolicyMetaData metaData =
            new PolicyMetaData( new KeyStoreMetaData[]{keyStore}, new GrantMetaData[]{grant} );
        final TestResolver resolver = new TestResolver();
        final Policy policy = builder.buildPolicy( metaData, resolver );
        final CodeSource codesource =
            new CodeSource( new URL( "file:/" ), new Certificate[]{MockCertificate.JENNY_CERTIFICATE} );
        final PermissionCollection permissions = policy.getPermissions( codesource );
        final Enumeration enumeration = permissions.elements();
        while( enumeration.hasMoreElements() )
        {
            final Object perm = enumeration.nextElement();
            if( perm instanceof FilePermission )
            {
                return;
            }
        }
        fail( "Expected to get permission set with ALlPermission contained" );
    }

    public void testMetaDataWithAPermissionAndMultipleCerts()
        throws Exception
    {
        final PolicyBuilder builder = new TestPolicyBuilder();
        final PermissionMetaData permission =
            new PermissionMetaData( RuntimePermission.class.getName(), "getFactory", null,
                                    null, null );
        final GrantMetaData grant =
            new GrantMetaData( "file:/", "jenny,mischelle,jenny", "default",
                               new PermissionMetaData[]{permission} );
        final KeyStoreMetaData keyStore =
            new KeyStoreMetaData( "default", "http://spice.sourceforge.net", "DoDgY" );
        final PolicyMetaData metaData =
            new PolicyMetaData( new KeyStoreMetaData[]{keyStore}, new GrantMetaData[]{grant} );
        final TestResolver resolver = new TestResolver();
        final Policy policy = builder.buildPolicy( metaData, resolver );
        final CodeSource codesource =
            new CodeSource( new URL( "file:/" ), new Certificate[]{MockCertificate.JENNY_CERTIFICATE} );
        final PermissionCollection permissions = policy.getPermissions( codesource );
        final Enumeration enumeration = permissions.elements();
        while( enumeration.hasMoreElements() )
        {
            final Object perm = enumeration.nextElement();
            if( perm instanceof RuntimePermission )
            {
                return;
            }
        }
        fail( "Expected to get permission set with ALlPermission contained" );
    }

    public void testMetaDataWithAPermissionAndCertsAndUnResolverPerm()
        throws Exception
    {
        final PolicyBuilder builder = new TestPolicyBuilder();
        final PermissionMetaData permission =
            new PermissionMetaData( AllPermission.class.getName(), null, null,
                                    "jenny", "default" );
        final GrantMetaData grant =
            new GrantMetaData( "file:/", "jenny", "default",
                               new PermissionMetaData[]{permission} );
        final KeyStoreMetaData keyStore =
            new KeyStoreMetaData( "default", "http://spice.sourceforge.net", "DoDgY" );
        final PolicyMetaData metaData =
            new PolicyMetaData( new KeyStoreMetaData[]{keyStore}, new GrantMetaData[]{grant} );
        final TestResolver resolver = new TestResolver();
        final Policy policy = builder.buildPolicy( metaData, resolver );
        final CodeSource codesource =
            new CodeSource( new URL( "file:/" ), new Certificate[]{MockCertificate.JENNY_CERTIFICATE} );
        final PermissionCollection permissions = policy.getPermissions( codesource );
        final Enumeration enumeration = permissions.elements();
        while( enumeration.hasMoreElements() )
        {
            final Object perm = enumeration.nextElement();
            if( perm instanceof UnresolvedPermission )
            {
                return;
            }
        }
        fail( "Expected to get permission set with UnresolvedPermission contained" );
    }

    public void testMetaDataWithAPermissionAndCertsAndMisnamedPerm()
        throws Exception
    {
        final PolicyBuilder builder = new TestPolicyBuilder();
        final PermissionMetaData permission =
            new PermissionMetaData( AllPermission.class.getName() + "sss", null, null,
                                    null, null );
        final GrantMetaData grant =
            new GrantMetaData( "file:/", "jenny", "default",
                               new PermissionMetaData[]{permission} );
        final KeyStoreMetaData keyStore =
            new KeyStoreMetaData( "default", "http://spice.sourceforge.net", "DoDgY" );
        final PolicyMetaData metaData =
            new PolicyMetaData( new KeyStoreMetaData[]{keyStore}, new GrantMetaData[]{grant} );
        final TestResolver resolver = new TestResolver();
        final Policy policy = builder.buildPolicy( metaData, resolver );
        final CodeSource codesource =
            new CodeSource( new URL( "file:/" ), new Certificate[]{MockCertificate.JENNY_CERTIFICATE} );
        final PermissionCollection permissions = policy.getPermissions( codesource );
        final Enumeration enumeration = permissions.elements();
        while( enumeration.hasMoreElements() )
        {
            final Object perm = enumeration.nextElement();
            if( perm instanceof UnresolvedPermission )
            {
                return;
            }
        }
        fail( "Expected to get permission set with UnresolvedPermission contained" );
    }

    public void testFailureWhenCreatingKeyStore()
        throws Exception
    {
        final PolicyBuilder builder = new TestPolicyBuilder();
        final PermissionMetaData permission =
            new PermissionMetaData( AllPermission.class.getName() + "sss", null, null,
                                    null, null );
        final GrantMetaData grant =
            new GrantMetaData( "file:/", "jenny", "default",
                               new PermissionMetaData[]{permission} );
        final KeyStoreMetaData keyStore =
            new KeyStoreMetaData( "default", "http://spice.sourceforge.net/NoExist", "DoDgY" );
        final PolicyMetaData metaData =
            new PolicyMetaData( new KeyStoreMetaData[]{keyStore}, new GrantMetaData[]{grant} );
        final TestResolver resolver = new TestResolver();
        try
        {
            builder.buildPolicy( metaData, resolver );
            fail( "Expected to fail when creating policy as unable to create store" );
        }
        catch( Exception e )
        {
        }
    }

    public void testFailureRetrievingCertForAlias()
        throws Exception
    {
        final PolicyBuilder builder = new TestNoInitPolicyBuilder();
        final PermissionMetaData permission =
            new PermissionMetaData( AllPermission.class.getName() + "sss", null, null,
                                    null, null );
        final GrantMetaData grant =
            new GrantMetaData( "file:/", "peter", "default",
                               new PermissionMetaData[]{permission} );
        final KeyStoreMetaData keyStore =
            new KeyStoreMetaData( "default", "http://spice.sourceforge.net", "DoDgY" );
        final PolicyMetaData metaData =
            new PolicyMetaData( new KeyStoreMetaData[]{keyStore}, new GrantMetaData[]{grant} );
        final TestResolver resolver = new TestResolver();
        try
        {
            builder.buildPolicy( metaData, resolver );
            fail( "Expected to fail when creating policy as unable to create store" );
        }
        catch( Exception e )
        {
        }
    }

    public void testAliasNoExist()
        throws Exception
    {
        final PolicyBuilder builder = new TestPolicyBuilder();
        final PermissionMetaData permission =
            new PermissionMetaData( AllPermission.class.getName() + "sss", null, null,
                                    null, null );
        final GrantMetaData grant =
            new GrantMetaData( "file:/", "peter", "default",
                               new PermissionMetaData[]{permission} );
        final KeyStoreMetaData keyStore =
            new KeyStoreMetaData( "default", "http://spice.sourceforge.net", "DoDgY" );
        final PolicyMetaData metaData =
            new PolicyMetaData( new KeyStoreMetaData[]{keyStore}, new GrantMetaData[]{grant} );
        final TestResolver resolver = new TestResolver();
        try
        {
            builder.buildPolicy( metaData, resolver );
            fail( "Expected to fail when creating policy as unable to load alias" );
        }
        catch( Exception e )
        {
        }
    }

    public void testNoKeyStore()
        throws Exception
    {
        final PolicyBuilder builder = new TestPolicyBuilder();
        final PermissionMetaData permission =
            new PermissionMetaData( AllPermission.class.getName(), null, null,
                                    null, null );
        final GrantMetaData grant =
            new GrantMetaData( "file:/", "peter", "default",
                               new PermissionMetaData[]{permission} );
        final PolicyMetaData metaData =
            new PolicyMetaData( new KeyStoreMetaData[]{}, new GrantMetaData[]{grant} );
        final TestResolver resolver = new TestResolver();
        try
        {
            builder.buildPolicy( metaData, resolver );
            fail( "Expected to fail when creating policy as missing keystore" );
        }
        catch( Exception e )
        {
        }
    }
}
