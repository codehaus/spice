/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.swingactions.reader;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.digester.AbstractObjectCreationFactory;
import org.apache.commons.digester.Digester;
import org.apache.commons.digester.Rule;
import org.apache.commons.digester.RuleSetBase;
import org.jcomponent.swingactions.metadata.GroupActionMetaData;
import org.jcomponent.swingactions.metadata.ActionMetaData;
import org.jcomponent.swingactions.metadata.SeparatorActionMetaData;
import org.xml.sax.Attributes;

/**
 * The set of Digester rules required to parse a 
 * configuration file (<code>swingactions.xml</code>).
 * 
 * @author Mauro Talevi
 */
public class ConfigRuleSet extends RuleSetBase {

    /**
     * Adds the set of Rule instances defined in this RuleSet to the
     * specified Digester.
     *
     * @param digester Digester instance to which the new Rule instances
     *  should be added.
     */
    public void addRuleInstances(Digester digester) {

        digester.addFactoryCreate
            ("swingactions/action-set/action",
             new ActionFactory());
        digester.addSetProperties
            ("swingactions/action-set/action");
        digester.addSetNext
            ("swingactions/action-set/action",
             "addActionMetaData",
             "org.jcomponent.swingactions.metadata.ActionMetaData");

        digester.addRule
            ("swingactions/action-groups/group",
             new SetGroupIdRule());
             
        digester.addFactoryCreate
            ("swingactions/action-groups/group/group-action",
             new GroupActionFactory());
        digester.addSetProperties
            ("swingactions/action-groups/group/group-action");
        digester.addSetNext
            ("swingactions/action-groups/group/group-action",
             "addGroupActionMetaData",
             "org.jcomponent.swingactions.metadata.GroupActionMetaData");

        digester.addFactoryCreate
            ("swingactions/action-groups/group/separator",
             new SeparatorActionFactory());
        digester.addSetProperties
            ("swingactions/action-groups/group/separator");
        digester.addSetNext
            ("swingactions/action-groups/group/separator",
             "addSeparatorActionMetaData",
             "org.jcomponent.swingactions.metadata.SeparatorActionMetaData");

    }

}


/**
 * An object creation factory which creates action instances
 */
final class ActionFactory extends AbstractObjectCreationFactory {

    public Object createObject(Attributes attributes) {

        final Map map = new HashMap();
        map.put( ActionMetaData.ID, attributes.getValue( "id" ) );
        map.put( ActionMetaData.NAME, attributes.getValue( "name" ) );
        map.put( ActionMetaData.SHORT_DESCRIPTION, attributes.getValue( "shortDescription" ) );
        map.put( ActionMetaData.LONG_DESCRIPTION, attributes.getValue( "longDescription" ) );
        map.put( ActionMetaData.SMALL_ICON, attributes.getValue( "smallIcon" ) );
        map.put( ActionMetaData.LARGE_ICON, attributes.getValue( "largeIcon" ) );
        map.put( ActionMetaData.ACTION_COMMAND_KEY, attributes.getValue( "commandKey" ) );
        map.put( ActionMetaData.ACCELERATOR_KEY, attributes.getValue( "acceleratorKey" ) );
        map.put( ActionMetaData.MNEMONIC_KEY, attributes.getValue( "mnemonicKey" ) );
        return new ActionMetaData( map );
    }

}

/**
 * An object creation factory which creates action group instances
 */
final class GroupActionFactory extends AbstractObjectCreationFactory {

    public Object createObject(Attributes attributes) {

        final ConfigReader reader = (ConfigReader)digester.peek();
        final Map map = new HashMap();
        map.put( GroupActionMetaData.GROUP_ID, reader.getCurrentGroupId() );
        map.put( GroupActionMetaData.ID, attributes.getValue( "id" ) );
        map.put( GroupActionMetaData.GROUP_ORDER, attributes.getValue( "groupOrder" ) );
        map.put( GroupActionMetaData.NAME, attributes.getValue( "name" ) );
        map.put( GroupActionMetaData.SHORT_DESCRIPTION, attributes.getValue( "shortDescription" ) );
        map.put( GroupActionMetaData.LONG_DESCRIPTION, attributes.getValue( "longDescription" ) );
        map.put( GroupActionMetaData.SMALL_ICON, attributes.getValue( "smallIcon" ) );
        map.put( GroupActionMetaData.LARGE_ICON, attributes.getValue( "largeIcon" ) );
        map.put( GroupActionMetaData.ACTION_COMMAND_KEY, attributes.getValue( "commandKey" ) );
        map.put( GroupActionMetaData.ACCELERATOR_KEY, attributes.getValue( "acceleratorKey" ) );
        map.put( GroupActionMetaData.MNEMONIC_KEY, attributes.getValue( "mnemonicKey" ) );
        return new GroupActionMetaData( map );
    }

}

/**
 * An object creation factory which creates action separator instances
 */
final class SeparatorActionFactory extends AbstractObjectCreationFactory {

    public Object createObject(Attributes attributes) {

        final ConfigReader reader = (ConfigReader)digester.peek();
        final Map map = new HashMap();
        map.put( SeparatorActionMetaData.GROUP_ID, reader.getCurrentGroupId() );
        return new SeparatorActionMetaData( map );
    }

}

/**
 * Rule to set the groupId of the group currently being parsed
 */
final class SetGroupIdRule extends Rule {

    public SetGroupIdRule() {
        super();
    }

    public void begin(Attributes attributes) throws Exception {
        final ConfigReader reader = (ConfigReader)digester.peek();
        reader.setCurrentGroupId( attributes.getValue( "id" ) );
    }

    public void end(Attributes attributes) throws Exception {
        final ConfigReader reader = (ConfigReader)digester.peek();
        reader.setCurrentGroupId( null );
    }

}


