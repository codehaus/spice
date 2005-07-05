/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.jervlet.impl.avalon;

import org.codehaus.spice.jervlet.Context;
import org.codehaus.spice.jervlet.ContextException;
import org.codehaus.spice.jervlet.ContextMonitor;
import org.apache.avalon.framework.logger.LogEnabled;
import org.apache.avalon.framework.logger.Logger;

/**
 * Avalon style, logging context monitor
 * <br/><br/>
 * The format of the log file is: "Operation message" "Reporting class"
 * "Context" "Message". Message is empty for notifications.
 *
 * @author Johan Sjoberg
 */
public class AvalonContextMonitor implements ContextMonitor, LogEnabled
{
    /** Avalon logger recieving all log events */
    private Logger m_logger;

    /**
     * @see org.codehaus.spice.jervlet.ContextMonitor
     * @param reportingClass
     * @param context
     * @param e
     * @throws ContextException
     */
    public void addContextException( Class reportingClass, Context context, Exception e )
        throws ContextException
    {
        final String message = "\"Got exception when adding context\" "
          + getExceptionMessage( reportingClass, context, e );
        m_logger.error( message, e );
        throwContextException( e );
    }

    /**
     * @see org.codehaus.spice.jervlet.ContextMonitor
     * @param reportingClass
     * @param context
     * @param e
     * @throws ContextException
     */
    public void removeContextException( Class reportingClass, Context context, Exception e )
        throws ContextException
    {
        final String message =  "\"Got exception when removing context\" "
          + getExceptionMessage( reportingClass, context, e );
        m_logger.error( message, e );
        throwContextException( e );
    }

    /**
     * @see org.codehaus.spice.jervlet.ContextMonitor
     * @param reportingClass
     * @param context
     * @param e
     * @throws ContextException
     */
    public void startContextException( Class reportingClass, Context context, Exception e )
        throws ContextException
    {
        final String message =  "\"Got exception when starting context\" "
          + getExceptionMessage( reportingClass, context, e );
        m_logger.error( message, e );
        throwContextException( e );
    }

    /**
     * @see org.codehaus.spice.jervlet.ContextMonitor
     * @param reportingClass
     * @param context
     * @param e
     * @throws ContextException
     */
    public void stopContextException( Class reportingClass, Context context, Exception e )
        throws ContextException
    {
        final String message =  "\"Got exception when stopping context\" "
          + getExceptionMessage( reportingClass, context, e );
        m_logger.error( message, e );
        throwContextException( e );
    }

    /**
     * @see org.codehaus.spice.jervlet.ContextMonitor
     * @param reportingClass
     * @param context
     * @param message
     */
    public void addContextWarning( Class reportingClass, Context context, String message )
    {
        final String logMessage =  "\"Context addition warning\" "
          + getWarning( reportingClass, context, message );
        m_logger.warn( logMessage );
    }

    /**
     * @see org.codehaus.spice.jervlet.ContextMonitor
     * @param reportingClass
     * @param context
     * @param message
     */
    public void removeContextWarning( Class reportingClass, Context context, String message )
    {
        final String logMessage = "\"Context removal warning\" "
          + getWarning( reportingClass, context, message );
        m_logger.warn( logMessage );
    }

    /**
     * @see org.codehaus.spice.jervlet.ContextMonitor
     * @param reportingClass
     * @param context
     * @param message
     */
    public void startContextWarning( Class reportingClass, Context context, String message )
    {
        final String logMessage = "\"Context start warning\" "
          + getWarning( reportingClass, context, message );
        m_logger.warn( logMessage );
    }

    /**
     * @see org.codehaus.spice.jervlet.ContextMonitor
     * @param reportingClass
     * @param context
     * @param message
     */
    public void stopContextWarning( Class reportingClass, Context context, String message )
    {
        final String logMessage = "\"Context stop warning\" "
          + getWarning( reportingClass, context, message );
        m_logger.warn( logMessage );
    }

    /**
     * @see org.codehaus.spice.jervlet.ContextMonitor
     * @param reportingClass
     * @param context
     */
    public void addContextNotification( Class reportingClass, Context context )
    {
        final String message = "\"Adding context\" "
          + getNotification( reportingClass, context );
        m_logger.info( message );
    }

    /**
     * @see org.codehaus.spice.jervlet.ContextMonitor
     * @param reportingClass
     * @param context
     */
    public void removeContextNotification( Class reportingClass, Context context )
    {
        final String message = "\"Removing context\" "
          + getNotification( reportingClass, context );
        m_logger.info( message );
    }

    /**
     * @see org.codehaus.spice.jervlet.ContextMonitor
     * @param reportingClass
     * @param context
     */
    public void startContextNotification( Class reportingClass, Context context )
    {
        final String message = "\"Starting context\" "
          + getNotification( reportingClass, context );
        m_logger.info( message );
    }

    /**
     * @see org.codehaus.spice.jervlet.ContextMonitor
     * @param reportingClass
     * @param context
     */
    public void stopContextNotification( Class reportingClass, Context context )
    {
        final String message = "\"Stopping context\" "
          + getNotification( reportingClass, context );
        m_logger.info( message );
    }

    /**
     * Get the logger. All monitor events vill be logger to this logger.
     *
     * @param logger The logger to use for monitor events
     * @dna.logger
     */
    public void enableLogging( Logger logger )
    {
        m_logger = logger;
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
        ContextException contextException =
          new ContextException( e.getMessage(), e.getCause() );
        throw contextException;
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
