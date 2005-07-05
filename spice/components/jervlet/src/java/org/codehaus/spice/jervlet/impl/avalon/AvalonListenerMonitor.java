/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.jervlet.impl.avalon;

import org.apache.avalon.framework.logger.Logger;
import org.apache.avalon.framework.logger.LogEnabled;
import org.codehaus.spice.jervlet.ListenerMonitor;
import org.codehaus.spice.jervlet.Listener;
import org.codehaus.spice.jervlet.ListenerException;

/**
 * Avalon logging listener monitor
 * <br/><br/>
 * The format of the log file is: "Operation message" "Reporting class"
 * "Context" "Message". Message is empty for notifications.
 *
 * @author Johan Sjoberg
 */
public class AvalonListenerMonitor implements ListenerMonitor, LogEnabled
{
    /** Avalon logger recieving all log events */
    Logger m_logger;

    /**
     * Create an AvalonListenerMonitor
     */
    public AvalonListenerMonitor()
    {
    }

    /**
     * Create an AvalonListenerMonitor
     *
     * @param logger The Avalon logger to send monitor events to.
     */
    public AvalonListenerMonitor( final Logger logger )
    {
        m_logger = logger;
    }

    /**
     * Get an AvalonLogger. If the logger was given in the
     * constructor, this method has no meaning. It won't
     * be set twice.
     *
     * @param logger The logger to send monitor events to.
     */
    public void enableLogging( final Logger logger )
    {
        if( null == m_logger )
        {
            m_logger = logger;
        }
    }

    /**
     * Notify that an Exception was thrown when adding a Listener.
     * <br/><br/>
     * Log level: ERROR
     *
     * @param reportingClass The class reporting the exception
     * @param listener The listener that qouldn't be started
     * @param e The exception that was thrown
     * @throws ListenerException A newly created ListenerException representing the reported error
     */
    public void addListenerException( Class reportingClass, Listener listener, Exception e )
        throws ListenerException
    {
        final String message =  "\"Add listener exception\" "
          + getExceptionMessage( reportingClass, listener, e );
        m_logger.error( message, e );
        throwListenerException( e );
    }

    /**
     * Notify that an Exception was thrown when removing a Listener.
     * <br/><br/>
     * Log level: ERROR
     *
     * @param reportingClass The class reporting the exception
     * @param listener The listener that qouldn't be started
     * @param e The exception that was thrown
     * @throws ListenerException A newly created ListenerException representing the reported error
     */
    public void removeListenerException( Class reportingClass, Listener listener, Exception e )
        throws ListenerException
    {
        final String message =  "\"Remove listener exception\" "
          + getExceptionMessage( reportingClass, listener, e );
        m_logger.error( message, e );
        throwListenerException( e );
    }

    /**
     * Report a warning about adding a listener
     * <br/><br/>
     * Log level: WARNING
     *
     * @param reportingClass The calass reporting the warning
     * @param listener Listener the warning concerns
     * @param message A message describing the warning
     */
    public void addListenerWarning( Class reportingClass, Listener listener, String message )
    {
        final String logMessage =  "\"Add listener warning\" "
          + getWarning( reportingClass, listener, message );
        m_logger.warn( logMessage );
    }

    /**
     * Report a warning about removing a listener
     * <br/><br/>
     * Log level: WARNING
     *
     * @param reportingClass The calass reporting the warning
     * @param listener Listener the warning concerns
     * @param message A message describing the warning
     */
    public void removeListenerWarning( Class reportingClass, Listener listener, String message )
    {
        final String logMessage =  "\"Remove listener warning\" "
          + getWarning( reportingClass, listener, message );
        m_logger.warn( logMessage );
    }

    /**
     * Report that a listener has been added
     * <br/><br/>
     * Log level: INFO
     *
     * @param reportingClass The class reporting the listener start event
     * @param listener The listener that was started
     */
    public void addListenerNotification( Class reportingClass, Listener listener )
    {
        final String message = "\"Added listener\" "
          + getNotification( reportingClass, listener );
        m_logger.info( message );
    }

    /**
     * Report that a listner has been removed
     * <br/><br/>
     * Log level: INFO
     *
     * @param reportingClass The class reporting the listener stop event
     * @param listener The listener that was stopped
     */
    public void removeListenerNotification( Class reportingClass, Listener listener )
    {
        final String message = "\"Removed listener\" "
          + getNotification( reportingClass, listener );
        m_logger.info( message );
    }



