/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 * This product includes software developed by the
 * Apache Software Foundation (http://www.apache.org/).
 */

import java.util.List;

import org.realityforge.cli.CLArgsParser;
import org.realityforge.cli.CLOption;
import org.realityforge.cli.CLOptionDescriptor;
import org.realityforge.cli.CLUtil;

/**
 * Demonstrates Basic example of command line utilities.
 *
 * @author <a href="jeff@socialchange.net.au">Jeff Turner</a>
 * @author <a href="peter@apache.org">Peter Donald</a>
 */
public class BasicCLI
{
    // Define our short one-letter option identifiers.
    private static final int HELP_OPT = 'h';
    private static final int VERSION_OPT = 'v';

    /**
     *  Define the understood options. Each CLOptionDescriptor contains:
     * - The "long" version of the option. Eg, "help" means that "--help" will
     * be recognised.
     * - The option flags, governing the option's argument(s).
     * - The "short" version of the option. Eg, 'h' means that "-h" will be
     * recognised.
     * - A description of the option.
     */
    private static final CLOptionDescriptor[] OPTIONS = new CLOptionDescriptor[]
    {
        new CLOptionDescriptor( "help",
                                CLOptionDescriptor.ARGUMENT_DISALLOWED,
                                HELP_OPT,
                                "print this message and exit" ),
        new CLOptionDescriptor( "version",
                                CLOptionDescriptor.ARGUMENT_DISALLOWED,
                                VERSION_OPT,
                                "print the version information and exit" )
    };

    public static void main( final String[] args )
    {
        System.out.println( "Starting BasicCLI..." );
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

                case HELP_OPT:
                    printUsage();
                    break;

                case VERSION_OPT:
                    printVersion();
                    break;
            }
        }
    }

    /**
     * Print out a dummy version
     */
    private static void printVersion()
    {
        System.out.println( "1.0" );
        System.exit( 0 );
    }

    /**
     * Print out a usage statement
     */
    private static void printUsage()
    {
        final String lineSeparator = System.getProperty( "line.separator" );

        final StringBuffer msg = new StringBuffer();

        msg.append( lineSeparator );
        msg.append( "Excalibur command-line arg parser demo" );
        msg.append( lineSeparator );
        msg.append( "Usage: java " + IncompatOptions.class.getName() + " [options]" );
        msg.append( lineSeparator );
        msg.append( lineSeparator );
        msg.append( "Options: " );
        msg.append( lineSeparator );

        /*
         * Notice that the next line uses CLUtil.describeOptions to generate the
         * list of descriptions for each option
         */
        msg.append( CLUtil.describeOptions( BasicCLI.OPTIONS ).toString() );

        System.out.println( msg.toString() );

        System.exit( 0 );
    }
}