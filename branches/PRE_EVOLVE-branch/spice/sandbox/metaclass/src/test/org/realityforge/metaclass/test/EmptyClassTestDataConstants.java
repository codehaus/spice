/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.test;

import org.realityforge.metaclass.model.Attribute;

/**
 * Constants for EmptyClass TestCases
 */
public interface EmptyClassTestDataConstants
{
    String CLASS_NAME = "org.realityforge.metaclass.test.data.EmptyClass";

    // there seems not to be a Modifier.NONE
    int CLASS_MODIFIER = 0;

    Attribute[] CLASS_ATTRIBUTES = new Attribute[]{};
}
