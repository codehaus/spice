package org.componenthaus.ant;

import org.componenthaus.ant.plugs.JavadocBuilderFactory;
import com.thoughtworks.qdox.JavaDocBuilder;

public class MockJavadocBuilderFactory implements JavadocBuilderFactory {
    private final JavaDocBuilder preparedJavadocBuilder;

    public MockJavadocBuilderFactory(JavaDocBuilder javadocBuilder) {
        this.preparedJavadocBuilder = javadocBuilder;
    }

    public JavaDocBuilder createBuilder() {
        return preparedJavadocBuilder;
    }
}
