/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.configkit;

import java.io.InputStream;
import org.iso_relax.verifier.Schema;
import org.iso_relax.verifier.VerifierFactory;
import org.iso_relax.verifier.VerifierConfigurationException;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

/**
 * The ConfigValidatorFactory is responsible for creating ConfigValidator
 * objects to validate configuration according to specified schemas.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.2 $ $Date: 2003-04-04 23:27:22 $
 */
public final class ConfigValidatorFactory
{
    /**
     * A constant defining namespace of RELAX_NG schema language.
     */
    public static final String RELAX_NG = "http://relaxng.org/ns/structure/1.0";

    /**
     * A constant defining namespace of W3C XMLSchema language.
     */
    public static final String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";

    /**
     * The Class object that points at MSV class.
     */
    private static Class c_msvClass;

    /**
     * Create a ConfigValidator and attempt to guess Schema Type. The config validator
     * loads the schema from specified publicID, systemID, classloader
     * combination using the {@link ResolverFactory}.
     *
     * @param publicID the publicID of schema (may be null)
     * @param systemID the systemID of schema (may be null)
     * @param classLoader the classloader from which to load schema
     * @return the ConfigValidatorthat conforms to input
     * @throws Exception if unable to create validator
     */
    public static ConfigValidator create( final String publicID,
                                          final String systemID,
                                          final ClassLoader classLoader )
        throws Exception
    {
        return create( null, publicID, systemID, classLoader );
    }

    /**
     * Create a ConfigValidator with specified type. The config validator
     * loads the schema from specified publicID, systemID, classloader
     * combination using the {@link ResolverFactory}.
     *
     * @param schemaType the type of the schema.
     *        (Usually a URL such as "http://relaxng.org/ns/structure/1.0")
     * @param publicID the publicID of schema (may be null)
     * @param systemID the systemID of schema (may be null)
     * @param classLoader the classloader from which to load schema
     * @return the ConfigValidatorthat conforms to input
     * @throws Exception if unable to create validator
     */
    public static ConfigValidator create( final String schemaType,
                                          final String publicID,
                                          final String systemID,
                                          final ClassLoader classLoader )
        throws Exception
    {
        if( null == publicID && null == systemID )
        {
            throw new NullPointerException( "publicID" );
        }

        final EntityResolver resolver = ResolverFactory.createResolver( classLoader );
        final InputSource inputSource = resolver.resolveEntity( publicID, systemID );
        if( null == inputSource )
        {
            final String message =
                "Unable to locate schema with id " + publicID + "/" + systemID;
            throw new Exception( message );
        }

        return create( schemaType, inputSource );
    }

    /**
     * Create a ConfigValidator and guess Schema type. The schema is loaded
     * from specified stream.
     *
     * @param inputStream the stream to load schema from
     * @return the ConfigValidatorthat conforms to input
     * @throws Exception if unable to create validator
     */
    public static ConfigValidator create( final InputStream inputStream )
        throws Exception
    {
        return create( null, inputStream );
    }

    /**
     * Create a ConfigValidator with specified type. The schema is loaded
     * from specified stream.
     *
     * @param schemaType the type of the schema.
     *        (Usually a URL such as "http://relaxng.org/ns/structure/1.0")
     * @param inputStream the stream to load schema from
     * @return the ConfigValidatorthat conforms to input
     * @throws Exception if unable to create validator
     */
    public static ConfigValidator create( final String schemaType,
                                          final InputStream inputStream )
        throws Exception
    {
        if( null == inputStream )
        {
            throw new NullPointerException( "inputStream" );
        }

        final InputSource inputSource = new InputSource( inputStream );
        return create( schemaType, inputSource );
    }

    /**
     * Create a ConfigValidator and guess Schema type. The schema is loaded
     * from specified source.
     *
     * @param inputSource the source to load schema from
     * @return the ConfigValidatorthat conforms to input
     * @throws Exception if unable to create validator
     */
    public static ConfigValidator create( final InputSource inputSource )
        throws Exception
    {
        return create( null, inputSource );
    }

    /**
     * Create a ConfigValidator with specified type. The schema is loaded
     * from specified source.
     *
     * @param schemaType the type of the schema.
     *        (Usually a URL such as "http://relaxng.org/ns/structure/1.0")
     * @param inputSource the source to load schema from
     * @return the ConfigValidatorthat conforms to input
     * @throws Exception if unable to create validator
     */
    public static ConfigValidator create( final String schemaType,
                                          final InputSource inputSource )
        throws Exception
    {
        if( null == inputSource )
        {
            throw new NullPointerException( "inputSource" );
        }
        final VerifierFactory factory = createFactory( schemaType );
        final Schema schema = factory.compileSchema( inputSource );
        return new ConfigValidator( schema );
    }

    /**
     * Create a VerifierFactory according to specified schemaType.
     * If the schemaType is not specified then
     *
     * against DTDs, XML Schema, RelaxNG, Relax or TREX.
     *
     * @param schemaType the type of schema (may be null)
     * @return the VerifierFactory created
     * @throws VerifierConfigurationException if unable to get factory
     */
    private static VerifierFactory createFactory( final String schemaType )
        throws VerifierConfigurationException
    {
        if( null == schemaType )
        {
            try
            {
                if( null == c_msvClass )
                {
                    c_msvClass = Class.forName( "com.sun.msv.verifier.jarv.TheFactoryImpl" );
                }
                return (VerifierFactory)c_msvClass.newInstance();
            }
            catch( final Exception e )
            {
                final String message =
                    "Unable to load MSV factory and thus can " +
                    "not auto-discover schema type.";
                throw new VerifierConfigurationException( message, e );
            }
        }
        else
        {
            return VerifierFactory.newInstance( schemaType );
        }
    }
}
