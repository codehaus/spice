/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.message;

/**
 * Registrar that deals with {@link Destination}s.
 *
 * @author <a href="mailto:peter.royal@pobox.com">peter royal</a>
 */
public interface DestinationRegistrar
{
    /**
     * Register a destination
     *
     * @param destination Destination to register. required.
     *
     * @throws DuplicateRegistrationException If this destination has already been registered
     */
    void register( Destination destination ) throws DuplicateRegistrationException;

    /**
     * Unregister a destination
     *
     * @param destination Destination to unregister. required.
     *
     * @throws NoSuchDestinationException If this destination has never been registered
     */
    void unregister( Destination destination ) throws NoSuchDestinationException;
}