package org.componenthaus.usecases.submitcomponent;

public class IllegalJarNameException extends RuntimeException{
    private final String jarName;

    public IllegalJarNameException(String jarName) {
        super();
        this.jarName= jarName;
    }

    public String getJarName() {
        return jarName;
    }
}
