package org.componenthaus.repository.impl;

import org.componenthaus.repository.api.Service;

import java.util.List;
import java.util.Iterator;

class ServiceImpl implements Service {
    private String name = null;
    private String version = null;
    private String fullyQualifiedName = null;
    private List authors = null;
    private String oneLineDescription = null;
    private String fullDescription = null;

    ServiceImpl(String fullyQualifiedName,String version,List authors,String oneLineDescription,String fullDescription) {
        this.fullyQualifiedName = fullyQualifiedName;
        this.version = version;
        this.authors = authors;
        this.oneLineDescription = oneLineDescription;
        this.fullDescription = fullDescription;
        this.name = extractName(fullyQualifiedName);
    }

    private static String extractName(String fullyQualifiedName) {
        final int lastDot = fullyQualifiedName.lastIndexOf(".");
        return fullyQualifiedName.substring(lastDot + 1);
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

    public String getAuthorsCSV() {
        final StringBuffer result = new StringBuffer();
        int count = 0;
        for(Iterator i=authors.iterator();i.hasNext();) {
            if ( count > 0) {
                result.append(", ");
            }
            result.append(i.next());
            count++;
        }
        return result.toString();
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
}
