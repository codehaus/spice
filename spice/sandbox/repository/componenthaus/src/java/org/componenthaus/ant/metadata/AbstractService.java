package org.componenthaus.ant.metadata;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.List;
import java.util.ArrayList;

public abstract class AbstractService {
    protected String fullyQualifiedName = null;
    protected String version = null;
    protected List authors = null;
    protected String oneLineDescription = null;
    protected String fullDescription = null;

    public AbstractService() {
        authors = new ArrayList();
    }

    public AbstractService(String fullyQualifiedName, String version, List authors, String oneLineDescription, String fullDescription) {
        this.fullyQualifiedName = fullyQualifiedName;
        this.version = version;
        this.authors = authors;
        this.oneLineDescription = oneLineDescription;
        this.fullDescription = fullDescription;
    }

    public String getFullyQualifiedName() {
        return fullyQualifiedName;
    }

    public void setFullyQualifiedName(String fullyQualifiedName) {
        this.fullyQualifiedName = fullyQualifiedName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List getAuthors() {
        return authors;
    }

    public void setAuthors(List authors) {
        this.authors = authors;
    }

    public void addAuthor(final String author) {
        System.out.println("Calling add author");
        authors.add(author);
    }

    public String getOneLineDescription() {
        return oneLineDescription;
    }

    public void setOneLineDescription(String oneLineDescription) {
        this.oneLineDescription = oneLineDescription;
    }

    public String getFullDescription() {
        return fullDescription;
    }

    public void setFullDescription(String fullDescription) {
        this.fullDescription = fullDescription;
    }

    public String toString() {
        return new ToStringBuilder(this).
                append("fullyQualifiedName",fullyQualifiedName).
                append("version",version).
                append("authors",authors).
                append("oneLineDescription",oneLineDescription).
                append("fullDescription",fullDescription).toString();
    }
}
