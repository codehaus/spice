package org.realityforge.metaclass.tools.qdox;

import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.MatchResult;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;

class OROAttributeParser
    implements AttributeParser
{
    private final Perl5Compiler m_compiler = new Perl5Compiler();
    private final Perl5Matcher m_matcher = new Perl5Matcher();

    public String[] parseValueIntoParameterSpans( final String value )
    {
        final Pattern pattern = getAttributePattern();
        if( m_matcher.matches( value, pattern ) )
        {
            final MatchResult match = m_matcher.getMatch();
            final int count = match.groups() - 1;
            final String[] results = new String[ count ];
            for( int i = 0; i < count; i++ )
            {
                results[ i ] = match.group( i + 1 ).trim();
            }
            return results;
        }
        else
        {
            return null;
        }
    }

    private Pattern getAttributePattern()
    {
        try
        {
            return m_compiler.compile( PERL_PATTERN );
        }
        catch( final MalformedPatternException mpe )
        {
            final String message =
                "Error compiling pattern " + PERL_PATTERN + " due to " + mpe;
            throw new IllegalStateException( message );
        }
    }

}
