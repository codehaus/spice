/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */

import java.util.List;

import org.realityforge.cli.CLArgsParser;
import org.realityforge.cli.CLOption;
import org.realityforge.cli.CLOptionDescriptor;
import org.realityforge.cli.CLUtil;

/**
 * Demonstrates example of CLI containing incompatible arguments.
 * In this demo the quiet and verbose options are incompatible with
 * each other.
 *
 * @author <a href="peter@apache.org">Peter Donald</a>
 */
public class IncompatOptions
{
    // Define our short one-letter option identifiers.
    private static final int VERBOSE_OPT = 'v';
    private static final int QUIET_OPT = 'q';

    /**
     *  Define the understood options. Each CLOptionDescriptor contains:
     * - The "long" version of the option. Eg, "quiet" means that "--quiet" will
     * be recognised.
     * - The option flags, governing the option's argument(s).
     * - The "short" version of the option. Eg, 'q' means that "-q" will be
     * recognised.
     * - A description of the option.
     */
    private static final CLOptionDescriptor[] OPTIONS = new CLOptionDescriptor[]
    {
        new CLOptionDescriptor( "verbose",
                                CLOptionDescriptor.ARGUMENT_DISALLOWED,
                                VERBOSE_OPT,
                                "Run command in verbose mode",
                                new int[]{QUIET_OPT} ),
        new CLOptionDescriptor( "quiet",
                                CLOptionDescriptor.ARGUMENT_DISALLOWED,
                                QUIET_OPT,
                                "Run command in quiet mode",
                                new int[]{VERBOSE_OPT} )
    };

    public static void main( final String[] args )
    {
        System.out.println( "Starting IncompatOptions..." );
        System.out.println( CLUtil.describeOptions( OPTIONS ) );
        System.out.println();

        // Parse the arguments
        final CLArgsParser parser = new CLArgsParser( args, OPTIONS );

        //Make sure that there was no errors parsing
        //arguments
        if( null != parser.getErrorString() )
        {
            System.err.println( "Error: " + parser.getErrorString() );
            return;
        }

        // Get a list of parsed options
        final List options = parser.getArguments();
        final int size = options.size();

        for( int i = 0; i < size; i++ )
        {
            final CLOption option = (CLOption)options.get( i );

            switch( option.getId() )
            {
                case CLOption.TEXT_ARGUMENT:
                    //This occurs when a user supplies an argument that
                    //is not an option
                    System.out.println( "Unknown arg: " + option.getArgument() );
                    break;

                case VERBOSE_OPT:
                    System.out.println( "Verbose mode!" );
                    break;

                case QUIET_OPT:
                    System.out.println( "Quiet mode!" );
                    break;
            }
        }
    }
}