/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.event;

import java.util.EventListener;

import org.drools.rule.RuleSet;

/**
 * @author <a href="mailto:peter.royal@pobox.com">peter royal</a>
 */
public interface Subscriber extends EventListener
{
    /**
     * Get the {@link RuleSet} that will drive the events that this Subscriber gets notified of.
     *
     * @return RuleSet instance. Must never be null.
     */
    RuleSet getRuleSet();
}