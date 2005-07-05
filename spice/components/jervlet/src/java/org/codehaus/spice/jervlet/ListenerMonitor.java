/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.jervlet;

/**
 * Listener monitor
 * <br/><br/>
 * This interface defines possible monitor events form a Listener.
 * 
 * @author Johan Sjoberg
 */
public interface ListenerMonitor
{
    /**
     * Notify that an Exception was thrown when adding a Listener.
     *
     * @param reportingClass The class reporting the exception
     * @param listener The listener that qouldn't be started
     * @param e The exception that was thrown
     * @throws ListenerException A newly created ListenerException
     *         representing the reported error
     */
    void addListenerException( Class reportingClass, Listener listener, Exception e )
        throws ListenerException;

    /**
     * Notify that an Exception was thrown when removing a Listener.
     *
     * @param reportingClass The class reporting the exception
     * @param listener The listener that qouldn't be started
     * @param e The exception that was thrown
     * @throws ListenerException A newly created ListenerException
     *         representing the reported error
     */
    void removeListenerException( Class reportingClass, Listener listener, Exception e )
        throws ListenerException;

    /**
     * Report a warning about adding a listener
     *
     * @param reportingClass The calass reporting the warning
     * @param listener Listener the warning concerns
     * @param message A message describing the warning
     */
    void addListenerWarning( Class reportingClass, Listener listener, String message );

    /**
     * Report a warning about removing a listener
     *
     * @param reportingClass The calass reporting the warning
     * @param listener Listener the warning concerns
     * @param message A message describing the warning
     */
    void removeListenerWarning( Class reportingClass, Listener listener, String message );

    /**
     * Report that a listener has been added
     *
     * @param reportingClass The class reporting the listener start event
     * @param listener The listener that was started
     */
    void addListenerNotification( Class reportingClass, Listener listener );

    /**
     * Report that a listner has been removed
     *
     * @param reportingClass  The class reporting the listener stop event
     * @param listener The listener that was stopped
     */
    void removeListenerNotification( Class reportingClass, Listener listener );

    /**
     * Notify that an Exception was thrown when starting a Listener.
     *
     * @param reportingClass The class reporting the exception
     * @param listener The listener that qouldn't be started
     * @param e The exception that was thrown
     * @throws ListenerException A newly created ListenerException
     *         representing the reported error
     */
    void startListenerException( Class reportingClass, Listener listener, Exception e )
        throws ListenerException;

    /**
     * Notify that an Exception was thrown when stopping a Listener.
     *
     * @param reportingClass The class reporting the exception
     * @param listener The listener that qouldn't be stopped
     * @param e The exception that was thrown
     * @throws ListenerException A newly created ListenerException
     *         representing the reported error
     */
    void stopListenerException( Class reportingClass, Listener listener, Exception e )
        throws ListenerException;

    /**
     * Report a warning about starting a listener
     *
     * @param reportingClass The calass reporting the warning
     * @param listener Listener the warning concerns
     * @param message A message describing the warning
     */
    void startListenerWarning( Class reportingClass, Listener listener, String message );

    /**
     * Report a warning about stopping a listener
     *
     * @param reportingClass The calass reporting the warning
     * @param listener Listener the warning concerns
     * @param message A message describing the warning
     */
    void stopListenerWarning( Class reportingClass, Listener listener, String message );

    /**
     * Report that a listener has been started
     *
     * @param reportingClass The class reporting the listener start event
     * @param listener The listener that was started
     */
    void startListenerNotification( Class reportingClass, Listener listener );

    /**
     * Report that a listner has been stopped
     *
     * @param reportingClass  The class reporting the listener stop event
     * @param listener The listener that was stopped
     */
    void stopListenerNotification( Class reportingClass, Listener listener );
}