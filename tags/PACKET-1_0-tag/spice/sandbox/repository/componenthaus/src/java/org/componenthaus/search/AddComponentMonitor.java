package org.componenthaus.search;

import org.componenthaus.repository.api.Component;
import org.componenthaus.repository.api.ComponentRepository;

public class AddComponentMonitor implements ComponentRepository.Monitor {
    private final SearchService searchService;

    public AddComponentMonitor(SearchService searchService) {
        this.searchService = searchService;
    }

    public void componentAdded(final Component component) {
        try {
            searchService.index(component.getId(),component.getFullDescription());
        } catch (SearchService.Exception e) {
            throw new RuntimeException("Failed to add index component " + component,e);
        }
    }
}
