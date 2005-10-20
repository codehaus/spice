package org.codehaus.spice.jervlet.impl;

import org.codehaus.spice.jervlet.ListenerMonitor;
import org.codehaus.spice.jervlet.Listener;
import org.codehaus.spice.jervlet.ListenerException;

/**
 * Listener monitor doing absolutely nothing,
 * but throwing exceptions when asked to.
 *
 * @author Johan Sjoberg
 */
public class NoopListenerMonitor implements ListenerMonitor
{
    /**
     * Notify that an Exception was thrown when adding a Listener.
     *
     * @param reportingClass The class reporting the exception
     * @param listener The listener that qouldn't be started
     * @param e The exception that was thrown
     * @throws ListenerException A newly created ListenerException representing the reported error
     */
    public void addListenerException( Class reportingClass, Listener listener, Exception e )
        throws ListenerException
    {
        throwListenerException( e );
    }

    /**
     * Notify that an Exception was thrown when removing a Listener.
     *
     * @param reportingClass The class reporting the exception
     * @param listener The listener that qouldn't be started
     * @param e The exception that was thrown
     * @throws ListenerException A newly created ListenerException representing the reported error
     */
    public void removeListenerException( Class reportingClass, Listener listener, Exception e )
        throws ListenerException
    {
        throwListenerException( e );
    }

    /**
     * Report a warning about adding a listener
     *
     * @param reportingClass The calass reporting the warning
     * @param listener Listener the warning concerns
     * @param message A message describing the warning
     */
    public void addListenerWarning( Class reportingClass, Listener listener, String message )
    {
    }

    /**
     * Report a warning about removing a listener
     *
     * @param reportingClass The calass reporting the warning
     * @param listener Listener the warning concerns
     * @param message A message describing the warning
     */
    public void removeListenerWarning( Class reportingClass, Listener listener, String message )
    {
    }

    /**
     * Report that a listener has been added
     *
     * @param reportingClass The class reporting the listener start event
     * @param listener The listener that was started
     */
    public void addListenerNotification( Class reportingClass, Listener listener )
    {
    }

    /**
     * Report that a listner has been removed
     *
     * @param reportingClass The class reporting the listener stop event
     * @param listener The listener that was stopped
     */
    public void removeListenerNotification( Class reportingClass, Listener listener )
    {
    }

    /**
     * Notify that an Exception was thrown when starting a Listener.
     *
     * @param reportingClass The class reporting the exception
     * @param listener The listener that qouldn't be started
     * @param e The exception that was thrown
     * @throws ListenerException A newly created ListenerException representing the reported error
     */
    public void startListenerException( Class reportingClass, Listener listener, Exception e )
        throws ListenerException
    {
        throwListenerException( e );
    }

    /**
     * Notify that an Exception was thrown when stopping a Listener.
     *
     * @param reportingClass The class reporting the exception
     * @param listener The listener that qouldn't be stopped
     * @param e The exception that was thrown
     * @throws ListenerException A newly created ListenerException representing the reported error
     */
    public void stopListenerException( Class reportingClass, Listener listener, Exception e )
        throws ListenerException
    {
        throwListenerException( e );
    }

    /**
     * Report a warning about starting a listener
     *
     * @param reportingClass The calass reporting the warning
     * @param listener Listener the warning concerns
     * @param message A message describing the warning
     */
    public void startListenerWarning( Class reportingClass, Listener listener, String message )
    {
    }

    /**
     * Report a warning about stopping a listener
     *
     * @param reportingClass The calass reporting the warning
     * @param listener Listener the warning concerns
     * @param message A message describing the warning
     */
    public void stopListenerWarning( Class reportingClass, Listener listener, String message )
    {
    }

    /**
     * Report that a listener has been started
     *
     * @param reportingClass The class reporting the listener start event
     * @param listner The listener that was started
     */
    public void startListenerNotification( Class reportingClass, Listener listner )
    {
    }

    /**
     * Report that a listner has been stopped
     *
     * @param reportingClass The class reporting the listener stop event
     * @param listener The listener that was stopped
     */
    public void stopListenerNotification( Class reportingClass, Listener listener )
    {
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
}
