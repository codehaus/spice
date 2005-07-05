package org.codehaus.spice.jervlet.impl;

import org.codehaus.spice.jervlet.ContextException;
import org.codehaus.spice.jervlet.ContextMonitor;
import org.codehaus.spice.jervlet.Context;

/**
 * Context monitor doing absolutely nothing,
 * but throwing exceptions.
 *
 * @author Johan Sjoberg
 */
public class NoopContextMonitor implements ContextMonitor
{
    /**
     * Report that an Exception was thrown when adding a Context
     *
     * @param reportingClass Class reporting the error
     * @param context        Context that couldn't be added
     * @param e              Exception that was thrown
     * @throws org.codehaus.spice.jervlet.ContextException
     *          A new exception describing the reported error
     */
    public void addContextException( Class reportingClass, Context context, Exception e )
        throws ContextException
    {
        throwContextException( e );
    }

    /**
     * Report that an Exception was thrown when removing a Context
     *
     * @param reportingClass Class reporting the error
     * @param context        Context that couldn't be removed
     * @param e              Exception that was thrown
     * @throws org.codehaus.spice.jervlet.ContextException
     *          A new exception describing the reported error
     */
    public void removeContextException( Class reportingClass, Context context, Exception e )
        throws ContextException
    {
        throwContextException( e );
    }

    /**
     * Report that an Exception was thrown when starting a Context
     *
     * @param reportingClass Class reporting the error
     * @param context        Context that couldn't be started
     * @param e              Exception that was thrown
     * @throws org.codehaus.spice.jervlet.ContextException
     *          A new exception describing the error
     */
    public void startContextException( Class reportingClass, Context context, Exception e )
        throws ContextException
    {
        throwContextException( e );
    }

    /**
     * Report that an Exception was thrown when stopping a Context
     *
     * @param reportingClass Class reporting the error
     * @param context        Context that couldn't be stopped
     * @param e              Exception that was thrown
     * @throws org.codehaus.spice.jervlet.ContextException
     *          A new exception decribing the error
     */
    public void stopContextException( Class reportingClass, Context context, Exception e )
        throws ContextException
    {
        throwContextException( e );
    }

    /**
     * Report a warning about the addition of a Context
     *
     * @param reportingClass Class reporting the warning
     * @param context        Context the warning conserns
     * @param message        A message describing the warning
     */
    public void addContextWarning( Class reportingClass, Context context, String message )
    {
    }

    /**
     * Report a warning about the removal of a Context
     *
     * @param reportingClass Class reporting the warning
     * @param context        Context the warning conserns
     * @param message        A message describing the warning
     */
    public void removeContextWarning( Class reportingClass, Context context, String message )
    {
    }

    /**
     * Report a warning about starting a Context
     *
     * @param reportingClass Class reporting the warning
     * @param context        Context the warning conserns
     * @param message        A message describing the warning
     */
    public void startContextWarning( Class reportingClass, Context context, String message )
    {
    }

    /**
     * Report a warning about stopping a Context
     *
     * @param reportingClass Class reporting the warning
     * @param context        Context the warning conserns
     * @param message        A message describing the warning
     */
    public void stopContextWarning( Class reportingClass, Context context, String message )
    {
    }

    /**
     * Report that a Context was added
     *
     * @param reportingClass Class reporting the additon
     * @param context        Context that was added
     */
    public void addContextNotification( Class reportingClass, Context context )
    {
    }

    /**
     * Report that a Context was removed
     *
     * @param reportingClass Class reporting the removal
     * @param context        Context that was removed
     */
    public void removeContextNotification( Class reportingClass, Context context )
    {
    }

    /**
     * Report that a Context was started
     *
     * @param reportingClass Reporting class
     * @param context        Context that was started
     */
    public void startContextNotification( Class reportingClass, Context context )
    {
    }

    /**
     * Report that a Context was stopped
     *
     * @param reportingClass Reporting class
     * @param context        Context that was stopped
     */
    public void stopContextNotification( Class reportingClass, Context context )
    {
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
}
