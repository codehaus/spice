/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.salt.config;

/**
 * Parameters present flat configuration data. Contained in the Parameters
 * object is a set of name-value pairs.
 *
 * @version $Revision: 1.1 $ $Date: 2003-12-02 02:15:04 $
 */
public interface Parameters
{
    /**
     * Return the names of all the parameters.
     *
     * @return the names of all the parameters.
     */
    String[] getParameterNames();

    /**
     * Return true of parameter with specified name exists.
     *
     * @param name the name
     * @return true of parameter with specified name exists.
     */
    boolean isParameter( String name );

    /**
     * Return value of parameter with specified name.
     *
     * @param name the name
     * @return the value
     * @throws org.codehaus.spice.salt.config.ParameterException if unable to
     * locate parameter
     */
    String getParameter( String name )
        throws ParameterException;

    /**
     * Return value of parameter with specified name.
     *
     * @param name the name
     * @param defaultValue the defaultValue if specified parameter does not
     * exist
     * @return the value
     */
    String getParameter( String name, String defaultValue );

    /**
     * Return value of parameter with specified name as an integer.
     *
     * @param name the name
     * @return the value
     * @throws org.codehaus.spice.salt.config.ParameterException if unable to
     * locate parameter or parameter can not be converted to correct type
     */
    int getParameterAsInteger( String name )
        throws ParameterException;

    /**
     * Return value of parameter with specified name as an integer.
     *
     * @param name the name
     * @param defaultValue the defaultValue if specified parameter does not
     * exist or parameter can not be converted to the correct type
     * @return the value
     */
    int getParameterAsInteger( String name, int defaultValue );

    /**
     * Return value of parameter with specified name as a long.
     *
     * @param name the name
     * @return the value
     * @throws org.codehaus.spice.salt.config.ParameterException if unable to
     * locate parameter or parameter can not be converted to correct type
     */
    long getParameterAsLong( String name )
        throws ParameterException;

    /**
     * Return value of parameter with specified name as a long.
     *
     * @param name the name
     * @param defaultValue the defaultValue if specified parameter does not
     * exist or parameter can not be converted to the correct type
     * @return the value
     */
    long getParameterAsLong( String name, long defaultValue );

    /**
     * Return value of parameter with specified name as a boolean.
     *
     * @param name the name
     * @return the value
     * @throws org.codehaus.spice.salt.config.ParameterException if unable to
     * locate parameter or parameter can not be converted to correct type
     */
    boolean getParameterAsBoolean( String name )
        throws ParameterException;

    /**
     * Return value of parameter with specified name as a boolean.
     *
     * @param name the name
     * @param defaultValue the defaultValue if specified parameter does not
     * exist or parameter can not be converted to the correct type
     * @return the value
     */
    boolean getParameterAsBoolean( String name, boolean defaultValue );

    /**
     * Return value of parameter with specified name as a float.
     *
     * @param name the name
     * @return the value
     * @throws org.codehaus.spice.salt.config.ParameterException if unable to
     * locate parameter or parameter can not be converted to correct type
     */
    float getParameterAsFloat( String name )
        throws ParameterException;

    /**
     * Return value of parameter with specified name as a float.
     *
     * @param name the name
     * @param defaultValue the defaultValue if specified parameter does not
     * exist or parameter can not be converted to the correct type
     * @return the value
     */
    float getParameterAsFloat( String name, float defaultValue );

    /**
     * Return a Parameters object that represents a subset of parameters with
     * specified prefix. The child parameters has a prefix with the separator
     * ('.') appended. ie. if the prefix was "foo" then the parameter "foo.baz"
     * would be included in child Parameters object using the key "baz".
     *
     * @param prefix the prefix
     * @return the parameters object
     */
    Parameters getChildParameters( String prefix );
}
