package org.componenthaus.repository.api;

import java.util.Collection;

/**
 * Represents a component as stored in the repository.
 *
 * @version 1.0
 * @author Mike Hogan
 */
public interface Component extends Service {
    //Do not pull id up into service because a service in an abstract sense does not have an id.  The id is a system level thing.
    String getId();
    void setId(final String id);
    Collection getImplementations();
    void addImplementation(ServiceImplementation impl);
    String getServiceInterface();
}
