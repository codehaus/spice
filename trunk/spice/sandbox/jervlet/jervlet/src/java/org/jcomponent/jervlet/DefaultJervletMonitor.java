/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */

package org.jcomponent.jervlet;

public class DefaultJervletMonitor implements JervletMonitor {

    public void startException(Class reportingClass, String context, Exception e) throws JervletException {
        final String msg = "Unable to start '" + context + "' : " + e.getMessage();
        throw new JervletException(msg, e);
    }

    public void stopException(Class reportingClass, String context, Exception e) throws JervletException {
        final String msg = "Unable to stop '" + context + "' : " + e.getMessage();
        throw new JervletException(msg, e);
    }

    public void redeployException(Class reportingClass, String context, JervletException e) throws JervletException {
        //getLogger().error( "Exception restarting: " + context, e );
        throw e;
    }

    public void deployingContext(Class reportingClass, String context, String webappUrl, String hostName) {
//        if( getLogger().isInfoEnabled() )
//            getLogger().info( "deploying context=" + context + ", webapp="
//                              + holder.getWebappUrl() + " to host="
//                              + ( m_hostName == null ? "(All Hosts)" : m_hostName ) );

    }

    public void undeployException(Class reportingClass, String context, Exception e) throws JervletException {
        final String msg = "Problem stopping web application in Jetty for context '" + context + "'";
        throw new JervletException(msg, e);
    }

    public void deployingContextException(Class reportingClass, String context, String webappUrl, String hostName, Exception e) throws JervletException {
        final String msg = "Unable to deploy '" + context + "' : " + e.getMessage();
        throw new JervletException(msg, e);
    }

    public void undeployWarning(Class reportingClass, String context, String msg) {
        final String emsg = "Unable to deploy '" + context + "' Reason : " + msg;
        // do something with it ?

    }

}
