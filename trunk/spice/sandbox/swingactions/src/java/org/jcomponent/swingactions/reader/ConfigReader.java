/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.swingactions.reader;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.digester.Digester;
import org.jcomponent.swingactions.metadata.ActionMetaData;

/** 
 * ConfigReader reads the configuration from an XML file.
 *
 * @author <a href="mailto:mauro.talevi at aquilonia.org">Mauro Talevi</a>
 */
public class ConfigReader {
    
    List m_actions; 
    
    public ConfigReader() {
        m_actions = new ArrayList();
    }

    public void addActionMetaData( final ActionMetaData metadata )
    {
        m_actions.add( metadata );
    }

    public ActionMetaData[]  getActionMetaData()
    {
        return (ActionMetaData[])m_actions.toArray(new ActionMetaData[ m_actions.size() ] );
    }

    /**
     * Parses configuration resource adding content to this ConfigReader
     * @param resource the InputStream configuration
     * @throws Exception if parse fails
     */
    public void parse( final InputStream resource) throws Exception {     
        final Digester digester = new Digester();
        digester.push(this);
        digester.setNamespaceAware(true);
        //TODO test validation
        digester.setValidating(false);
        digester.addRuleSet(new ConfigRuleSet());
//        digester.register
//            ("-//JComponent Group//DTD SwingActions Configuration 1.0//EN",
//             this.getClass().getResource
//             ("http://jcomponent.org/swingactions/dtds/swingactions_1_0.dtd").toString());

        digester.parse( resource );
    }


    
}
