/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */

package org.jcomponent.jervlet;

public interface JervletMonitor {

    void startException(Class reportingClass, String context, Exception e) throws JervletException;

    void stopException(Class reportingClass, String context, Exception e) throws JervletException;

    void redeployException(Class reportingClass, String context, JervletException e) throws JervletException;

    void deployingContext(Class reportingClass, String context, String webappUrl, String hostName);

    void undeployException(Class reportingClass, String context, Exception e) throws JervletException;

    void deployingException(Class reportingClass, String context, String webappUrl, String hostName, Exception e) throws JervletException;

    void undeployWarning(Class reportingClass, String context, String msg);

}
