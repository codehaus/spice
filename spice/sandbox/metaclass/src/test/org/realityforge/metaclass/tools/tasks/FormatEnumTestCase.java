/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.tools.tasks;

import junit.framework.TestCase;
import org.apache.tools.ant.BuildException;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-10-04 01:19:17 $
 */
public class FormatEnumTestCase
    extends TestCase
{
    public void testBinaryFormat()
        throws Exception
    {
        final FormatEnum enum = new FormatEnum();
        enum.setValue( "binary" );
        assertEquals( GenerateClassDescriptorsTask.BINARY_TYPE,
                      enum.getTypeCode() );
    }

    public void testSerializedFormat()
        throws Exception
    {
        final FormatEnum enum = new FormatEnum();
        enum.setValue( "ser" );
        assertEquals( GenerateClassDescriptorsTask.SER_TYPE,
                      enum.getTypeCode() );
    }

    public void testXMLFormat()
        throws Exception
    {
        final FormatEnum enum = new FormatEnum();
        enum.setValue( "xml" );
        assertEquals( GenerateClassDescriptorsTask.XML_TYPE,
                      enum.getTypeCode() );
    }

    public void testUnknownFormat()
        throws Exception
    {
        final FormatEnum enum = new FormatEnum();
        try
        {
            enum.setValue( "unknown" );
        }
        catch( BuildException e )
        {
            return;
        }
        fail( "Expected to fail to set unknown type" );
    }
}
