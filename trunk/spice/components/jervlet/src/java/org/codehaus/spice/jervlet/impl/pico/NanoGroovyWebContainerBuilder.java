package org.codehaus.spice.jervlet.impl.pico;


import java.util.Map;
import java.util.Collections;
import java.util.HashMap;

import groovy.util.BuilderSupport;

public class NanoGroovyWebContainerBuilder extends BuilderSupport  {

    private int i = 0;

    protected void setParent(Object object, Object object1) {
        System.out.println("--> setParent( " + object + ", " + object1 +")");
    }

    protected Object createNode(Object name) {
        return createNode(name, Collections.EMPTY_MAP);
    }

    protected Object createNode(Object name, Object value) {
        Map attributes = new HashMap();
        attributes.put("class", value);
        return createNode(name, attributes);
    }

    protected Object createNode(Object object, Map map) {
        String rVal = "Dbg-" + ++i;
        System.out.println("--> createNode(" + object + ", " + map + ") returning '" + rVal + "'");
        //TODO.
        return rVal;
    }

    protected Object createNode(Object name, Map attributes, Object object1) {
        return createNode(name, attributes);
    }

    public NanoGroovyWebContainerBuilder() {

    }
}