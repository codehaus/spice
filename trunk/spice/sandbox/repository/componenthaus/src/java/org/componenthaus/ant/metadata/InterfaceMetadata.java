package org.componenthaus.ant.metadata;

public class InterfaceMetadata {
    private String packageName;
    private String name;
    private String javadoc;
    private String source;

    public InterfaceMetadata(String packageName, String name, String javadoc, String source) {
        this.packageName = packageName;
        this.name = name;
        this.javadoc = javadoc;
        this.source = source;
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
        if ( indexOfFirstFullStop != -1 ) {
            result = javadoc.substring(0,indexOfFirstFullStop + 1);  //+1 to include the full stop itself.
        }
        return result;
    }

    public String getSource() {
        return source;
    }
}
