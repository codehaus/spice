/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.converter.lib;

import java.io.File;
import org.realityforge.converter.AbstractConverter;
import org.realityforge.converter.ConverterException;

/**
 * String to File converter.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-04-04 11:26:08 $
 */
public class StringToFileConverter
    extends AbstractConverter
{
    /**
     * The directory that relative files are relative to.
     */
    private final File m_baseDirectory;

    /**
     * Construct the converter.
     *
     * @param baseDirectory the directory that relative files are relative to
     */
    public StringToFileConverter( final File baseDirectory )
    {
        this( baseDirectory, null );
    }

    /**
     * Construct the converter with a default value.
     * If the default value is non-null, it will be returned if unable
     * to convert object to correct type.
     *
     * @param baseDirectory the directory that relative files are relative to
     * @param defaultValue the default value
     */
    public StringToFileConverter( final File baseDirectory,
                                  final File defaultValue )
    {
        super( String.class, File.class, defaultValue );
        if( null == baseDirectory )
        {
            throw new NullPointerException( "baseDirectory" );
        }
        m_baseDirectory = baseDirectory;
    }

    /**
     * Converts a String to a File.
     *
     * @param object the original object to convert
     * @param context the context in which to convert object (unused)
     * @return the converted object
     * @throws ConverterException if error converting object
     */
    public Object convert( final Object object, final Object context )
        throws ConverterException
    {
        return new File( m_baseDirectory, object.toString() );
    }
}

