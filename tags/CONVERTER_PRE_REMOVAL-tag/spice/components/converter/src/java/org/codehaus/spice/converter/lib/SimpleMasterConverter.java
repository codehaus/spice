/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.converter.lib;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import org.codehaus.spice.converter.AbstractMasterConverter;

/**
 * A very simple master converter that is capable of using
 * any of the converters in this package.
 *
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2003-12-02 08:37:56 $
 */
public class SimpleMasterConverter
    extends AbstractMasterConverter
{
    /**
     * Construct the MasterConverter and register
     * all the converters that exist in this package.
     */
    public SimpleMasterConverter()
    {
        registerConverter( new SimpleConverterFactory( ObjectToStringConverter.class ),
                           Object.class.getName(),
                           String.class.getName() );
        registerConverter( new SimpleConverterFactory( StringToBooleanConverter.class ),
                           String.class.getName(),
                           Boolean.class.getName() );
        registerConverter( new SimpleConverterFactory( StringToBooleanConverter.class ),
                           String.class.getName(),
                           Boolean.TYPE.getName() );
        registerConverter( new SimpleConverterFactory( StringToByteConverter.class ),
                           String.class.getName(),
                           Byte.class.getName() );
        registerConverter( new SimpleConverterFactory( StringToByteConverter.class ),
                           String.class.getName(),
                           Byte.TYPE.getName() );
        registerConverter( new SimpleConverterFactory( StringToClassConverter.class ),
                           String.class.getName(),
                           Class.class.getName() );
        registerConverter( new SimpleConverterFactory( StringToDoubleConverter.class ),
                           String.class.getName(),
                           Double.class.getName() );
        registerConverter( new SimpleConverterFactory( StringToDoubleConverter.class ),
                           String.class.getName(),
                           Double.TYPE.getName() );
        registerConverter( new SimpleConverterFactory( StringToFloatConverter.class ),
                           String.class.getName(),
                           Float.class.getName() );
        registerConverter( new SimpleConverterFactory( StringToFloatConverter.class ),
                           String.class.getName(),
                           Float.TYPE.getName() );
        registerConverter( new SimpleConverterFactory( StringToIntegerConverter.class ),
                           String.class.getName(),
                           Integer.class.getName() );
        registerConverter( new SimpleConverterFactory( StringToIntegerConverter.class ),
                           String.class.getName(),
                           Integer.TYPE.getName() );
        registerConverter( new SimpleConverterFactory( StringToLongConverter.class ),
                           String.class.getName(),
                           Long.class.getName() );
        registerConverter( new SimpleConverterFactory( StringToLongConverter.class ),
                           String.class.getName(),
                           Long.TYPE.getName() );
        registerConverter( new SimpleConverterFactory( StringToShortConverter.class ),
                           String.class.getName(),
                           Short.class.getName() );
        registerConverter( new SimpleConverterFactory( StringToShortConverter.class ),
                           String.class.getName(),
                           Short.TYPE.getName() );
        registerConverter( new SimpleConverterFactory( StringToURLConverter.class ),
                           String.class.getName(),
                           URL.class.getName() );
        registerConverter( new SimpleConverterFactory( StringToDateConverter.class ),
                           String.class.getName(),
                           Date.class.getName() );
        registerConverter( new SimpleConverterFactory( StringToSQLDateConverter.class ),
                           String.class.getName(),
                           java.sql.Date.class.getName() );
        registerConverter( new SimpleConverterFactory( StringToBigDecimalConverter.class ),
                           String.class.getName(),
                           BigDecimal.class.getName() );
        registerConverter( new SimpleConverterFactory( StringToBigIntegerConverter.class ),
                           String.class.getName(),
                           BigInteger.class.getName() );
        registerConverter( new SimpleConverterFactory( StringToTimeConverter.class ),
                           String.class.getName(),
                           Time.class.getName() );
        registerConverter( new SimpleConverterFactory( StringToTimestampConverter.class ),
                           String.class.getName(),
                           Timestamp.class.getName() );
    }
}
