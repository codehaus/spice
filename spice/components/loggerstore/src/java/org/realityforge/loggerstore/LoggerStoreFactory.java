/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.loggerstore;

import java.util.Map;
/**
 * <p>LoggerStoreFactory is a factory interface for LoggerStore instances. 
 * There is a factory implementation for each specific logger implementation 
 * (LogKit, Log4J, JDK14).  
 *
 * <p>The factory also acts a configurator, 
 * handling the specific way in which a logger is configured.
 * The LoggerStore is configured via a map of parameters passed in the
 * create method.  LoggerStoreFactory defines the keys used to retrieve
 * the elements of the map.</p> 
 *
 * @author <a href="mailto:mauro.talevi at aquilonia.org">Mauro Talevi</a>
 * @author <a href="mailto:peter at apache.org">Peter Donald</a>
 */
public interface LoggerStoreFactory
{
    
    /**
     * Constant used to define LogKit Logger type
     */
    public static final String LOGKIT = "logkit";

    /**
     * Constant used to define Log4J Logger type
     */
    public static final String LOG4J = "log4j";

    /**
     * Constant used to define JDK14 Logger type
     */
    public static final String JDK14 = "jdk14";
    
    /** 
     *  The CONFIGURATION key.  Used to denote the configuration object
     *  required by the LoggerStore.  Each LoggerStore accepts different
     *  configuration objects.
     */
    public static final String CONFIGURATION = "configuration";
    
    /** 
     *  The CONFIGURATION_TYPE key.  Used to denote the type of configuration, 
     *  which can take value PROPERTIES or XML.
     */
    public static final String CONFIGURATION_TYPE = "configurationType";
    
    /** 
     *  The PROPERTIES bound value.
     */
    public static final String PROPERTIES = "properties";
    
    /** 
     *  The XML bound value.
     */
    public static final String XML = "xml";
    
    /**
     * Creates a LoggerStore from a given set of configuration parameters.
     *
     * @param config the Map of parameters for the configuration of the store
     * @return the LoggerStore
     * @throws Exception if unable to create the LoggerStore
     */
    public LoggerStore createLoggerStore( final Map config )
        throws Exception;

} 
    
