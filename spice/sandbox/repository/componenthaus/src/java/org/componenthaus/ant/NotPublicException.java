package org.componenthaus.ant;

public class NotPublicException extends RuntimeException {
    private final String fullyQualifiedName;

    public NotPublicException(String fullyQualifiedName) {
        super("Class " + fullyQualifiedName + " is not public");
        this.fullyQualifiedName = fullyQualifiedName;
    }

    public String getFullyQualifiedName() {
        return fullyQualifiedName;
    }
}
