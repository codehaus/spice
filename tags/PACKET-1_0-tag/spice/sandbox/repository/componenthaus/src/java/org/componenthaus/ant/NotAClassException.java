package org.componenthaus.ant;

public class NotAClassException extends RuntimeException {
    private String name;

    public NotAClassException(String name) {
        super("Not a class " + name);
        this.name = name;

    }

    public String getName() {
        return name;
    }
}
