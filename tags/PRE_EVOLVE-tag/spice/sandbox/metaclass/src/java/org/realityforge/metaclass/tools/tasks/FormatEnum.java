/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.tools.tasks;

import org.apache.tools.ant.types.EnumeratedAttribute;

/**
 * This is an enumeration that gives the option of either
 * outputting as xml or as a serialized format.
 *
 * @author <a href="mailto:peter at apache.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-04-16 10:40:47 $
 */
public class FormatEnum
    extends EnumeratedAttribute
{
    public int getTypeCode()
    {
        final String value = super.getValue();
        if( value.equals( "binary") )
        {
            return MetaGenerateTask.BINARY_TYPE;
        }
        else if( value.equals( "ser" ) )
        {
            return MetaGenerateTask.SER_TYPE;
        }
        else
        {
            return MetaGenerateTask.XML_TYPE;
        }
    }

    public String[] getValues()
    {
        return new String[]{"xml", "ser", "legacy"};
    }
}
