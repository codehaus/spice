/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */

package org.jcomponent.jervlet;

public interface JervletConfig {

    String getHostName();

    int getPort();

    int getMinThreads();

    int getMaxThreads();

    boolean getExtractWarFile();

}
