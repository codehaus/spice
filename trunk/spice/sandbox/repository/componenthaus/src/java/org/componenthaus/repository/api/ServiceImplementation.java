package org.componenthaus.repository.api;

import java.util.Collection;

public interface ServiceImplementation extends Service {
    String getId();
    void setId(String id);
    Collection getPlugs();
    void addPlug(Service service);
}
