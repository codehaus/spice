/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaschema;

import java.io.InputStream;
import java.util.Properties;
import java.util.StringTokenizer;
import org.exteca.ontology.Attribute;
import org.exteca.ontology.Concept;
import org.exteca.ontology.Ontology;
import org.exteca.ontology.OntologyException;
import org.exteca.ontology.Property;

/**
 * SchemaValidator validates a metadata schema implementing one or more
 * validator.
 *
 * Schema is represented as an Ontology and read from an XML file.
 *
 * @author Mauro Talevi
 */
public class SchemaValidator
    implements AttributeValidator
{
    /** The Ontology representing the schema */
    private Ontology ontology = new Ontology();

    /**
     *  Default constructor
     */
    public SchemaValidator()
    {
    }

    /**
     *  Reads Ontology from file
     *  @param schemaPath the path to the Ontology file
     */
    public void readSchema( String schemaPath ) throws InvalidMetaDataException
    {
        try
        {
            ontology.read( schemaPath );
        }
        catch( OntologyException e )
        {
            final String message = "Invalid Schema " + schemaPath;
            throw new InvalidMetaDataException( message );
        }
    }

    /**
     *  Reads Ontology from InputStream
     *  @param schemaInputStream the InputStream containting the Ontology
     */
    public void readSchema( InputStream schemaInputStream ) throws InvalidMetaDataException
    {
        try
        {
            ontology.read( schemaInputStream );
        }
        catch( OntologyException e )
        {
            final String message = "Invalid Schema InputStream " + schemaInputStream;
            throw new InvalidMetaDataException( message );
        }
    }

    /**
     *   Validates attribute of an artefact
     *
     *   @param artefactName the name of the artefact to which the attribute applies
     *   @param name the name of the attribute
     *   @param value the String representing the value of the attribute
     */
    public void validateAttribute( String artefactName, String name, String value )
        throws InvalidMetaDataException
    {
        try
        {
            Concept artefact = ontology.getConcept( artefactName );
            Attribute attribute = ontology.getAttribute( name );
            Property property = new Property( attribute, value );
            if( !artefact.hasProperty( property ) )
            {
                final String message = "Invalid Attribute " + name;
                throw new InvalidMetaDataException( message );

            }
        }
        catch( OntologyException e )
        {
            final String message = "Invalid attribute " + name;
            throw new InvalidMetaDataException( message );
        }
    }

    /**
     *   Validates attribute of an artefact
     *
     *   @param artefactName the name of the artefact to which the attribute applies
     *   @param name the name of the attribute
     *   @param parameters the Properties representing the values of the attribute
     */
    public void validateAttribute( String artefactName, String name, Properties parameters )
        throws InvalidMetaDataException
    {
        try
        {
            Concept artefact = ontology.getConcept( artefactName );
            Attribute attribute = ontology.getAttribute( name );
            // Ontology does not allow (yet) to define an attribute data-type Properties
            // workaround is to get property value as String create Properties object
            // and validate against input value
            Property[] properties = artefact.getProperties();
            for( int i = 0; i < properties.length; i++ )
            {
                // First find the properties identified by the attribute above
                if( attribute.equals( properties[ i ].getAttribute() ) )
                {
                    // reset boolean flag
                    boolean isValid = true;
                    String value = properties[ i ].getValue();
                    // value is a set of key=value pairs separated by ;
                    String[] pairs = splitString( value, ";" );
                    for( int j = 0; j < pairs.length; j++ )
                    {
                        String[] pairValues = splitString( pairs[ j ], "=" );
                        // validate values against the Properties provided
                        if( !parameters.containsKey( pairValues[ 0 ] ) ||
                            !parameters.containsValue( pairValues[ 1 ] ) ||
                            !pairValues[ 1 ].equals( parameters.getProperty( pairValues[ 0 ] ) ) )
                        {
                            isValid = false;
                            break;
                        }
                    }
                    if( !isValid )
                    {
                        final String message = "Invalid Attribute " + name;
                        throw new InvalidMetaDataException( message );
                    }
                }
            }
        }
        catch( OntologyException e )
        {
            final String message = "Invalid attribute " + name;
            throw new InvalidMetaDataException( message );
        }
    }

    /**
     * Splits the string on every token into an array of stack frames.
     *
     * @param string the string to split
     * @param onToken the token to split on
     * @return the resultant array
     */
    private static String[] splitString( final String string, final String onToken )
    {
        final StringTokenizer tokenizer = new StringTokenizer( string, onToken );
        final String[] result = new String[ tokenizer.countTokens() ];

        for( int i = 0; i < result.length; i++ )
        {
            result[ i ] = tokenizer.nextToken();
        }

        return result;
    }
}
