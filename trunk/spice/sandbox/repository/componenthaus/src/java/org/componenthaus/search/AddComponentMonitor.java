package org.componenthaus.search;

import org.componenthaus.repository.api.Component;
import org.componenthaus.repository.api.ComponentRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContextException;

public class AddComponentMonitor implements ComponentRepository.Monitor, InitializingBean {
    private SearchService searchService = null;

    public void afterPropertiesSet() throws Exception {
        if ( searchService == null ) {
            throw new ApplicationContextException("Must set property 'searchService' on " + getClass());
        }
    }

    public void setSearchService(SearchService searchService) {
        this.searchService = searchService;
    }

    public void componentAdded(final Component component) {
        System.out.println("Indexing component " + component);
        try {
            searchService.index(component.getId(),component.getFullDescription());
        } catch (SearchService.Exception e) {
            throw new RuntimeException("Failed to add index component " + component,e);
        }
    }
}
