package org.realityforge.metaschema;

import java.util.Properties;

/**
 * Validates Attributes for a given artefact.
 * Attributes are identified by their names and their values can be of two types:
 * a String or a Properties key/value set.
 * 
 * @author Mauro Talevi 
 */
public interface AttributeValidator {

	/**
	 *   Validates attribute of an artefact  
	 * 
	 *   @param artefactName the name of the artefact to which the attribute applies
	 *   @param name the name of the attribute
	 *   @param value the String representing the value of the attribute
	 */
	void validateAttribute(String artefactName, String name, String value)
		throws InvalidMetaDataException;

	/**
	 *   Validates attribute of an artefact
	 * 
	 *   @param artefactName the name of the artefact to which the attribute applies
	 *   @param name the name of the attribute
	 *   @param values the Properties representing the values of the attribute
	 */
	void validateAttribute(String artefactName, String name, Properties values)
		throws InvalidMetaDataException;

}
