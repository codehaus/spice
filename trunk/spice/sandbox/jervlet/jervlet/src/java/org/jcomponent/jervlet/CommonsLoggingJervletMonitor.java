/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */

package org.jcomponent.jervlet;

import org.apache.commons.logging.LogFactory;

public class CommonsLoggingJervletMonitor extends DefaultJervletMonitor {

    public void startException(Class reportingClass, String context, Exception e) throws JervletException {
        LogFactory.getFactory().getInstance(reportingClass).error("Exception starting Context '"+ context+"' :" + e.getMessage(),e);
        super.startException(reportingClass, context, e);
    }

    public void stopException(Class reportingClass, String context, Exception e) throws JervletException {
        LogFactory.getFactory().getInstance(reportingClass).error("Exception stopping Context '"+ context+"' :" + e.getMessage(),e);
        super.stopException(reportingClass,context,e );
    }

    public void redeployException(Class reportingClass, String context, JervletException e) throws JervletException {
        LogFactory.getFactory().getInstance(reportingClass).error("Exception redeploying Context '"+ context+"' :" + e.getMessage(),e);
        super.redeployException(reportingClass, context, e);
    }

    public void deployingContext(Class reportingClass, String context, String webappUrl, String hostName) {
        LogFactory.getFactory().getInstance(reportingClass).info("Deploying Context '"+ context
                +"', WebAppUrl '"+webappUrl
                +"', HostName '"+ hostName(hostName) +"'");
        super.deployingContext(reportingClass, context, webappUrl, hostName);
    }
    private String hostName(String hostName) {
        return (hostName == null ? "(All Hosts)" : hostName);
    }

    public void undeployException(Class reportingClass, String context, Exception e) throws JervletException {
        LogFactory.getFactory().getInstance(reportingClass).error("Exception undeploying Context '"+ context+"' :" + e.getMessage(),e);
        super.undeployException(reportingClass, context, e);
    }

    public void deployingException(Class reportingClass, String context, String webappUrl, String hostName, Exception e) throws JervletException {
        LogFactory.getFactory().getInstance(reportingClass).error("Exception deploying Context '"+ context
                +"', WebAppUrl '"+webappUrl
                +"', HostName '"+ hostName(hostName) +"'");
        super.deployingException(reportingClass, context, webappUrl, hostName, e);
    }

    public void undeployWarning(Class reportingClass, String context, String msg) {
        LogFactory.getFactory().getInstance(reportingClass).warn("Warning undeploying Context '"+ context
                +"' Message: '" + msg +"'");
        super.undeployWarning(reportingClass, context, msg);
    }
}