    /**
     * Notify that an Exception was thrown when starting a Listener.
     * <br/><br/>
     * Log level: ERROR
     *
     * @param reportingClass The class reporting the exception
     * @param listener The listener that qouldn't be started
     * @param e The exception that was thrown
     * @throws ListenerException A newly created ListenerException
     *         representing the reported error
     */
    public void startListenerException( Class reportingClass, Listener listener, Exception e )
        throws ListenerException
    {
        final String message =  "\"Start listener exception\" "
          + getExceptionMessage( reportingClass, listener, e );;
        m_logger.error( message, e );
        throwListenerException( e );
    }

    /**
     * Notify that an Exception was thrown when stopping a Listener.
     * <br/><br/>
     * Log level: ERROR
     *
     * @param reportingClass The class reporting the exception
     * @param listener The listener that qouldn't be stopped
     * @param e The exception that was thrown
     * @throws ListenerException A newly created ListenerException
     *         representing the reported error
     */
    public void stopListenerException( Class reportingClass, Listener listener, Exception e )
        throws ListenerException
    {
        final String message =  "\"Stop listener exception\" "
          + getExceptionMessage( reportingClass, listener, e );
        m_logger.error( message, e );
        throwListenerException( e );
    }

    /**
     * Report a warning about starting a listener
     * <br/><br/>
     * Log level: WARNING
     *
     * @param reportingClass The calass reporting the warning
     * @param listener Listener the warning concerns
     * @param message A message describing the warning
     */
    public void startListenerWarning( Class reportingClass, Listener listener, String message )
    {
        final String logMessage =  "\"Start listener warning\" "
          + getWarning( reportingClass, listener, message );
        m_logger.warn( logMessage );
    }

    /**
     * Report a warning about stopping a listener
     * <br/><br/>
     * Log level: WARNING
     *
     * @param reportingClass The calass reporting the warning
     * @param listener Listener the warning concerns
     * @param message A message describing the warning
     */
    public void stopListenerWarning( Class reportingClass, Listener listener, String message )
    {
        final String logMessage =  "\"Stop listener warning\" "
          + getWarning( reportingClass, listener, message );
        m_logger.warn( logMessage );
    }

    /**
     * Report that a listener has been started
     * <br/><br/>
     * Log level: INFO
     *
     * @param reportingClass The class reporting the listener start event
     * @param listener The listener that was started
     */
    public void startListenerNotification( Class reportingClass, Listener listener )
    {
        final String message = "\"Started listener\" "
          + getNotification( reportingClass, listener );
        m_logger.info( message );
    }

    /**
     * Report that a listner has been stopped
     * <br/><br/>
     * Log level: INFO
     *
     * @param reportingClass The class reporting the listener stop event
     * @param listener The listener that was stopped
     */
    public void stopListenerNotification( Class reportingClass, Listener listener )
    {
        final String message = "\"Stopped listener\" "
          + getNotification( reportingClass, listener );
        m_logger.info( message );
    }

    /**
     * Throw a <code>ListenerException</code>
     *
     * @param e Original Exception
     * @throws ListenerException A new ListenerException instance
     */
    private void throwListenerException( Exception e )
        throws ListenerException
    {
        ListenerException listenerException =
          new ListenerException( e.getMessage(), e.getCause() );
        throw listenerException;
    }

    /**
     * Form an Exception message
     *
     * @return a new Exception message
     */
    private String getExceptionMessage( Class reportingClass, Listener listener, Exception e )
    {
         return new StringBuffer().append( "\"" ).append( reportingClass.getName() )
           .append( "\" \"" ).append( listener ).append( "\" \"" ).append( e.getMessage() )
           .append( "\"" ).toString();
    }

    /**
     * Form an message
     *
     * @return a new warning message
     */
    private String getWarning( Class reportingClass, Listener listener, String message )
    {
         return new StringBuffer().append( "\"" ).append( reportingClass.getName() )
           .append( "\" \"" ).append( listener ).append( "\" \"" ).append( message )
           .append( "\"" ).toString();
    }

    /**
     * Form a notification
     *
     * @return a new notification message
     */
    private String getNotification( Class reportingClass, Listener listener )
    {
        return new StringBuffer().append( "\"" ).append( reportingClass.getName() )
          .append( "\" \"" ).append( listener ).append( "\" \"\"" ).toString();
    }
}
