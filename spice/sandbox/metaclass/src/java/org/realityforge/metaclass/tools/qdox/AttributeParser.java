package org.realityforge.metaclass.tools.qdox;

interface AttributeParser
{
    String PERL_PATTERN = "^[ \\t\\r\\n]*([a-zA-Z\\_][a-zA-Z0-9\\_]*=\\\"[^\\\"]*\\\"[ \\t\\r\\n]*)+";

    String[] parseValueIntoParameterSpans( String value );
}
