package org.componenthaus.ant.plugs;

import com.thoughtworks.qdox.JavaDocBuilder;

public class JavadocBuilderFactoryImpl implements JavadocBuilderFactory {
    public JavaDocBuilder createBuilder() {
        return new JavaDocBuilder();
    }
}
