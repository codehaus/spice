/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.jervlet;

/**
 * Context monitor
 * <br/><br/>
 * This interface defines possible monitor events from Contexts.
 * 
 * @author Johan Sjoberg
 */
public interface ContextMonitor
{
    /** Role, used in some component frameworks */
    String ROLE = ContextMonitor.class.getName();

    /**
     * Report that an Exception was thrown when adding a Context
     *
     * @param reportingClass Class reporting the error
     * @param context Context that couldn't be added
     * @param e Exception that was thrown
     * @throws ContextException A new exception describing the reported error
     */
    void addContextException( Class reportingClass, Context context, Exception e )
      throws ContextException;

    /**
     * Report that an Exception was thrown when removing a Context
     *
     * @param reportingClass Class reporting the error
     * @param context Context that couldn't be removed
     * @param e Exception that was thrown
     * @throws ContextException A new exception describing the reported error
     */
    void removeContextException( Class reportingClass, Context context, Exception e )
      throws ContextException;

    /**
     * Report that an Exception was thrown when starting a Context
     *
     * @param reportingClass Class reporting the error
     * @param context Context that couldn't be started
     * @param e Exception that was thrown
     * @throws ContextException A new exception describing the error
     */
    void startContextException( Class reportingClass, Context context, Exception e )
      throws ContextException;

    /**
     * Report that an Exception was thrown when stopping a Context
     *
     * @param reportingClass Class reporting the error
     * @param context Context that couldn't be stopped
     * @param e Exception that was thrown
     * @throws ContextException A new exception decribing the error
     */
    void stopContextException( Class reportingClass, Context context, Exception e )
      throws ContextException;

    /**
     * Report a warning about the addition of a Context
     *
     * @param reportingClass Class reporting the warning
     * @param context Context the warning conserns
     * @param message A message describing the warning
     */
    void addContextWarning( Class reportingClass, Context context, String message );

    /**
     * Report a warning about the removal of a Context
     *
     * @param reportingClass Class reporting the warning
     * @param context Context the warning conserns
     * @param message A message describing the warning
     */
    void removeContextWarning( Class reportingClass, Context context, String message );

    /**
     * Report a warning about starting a Context
     *
     * @param reportingClass Class reporting the warning
     * @param context Context the warning conserns
     * @param message A message describing the warning
     */
    void startContextWarning( Class reportingClass, Context context, String message );

    /**
     * Report a warning about stopping a Context
     *
     * @param reportingClass Class reporting the warning
     * @param context Context the warning conserns
     * @param message A message describing the warning
     */
    void stopContextWarning( Class reportingClass, Context context, String message );

    /**
     * Report that a Context was added
     *
     * @param reportingClass Class reporting the additon
     * @param context Context that was added
     */
    void addContextNotification( Class reportingClass, Context context );

    /**
     * Report that a Context was removed
     *
     * @param reportingClass Class reporting the removal
     * @param context Context that was removed
     */
    void removeContextNotification( Class reportingClass, Context context );

    /**
     * Report that a Context was started
     *
     * @param reportingClass Reporting class
     * @param context Context that was started
     */
    void startContextNotification( Class reportingClass, Context context );

    /**
     * Report that a Context was stopped
     *
     * @param reportingClass Reporting class
     * @param context Context that was stopped
     */
    void stopContextNotification( Class reportingClass, Context context );
}
