/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.cli;

/**
 * CLUtil offers basic utility operations for use both internal and external to package.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.5 $ $Date: 2003-06-27 03:45:17 $
 * @since 4.0
 * @see CLOptionDescriptor
 */
public final class CLUtil
{
    private static final int MAX_DESCRIPTION_COLUMN_LENGTH = 60;

    /**
     * Format options into StringBuffer and return. This is typically used to
     * print "Usage" text in response to a "--help" or invalid option.
     *
     * @param options the option descriptors
     * @return the formatted description/help for options
     */
    public static final StringBuffer describeOptions( final CLOptionDescriptor[] options )
    {
        final String lSep = System.getProperty( "line.separator" );
        final StringBuffer sb = new StringBuffer();

        for( int i = 0; i < options.length; i++ )
        {
            final char ch = (char)options[ i ].getId();
            final String name = options[ i ].getName();
            String description = options[ i ].getDescription();
            int flags = options[ i ].getFlags();
            boolean argumentRequired =
                ( ( flags & CLOptionDescriptor.ARGUMENT_REQUIRED ) ==
                CLOptionDescriptor.ARGUMENT_REQUIRED );
            boolean twoArgumentsRequired =
                ( ( flags & CLOptionDescriptor.ARGUMENTS_REQUIRED_2 ) ==
                CLOptionDescriptor.ARGUMENTS_REQUIRED_2 );
            boolean needComma = false;
            if( twoArgumentsRequired )
            {
                argumentRequired = true;
            }

            sb.append( '\t' );

            if( Character.isLetter( ch ) )
            {
                sb.append( "-" );
                sb.append( ch );
                needComma = true;
            }

            if( null != name )
            {
                if( needComma )
                {
                    sb.append( ", " );
                }

                sb.append( "--" );
                sb.append( name );
            }

            if( argumentRequired )
            {
                sb.append( " <argument>" );
            }
            if( twoArgumentsRequired )
            {
                sb.append( "=<value>" );
            }
            sb.append( lSep );

            if( null != description )
            {
                while( description.length() > MAX_DESCRIPTION_COLUMN_LENGTH )
                {
                    final String descriptionPart =
                        description.substring( 0, MAX_DESCRIPTION_COLUMN_LENGTH );
                    description =
                        description.substring( MAX_DESCRIPTION_COLUMN_LENGTH );
                    sb.append( "\t\t" );
                    sb.append( descriptionPart );
                    sb.append( lSep );
                }

                sb.append( "\t\t" );
                sb.append( description );
                sb.append( lSep );
            }
        }
        return sb;
    }

    /**
     * Private Constructor so that no instance can ever be created.
     *
     */
    private CLUtil()
    {
    }
}
