package org.componenthaus.usecases.submitcomponent;

import org.componenthaus.ant.metadata.ComponentMetadata;
import org.componenthaus.ant.metadata.InterfaceMetadata;
import org.componenthaus.ant.ClassAbbreviator;
import org.componenthaus.repository.api.Component;
import org.componenthaus.repository.impl.ComponentFactory;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collections;
import java.io.StringReader;

import com.thoughtworks.qdox.JavaDocBuilder;
import com.thoughtworks.qdox.model.JavaClass;

public class MetadataConverterImpl implements MetadataConverter {
    private final ComponentFactory componentFactory;
    private final ClassAbbreviator classAbbreviator;

    public MetadataConverterImpl(ComponentFactory componentFactory, ClassAbbreviator classAbbreviator) {
        this.componentFactory = componentFactory;
        this.classAbbreviator = classAbbreviator;
    }

    public Collection convert(ComponentMetadata componentMetadata) {
        if ( componentMetadata == null ) {
            throw new IllegalArgumentException("componentMetadata cannot be null");
        }
        if ( componentMetadata.numInterfaces() <= 0 ) {
            throw new IllegalArgumentException("componentMetadata must contain at least one service interface");
        }
        Collection result = new ArrayList();
        for(Iterator i=componentMetadata.getInterfaces();i.hasNext();) {
            result.add(createComponent((InterfaceMetadata)i.next()));
        }
        return result;
    }

    private Component createComponent(InterfaceMetadata interfaceMetadata) {
        System.out.println("Adding interface " + interfaceMetadata.getFullyQualifiedName());
        String source = null;
        if ( interfaceMetadata.isClass()) {
            StringBuffer result = new StringBuffer();
            JavaDocBuilder builder = new JavaDocBuilder();
            builder.addSource(new StringReader(interfaceMetadata.getSource()));
            JavaClass aClass = builder.getClassByName(interfaceMetadata.getFullyQualifiedName());
            assert aClass != null : interfaceMetadata.getFullyQualifiedName();
            System.out.println("Class = " + aClass);
            classAbbreviator.abbreviate(aClass, result);
            source = result.toString();
            System.out.println("Is a class, formatted the source to be " + source);
        } else {
            System.out.println(interfaceMetadata.getName() + " is an interface");
            source = interfaceMetadata.getSource();
        }
        return componentFactory.createComponent(
                interfaceMetadata.getFullyQualifiedName(),
                "1.0",
                Collections.EMPTY_LIST,
                interfaceMetadata.getShortDescription(),
                interfaceMetadata.getJavadoc(),
                source);
    }
}
