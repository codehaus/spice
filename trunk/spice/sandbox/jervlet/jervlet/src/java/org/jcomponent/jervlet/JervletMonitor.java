package org.jcomponent.jervlet;

public interface JervletMonitor {
    void startException(Class reportingClass, String context, Exception e) throws JervletException;

    void stopException(Class reportingClass, String context, Exception e) throws JervletException;

    void redeployException(Class reportingClass, String context, JervletException e) throws JervletException;

    void deployingContext(Class reportingClass, String context, String webappUrl, String hostName);

    void undeployException(Class reportingClass, String context, Exception e) throws JervletException;

    void deployingContextException(Class reportingClass, String context, String webappUrl, String hostName, Exception e) throws JervletException;

    void undeployWarning(Class reportingClass, String context, String msg);

}
