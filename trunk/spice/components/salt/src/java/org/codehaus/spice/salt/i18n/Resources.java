/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.salt.i18n;

import java.util.Locale;

/**
 * A class to simplify extracting localized strings, icons and other common
 * resources from a ResourceBundle.
 *
 * @author Peter Donald
 */
public class Resources extends I18nResources
{
    /**
     * Constructor that builds a manager in default locale using specified
     * ClassLoader.
     *
     * @param baseName the base name of ResourceBundle
     * @param classLoader the classLoader to load ResourceBundle from
     */
    public Resources( final String baseName, final ClassLoader classLoader )
    {
        super( baseName, Locale.getDefault(), classLoader );
    }

    /**
     * Constructor that builds a manager in specified locale.
     *
     * @param baseName the base name of ResourceBundle
     * @param locale the Locale for resource bundle
     * @param classLoader the classLoader to load ResourceBundle from
     */
    public Resources( final String baseName,
                      final Locale locale,
                      final ClassLoader classLoader )
    {
        super( baseName, locale, classLoader );
    }
}
