package org.componenthaus.ant.metadata;

import org.apache.commons.lang.builder.EqualsBuilder;

public class InterfaceMetadata {
    private String packageName;
    private String name;
    private String javadoc;
    private String source;
    private boolean isClass = false;

    public InterfaceMetadata(String packageName, String name, String javadoc, String source, boolean isClass) {
        this.packageName = packageName;
        this.name = name;
        this.javadoc = javadoc;
        this.source = source;
        this.isClass = isClass;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getName() {
        return name;
    }

    public String getFullyQualifiedName() {
        return packageName + "." + name;
    }

    public String getJavadoc() {
        return javadoc;
    }

    public String getShortDescription() {
        return firstLine(javadoc);
    }

    private String firstLine(String javadoc) {
        String result = javadoc;
        int indexOfFirstFullStop = javadoc.indexOf(".");
        if (indexOfFirstFullStop != -1) {
            result = javadoc.substring(0, indexOfFirstFullStop + 1);  //+1 to include the full stop itself.
        }
        return result;
    }

    public String getSource() {
        return source;
    }

    public boolean isClass() {
        return isClass;
    }

    public boolean equals(Object o) {
        if (!(o instanceof InterfaceMetadata)) {
            return false;
        }
        InterfaceMetadata rhs = (InterfaceMetadata) o;
        return new EqualsBuilder()
                .append(packageName, rhs.packageName)
                .append(name, rhs.name)
                .append(javadoc, rhs.javadoc)
                .append(source, rhs.source)
                .append(isClass, rhs.isClass)
                .isEquals();
    }
}
