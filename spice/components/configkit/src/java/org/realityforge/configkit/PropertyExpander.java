/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.configkit;

import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

/**
 * This is a utility class designed for expanding propertys in
 * configuration files. Propertys are stored in Map objects and
 * any section that begins with "${" and ends with "}" will have
 * inner name replaced with property value from map.
 *
 * @author <a href="mailto:peter at apache.org">Peter Donald</a>
 * @version $Revision: 1.2 $ $Date: 2003-05-26 12:37:57 $
 */
public final class PropertyExpander
{
    /**
     * Flag indicating that undefined propertys should be
     * not be replaced. ie "${myUnresolvedProperty}"
     */
    public static final int LEAVE_UNDEFINED = 1;

    /**
     * Flag indicating that undefined propertys should cause
     * an exception to be thrown.
     */
    public static final int EXCEPT_ON_UNDEFINED = 2;

    /**
     * Flag indicating that undefined propertys should be
     * replaced with a empty string "".
     */
    public static final int EMPTY_ON_UNDEFINED = 3;

    /**
     * The flag for behaviour when undefined property is encountered.
     * Must be one of the *_UNDEFINED propertys.
     */
    private final int m_onUndefined;

    /**
     * Create property expander with EXCEPT_ON_UNDEFINED policy for undefined propertys.
     */
    public PropertyExpander()
    {
        this( EXCEPT_ON_UNDEFINED );
    }

    /**
     * Create property expander with specified policy for undefined propertys.
     *
     * @param onUndefined the flag indicating behaviour when undefined
     *        property is encountered. Must be one of the *_UNDEFINED constants.
     */
    public PropertyExpander( final int onUndefined )
    {
        m_onUndefined = onUndefined;
    }

    /**
     * Expand all propertys in the input Properties object.
     *
     * @param input the Properties object to resolve
     * @param data the data that holds property values
     * @exception Exception if an error occurs
     */
    public Properties expandValues( final Properties input, final Map data )
        throws Exception
    {
        final Properties result = new Properties();
        final Iterator iterator = input.keySet().iterator();
        while( iterator.hasNext() )
        {
            final String key = (String)iterator.next();
            final String value = input.getProperty( key );
            final String newKey = expandValues( key, data );
            final String newValue = expandValues( value, data );
            result.setProperty( newKey, newValue );
        }
        return result;
    }

    /**
     * Expand all propertys in the input XML attributes and text contents.
     *
     * @param input the DOM element to resolve
     * @param data the data that holds property values
     * @exception Exception if an error occurs
     */
    public void expandValues( final Element input, final Map data )
        throws Exception
    {
        final NamedNodeMap attributes = input.getAttributes();
        final int attributeCount = attributes.getLength();
        for( int i = 0; i < attributeCount; i++ )
        {
            final Attr attr = (Attr)attributes.item( i );
            final String value = attr.getValue();
            final String newValue = expandValues( value, data );
            attr.setValue( newValue );
        }
        final NodeList nodes = input.getChildNodes();
        final int childCount = nodes.getLength();
        for( int i = 0; i < childCount; i++ )
        {
            final Node node = nodes.item( i );
            if( node instanceof Element )
            {
                expandValues( (Element)node, data );
            }
            else if( node instanceof Text )
            {
                final Text text = (Text)node;
                final String content = text.getData();
                final String newContent = expandValues( content, data );
                text.setData( newContent );
            }
        }
    }

    /**
     * Expand all propertys in input string.
     *
     * @param input the string to resolve
     * @param data the data that holds property values
     * @return the resolved string
     * @exception Exception if an error occurs
     */
    public String expandValues( final String input, final Map data )
        throws Exception
    {
        int start = findBeginning( input, 0 );
        if( -1 == start )
        {
            return input;
        }

        int end = findEnding( input, start );

        final int length = input.length();
        if( 0 == start && end == ( length - 1 ) )
        {
            return getValue( input.substring( start + 2, end ),
                             data );
        }

        final StringBuffer sb = new StringBuffer( length * 2 );
        int lastPlace = 0;

        while( true )
        {
            final String value =
                getValue( input.substring( start + 2, end ), data );

            sb.append( input.substring( lastPlace, start ) );
            sb.append( value );

            lastPlace = end + 1;

            start = findBeginning( input, lastPlace );
            if( -1 == start )
            {
                break;
            }

            end = findEnding( input, start );
        }

        sb.append( input.substring( lastPlace, length ) );

        return sb.toString();
    }

    /**
     * Helper method to find the start of a input definition.
     *
     * @param input the input string
     * @param index the current index into input
     * @return the index of start of property definition
     */
    private int findBeginning( final String input, final int index )
    {
        return input.indexOf( "${", index );
    }

    /**
     * Helper method to find the end of a property definition.
     *
     * @param input the input string
     * @param index the current index into input
     * @return the index of end of property definition
     * @throws Exception if unable to locate end of property
     */
    private int findEnding( final String input, final int index )
        throws Exception
    {
        final int end = input.indexOf( '}', index );
        if( -1 == end )
        {
            final String message = "Malformed input with mismatched }'s";
            throw new Exception( message );
        }

        return end;
    }

    /**
     * Return value from data map. If null is retrieved
     * from data map then behave as defined by
     * {@link #m_onUndefined} flag.
     *
     * @param key the key of value in data
     * @param data the data map
     * @return the stringized value of value in map
     * @exception Exception if an error occurs
     */
    private String getValue( final String key, final Map data )
        throws Exception
    {
        final Object value = data.get( key );
        if( null == value )
        {
            if( LEAVE_UNDEFINED == m_onUndefined )
            {
                return "${" + key + "}";
            }
            else if( EMPTY_ON_UNDEFINED == m_onUndefined )
            {
                return "";
            }
            else// if( EXCEPT_ON_UNDEFINED == m_onUndefined )
            {
                final String message = "Unable to find " + key +
                    " to expand during property resolution.";
                throw new Exception( message );
            }
        }

        return String.valueOf( value );
    }
}
