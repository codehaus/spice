package org.componenthaus.repository.api;

import java.util.List;

/**
 * This represents some nameable, versioned thing with authors and a description,
 * that exposes a service.  In other words, either a service specification or a service
 * implementation (both spec and impl have these things in common).
 */
public interface Service {
    String getName();
    String getVersion();
    String getFullyQualifiedName();
    String getAuthorsCSV();
    List getAuthors();
    String getOneLineDescription();
    String getFullDescription();
}
