package org.jcomponent.jervlet;

import java.io.File;

public interface MultihostJervlet
{
    /** Role of the MultihostJervlet Service*/
    public String ROLE = MultihostJervlet.class.getName();

    /**
     * Deploy the given Web Application
     * @param context Context for the the webapp
     * @param pathToWebAppFolder path can be a war-archive or exploded directory
     * @throws JervletException Thrown when context already exists
     */
    void deploy( String host, String context, File pathToWebAppFolder ) throws JervletException;

    /**
     * Undeploy the given WebApp
     * @param context Context for the the webapp
     * @throws JervletException Thrown if context does NOT exist
     */
    void undeploy( String host, String context ) throws JervletException;
}
