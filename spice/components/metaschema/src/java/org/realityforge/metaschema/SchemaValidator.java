package org.realityforge.metaschema;

import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;
import org.exteca.ontology.*;
import org.apache.regexp.*;

/**
 * SchemaValidator validates a metadata schema implementing one or more
 * validator.
 *  
 * Schema is represented as an Ontology and read from an XML file.
 *
 * @author Mauro Talevi
 */
public class SchemaValidator implements AttributeValidator {
	
	/** The Ontology representing the schema */
	private Ontology ontology = new Ontology();
	
	/** 
	 *  The RE for splitting =-separated Strings
	 */ 
	RE splitByEquals;
	
	/** 
	 *  The RE for splitting ;-separated Strings
	 */ 
	RE splitBySemicolon;
	
	/**  
	 *  Default constructor
	 */
	public SchemaValidator() {
		try {
			splitByEquals = new RE("=");	
			splitBySemicolon = new RE(";");	
		} catch ( RESyntaxException e ) {
		}	
	}
	
	/**
	 *  Reads Ontology from file
	 *  @param schemaPath the path to the Ontology file
	 */
	public void readSchema(String schemaPath) throws InvalidMetaDataException {
		try {
			ontology.read(schemaPath);
		} catch (OntologyException e) {
			final String message = "Invalid Schema " + schemaPath;
			throw new InvalidMetaDataException(message);
		}
	}
	
	/**
	 *  Reads Ontology from InputStream
	 *  @param schemaInputStream the InputStream containting the Ontology 
	 */
	public void readSchema(InputStream schemaInputStream) throws InvalidMetaDataException {
		try {
			ontology.read(schemaInputStream);
		} catch (OntologyException e) {
			final String message = "Invalid Schema InputStream " + schemaInputStream;
			throw new InvalidMetaDataException(message);
		}
	}
	/**
	 *   Validates attribute of an artefact  
	 * 
	 *   @param artefactName the name of the artefact to which the attribute applies
	 *   @param name the name of the attribute
	 *   @param value the String representing the value of the attribute
	 */
	public void validateAttribute(String artefactName, String name, String value)
		throws InvalidMetaDataException {
	    try {
			Concept artefact = ontology.getConcept( artefactName );
			Attribute attribute = ontology.getAttribute( name );
			Property property = new Property( attribute, value );
			if ( !artefact.hasProperty( property ) ){
				final String message = "Invalid Attribute " + name;
				throw new InvalidMetaDataException( message );

			}
		} catch (OntologyException e) {
			final String message = "Invalid attribute " + name;
			throw new InvalidMetaDataException( message );
		}
	}
	
	/**
	 *   Validates attribute of an artefact
	 * 
	 *   @param artefactName the name of the artefact to which the attribute applies
	 *   @param name the name of the attribute
	 *   @param values the Properties representing the values of the attribute
	 */
	public void validateAttribute(String artefactName, String name, Properties parameters)
		throws InvalidMetaDataException{
		try {
			Concept artefact = ontology.getConcept( artefactName );
			Attribute attribute = ontology.getAttribute( name );
			// Ontology does not allow (yet) to define an attribute data-type Properties
			// workaround is to get property value as String create Properties object
			// and validate against input value
			Property[] properties = artefact.getProperties();
			for ( int i = 0; i < properties.length; i++ ){
				// First find the properties identified by the attribute above
				if ( attribute.equals(properties[i].getAttribute()) ){
					// reset boolean flag
					boolean isValid = true;
					String value = properties[i].getValue();
					// value is a set of key=value pairs separated by ;
					String[] pairs = splitBySemicolon.split( value );
					for ( int j = 0; j < pairs.length; j++ ){
						String[] pairValues = splitByEquals.split( pairs[j] );
						// validate values against the Properties provided
						if ( !parameters.containsKey(pairValues[0]) ||
						     !parameters.containsValue(pairValues[1]) ||
				 			 !pairValues[1].equals(parameters.getProperty(pairValues[0])) ) {
							isValid = false;
							break;
						}
					}
					if ( !isValid ){									
						final String message = "Invalid Attribute " + name;
						throw new InvalidMetaDataException( message );
					}
				}
			}
			
			
			
		} catch (OntologyException e) {
			final String message = "Invalid attribute " + name;
			throw new InvalidMetaDataException( message );
		}
	}

}
