package org.componenthaus.usecases.submitcomponent;

public class MissingJarEntryException extends RuntimeException {
    private String jarEntryName;

    public MissingJarEntryException(String jarEntryName) {
        super();
        this.jarEntryName = jarEntryName;
    }

    public String getJarEntryName() {
        return jarEntryName;
    }
}
