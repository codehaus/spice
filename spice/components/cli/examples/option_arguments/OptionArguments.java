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
 * This simple example shows how to have options, requiring
 * an argument, optionally supporting an argument or requiring
 * 2 arguments.
 *
 * @author <a href="peter@apache.org">Peter Donald</a>
 */
public class OptionArguments
{
    // Define our short one-letter option identifiers.
    private static final int FILE_OPT = 'f';
    private static final int DEFINE_OPT = 'D';
    private static final int SECURE_OPT = 'S';

    private static final CLOptionDescriptor[] OPTIONS = new CLOptionDescriptor[]
    {
        //File requires an argument
        new CLOptionDescriptor( "file",
                                CLOptionDescriptor.ARGUMENT_REQUIRED,
                                FILE_OPT,
                                "specify a file" ),

        //secure can take an argument if supplied
        new CLOptionDescriptor( "secure",
                                CLOptionDescriptor.ARGUMENT_OPTIONAL,
                                SECURE_OPT,
                                "set security mode" ),

        //define requires 2 arguments
        new CLOptionDescriptor( "define",
                                CLOptionDescriptor.ARGUMENTS_REQUIRED_2,
                                DEFINE_OPT,
                                "Require 2 arguments" )
    };

    public static void main( final String[] args )
    {
        System.out.println( "Starting OptionArguments..." );
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

                case FILE_OPT:
                    System.out.println( "File: " + option.getArgument() );
                    break;

                case SECURE_OPT:
                    if( null == option.getArgument() )
                    {
                        System.out.println( "Secure Mode with no args" );
                    }
                    else
                    {
                        System.out.println( "Secure Mode with arg: " + option.getArgument() );
                    }
                    break;

                case DEFINE_OPT:
                    System.out.println( "Defining: " +
                                        option.getArgument( 0 ) + "=" +
                                        option.getArgument( 1 ) );
                    break;
            }
        }
    }
}