package org.componenthaus.repository.impl;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.componenthaus.repository.api.Service;

import java.util.ArrayList;
import java.util.List;

class ServiceImpl implements Service {
    private String name = null;
    private String version = null;
    private String fullyQualifiedName = null;
    private List authors = null;
    private String oneLineDescription = null;
    private String fullDescription = null;

    public ServiceImpl() {
        authors = new ArrayList();
    }

    ServiceImpl(String fullyQualifiedName,String version,List authors,String oneLineDescription,String fullDescription) {
        this.fullyQualifiedName = fullyQualifiedName;
        this.version = version;
        this.authors = authors;
        this.oneLineDescription = oneLineDescription;
        this.fullDescription = fullDescription;
        this.name = extractName(fullyQualifiedName);
    }

    private static String extractName(String fullyQualifiedName) {
        String result = fullyQualifiedName;
        if ( fullyQualifiedName != null ) {
            final int lastDot = fullyQualifiedName.lastIndexOf(".");
            result = fullyQualifiedName.substring(lastDot + 1);
        }
        return result;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getFullyQualifiedName() {
        return fullyQualifiedName;
    }

    public List getAuthors() {
        return authors;
    }

    public String getOneLineDescription() {
        return oneLineDescription;
    }

    public String getFullDescription() {
        return fullDescription;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setFullyQualifiedName(String fullyQualifiedName) {
        this.fullyQualifiedName = fullyQualifiedName;
    }

    public void setAuthors(List authors) {
        this.authors = authors;
    }

    public void setOneLineDescription(String oneLineDescription) {
        this.oneLineDescription = oneLineDescription;
    }

    public void setFullDescription(String fullDescription) {
        this.fullDescription = fullDescription;
    }

    public boolean equals(Object o) {
        if (!(o instanceof ServiceImpl)) {
            return false;
        }
        ServiceImpl rhs = (ServiceImpl) o;
        return new EqualsBuilder()
                .append(name, rhs.name)
                .append(version, rhs.version)
                .append(fullyQualifiedName, rhs.fullyQualifiedName)
                .append(authors, rhs.authors)
                .append(oneLineDescription, rhs.oneLineDescription)
                .append(fullDescription, rhs.fullDescription)
                .isEquals();
    }
}
