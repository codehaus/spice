package org.realityforge.metaclass.tools.qdox;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Jdk14AttributeParser
    implements AttributeParser
{
    public String[] parseValueIntoParameterSpans( String value )
    {
        final Pattern pattern = Pattern.compile( PERL_PATTERN );
        final Matcher matcher = pattern.matcher( value );
        if( matcher.matches() )
        {
            final int count = matcher.groupCount();
            final String[] results = new String[ count ];
            for( int i = 0; i < count; i++ )
            {
                results[ i ] = matcher.group( i ).trim();
            }
            return results;
        }
        else
        {
            return null;
        }
    }
}
