/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 * Portions of this software are based upon software originally
 * developed as part of the Apache Avalon project under
 * the Apache 1.1 License.
 */

import java.util.List;

import org.realityforge.cli.CLArgsParser;
import org.realityforge.cli.CLOption;
import org.realityforge.cli.CLOptionDescriptor;
import org.realityforge.cli.CLUtil;

/**
 * This simple example shows how to set it up so that only the
 * long form or only short form of an option is capable of
 * being used.
 *
 * @author <a href="peter@apache.org">Peter Donald</a>
 */
public class NoAlias
{
    // Define our short one-letter option identifiers.
    private static final int SHORT_OPT = 's';
    private static final int LONG_OPT = 1;

    private static final CLOptionDescriptor[] OPTIONS = new CLOptionDescriptor[]
    {
        new CLOptionDescriptor( null,
                                CLOptionDescriptor.ARGUMENT_DISALLOWED,
                                SHORT_OPT,
                                "option with only short form",
                                new int[ 0 ] ),
        new CLOptionDescriptor( "long",
                                CLOptionDescriptor.ARGUMENT_DISALLOWED,
                                LONG_OPT,
                                "option with long form" )
    };

    public static void main( final String[] args )
    {
        System.out.println( "Starting NoAlias..." );
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

                case SHORT_OPT:
                    System.out.println( "Received short option" );
                    break;

                case LONG_OPT:
                    System.out.println( "Received long option" );
                    break;
            }
        }
    }
}