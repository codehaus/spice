/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.jervlet.impl;

import org.codehaus.spice.jervlet.ContextException;
import org.codehaus.spice.jervlet.ContextMonitor;
import org.codehaus.spice.jervlet.Context;

/**
 * Context monitor writing to System.out and System.err
 *
 * @author Johan Sjoberg
 */
public class SystemOutContextMonitor implements ContextMonitor
{

    /**
     * Report that an Exception was thrown when adding a Context
     *
     * @param reportingClass Class reporting the error
     * @param context Context that couldn't be added
     * @param e Exception that was thrown
     * @throws org.codehaus.spice.jervlet.ContextException A new exception describing the reported error
     */
    public void addContextException( Class reportingClass, Context context, Exception e )
      throws ContextException
    {
        final String message = "\"Got exception when adding context\" "
          + getExceptionMessage( reportingClass, context, e );
        System.err.println( message );
        throwContextException( e );
    }

    /**
     * Report that an Exception was thrown when removing a Context
     *
     * @param reportingClass Class reporting the error
     * @param context Context that couldn't be removed
     * @param e Exception that was thrown
     * @throws org.codehaus.spice.jervlet.ContextException A new exception describing the reported error
     */
    public void removeContextException( Class reportingClass, Context context, Exception e )
      throws ContextException
    {
        final String message =  "\"Got exception when removing context\" "
          + getExceptionMessage( reportingClass, context, e );
        System.err.println( message );
        throwContextException( e );
    }

    /**
     * Report that an Exception was thrown when starting a Context
     *
     * @param reportingClass Class reporting the error
     * @param context Context that couldn't be started
     * @param e Exception that was thrown
     * @throws org.codehaus.spice.jervlet.ContextException A new exception describing the error
     */
    public void startContextException( Class reportingClass, Context context, Exception e )
      throws ContextException
    {
        final String message =  "\"Got exception when starting context\" "
          + getExceptionMessage( reportingClass, context, e );
        System.err.println( message );
        throwContextException( e );
    }

    /**
     * Report that an Exception was thrown when stopping a Context
     *
     * @param reportingClass Class reporting the error
     * @param context Context that couldn't be stopped
     * @param e Exception that was thrown
     * @throws org.codehaus.spice.jervlet.ContextException A new exception decribing the error
     */
    public void stopContextException( Class reportingClass, Context context, Exception e )
      throws ContextException
    {
        final String message =  "\"Got exception when stopping context\" "
          + getExceptionMessage( reportingClass, context, e );
        System.err.println( message );
        throwContextException( e );
    }

    /**
     * Report a warning about the addition of a Context
     *
     * @param reportingClass Class reporting the warning
     * @param context Context the warning conserns
     * @param message A message describing the warning
     */
    public void addContextWarning( Class reportingClass, Context context, String message )
    {
        final String logMessage =  "\"Context addition warning\" "
          + getWarning( reportingClass, context, message );
        System.out.println( logMessage );
    }

    /**
     * Report a warning about the removal of a Context
     *
     * @param reportingClass Class reporting the warning
     * @param context Context the warning conserns
     * @param message A message describing the warning
     */
    public void removeContextWarning( Class reportingClass, Context context, String message )
    {
        final String logMessage = "\"Context removal warning\" "
          + getWarning( reportingClass, context, message );
        System.out.println( logMessage );
    }

    /**
     * Report a warning about starting a Context
     *
     * @param reportingClass Class reporting the warning
     * @param context Context the warning conserns
     * @param message A message describing the warning
     */
    public void startContextWarning( Class reportingClass, Context context, String message )
    {
        final String logMessage = "\"Context start warning\" "
          + getWarning( reportingClass, context, message );
        System.out.println( logMessage );
    }

    /**
     * Report a warning about stopping a Context
     *
     * @param reportingClass Class reporting the warning
     * @param context Context the warning conserns
     * @param message A message describing the warning
     */
    public void stopContextWarning( Class reportingClass, Context context, String message )
    {
        final String logMessage = "\"Context stop warning\" "
          + getWarning( reportingClass, context, message );
        System.out.println( logMessage );
    }

    /**
     * Report that a Context was added
     *
     * @param reportingClass Class reporting the additon
     * @param context Context that was added
     */
    public void addContextNotification( Class reportingClass, Context context )
    {
        final String message = "\"Added context\" "
          + getNotification( reportingClass, context );
        System.out.println( message );
    }

    /**
     * Report that a Context was removed
     *
     * @param reportingClass Class reporting the removal
     * @param context Context that was removed
     */
    public void removeContextNotification( Class reportingClass, Context context )
    {
        final String message = "\"Removed context\" "
          + getNotification( reportingClass, context );
        System.out.println( message );
    }

    /**
     * Report that a Context was started
     *
     * @param reportingClass Reporting class
     * @param context Context that was started
     */
    public void startContextNotification( Class reportingClass, Context context )
    {
        final String message = "\"Started context\" "
          + getNotification( reportingClass, context );
        System.out.println( message );
    }

    /**
     * Report that a Context was stopped
     *
     * @param reportingClass Reporting class
     * @param context Context that was stopped
     */
    public void stopContextNotification( Class reportingClass, Context context )
    {
        final String message = "\"Stopped context\" "
          + getNotification( reportingClass, context );
        System.out.println( message );
    }
    /**
     * Throw a <code>ContextException</code>
     *
     * @param e Original Exception
     * @throws ContextException A new ListenerException instance
     */
    private void throwContextException( Exception e )
        throws ContextException
    {
        throw new ContextException( e.getMessage(), e.getCause() );
    }

    /**
     * Form an exception message
     *
     * @return a new Exception message
     */
    private String getExceptionMessage( Class reportingClass, Context context, Exception e )
    {
         return new StringBuffer().append( "\"" )
           .append( reportingClass.getName() ).append( "\" \"" )
           .append( context ).append( "\" \"" ).append( e.getMessage() )
           .append( "\"" ).toString();
    }

    /**
     * Form a message
     *
     * @return a new warning message
     */
    private String getWarning( Class reportingClass, Context context, String message )
    {
         return new StringBuffer().append( "\"" )
           .append( reportingClass.getName() ).append( "\" \"" )
           .append( context ).append( "\" \"" ).append( message )
           .append( "\"" ).toString();
    }

    /**
     * Form a notification
     *
     * @return a new notification message
     */
    private String getNotification( Class reportingClass, Context context )
    {
        return new StringBuffer().append( "\"" )
          .append( reportingClass.getName() )
          .append( "\" \"" ).append( context )
          .append( "\" \"\"" ).toString();
    }
}
