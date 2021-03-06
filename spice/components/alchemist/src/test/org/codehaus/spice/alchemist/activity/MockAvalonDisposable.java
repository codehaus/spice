/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.alchemist.activity;

import org.apache.avalon.framework.activity.Disposable;


/**
 * @author Mauro Talevi
 */
public class MockAvalonDisposable implements Disposable {
    
    /**
     * @see Disposable#dispose()
     */
    public void dispose(){
        // no-op
    }
}
