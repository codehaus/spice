package org.componenthaus.search;

import junit.framework.TestCase;
import org.componenthaus.tests.MockSearchService;
import org.componenthaus.tests.MockComponent;

public class AddComponentMonitorTestCase extends TestCase {
    private AddComponentMonitor monitor = null;
    private MockSearchService searchService = null;

    protected void setUp() throws Exception {
        searchService = new MockSearchService();
        monitor = new AddComponentMonitor(searchService);
    }

    public void testCallsSearchServiceCorrectly() {
        String componentId = "lkjlkj";
        String fullDescription = "full desc";
        MockComponent component = new MockComponent(componentId);
        component.setFullDescription(fullDescription);
        searchService.setupExpectedIndexCall(componentId, fullDescription);
        monitor.componentAdded(component);
        searchService.verify();
    }

    public void testPropagatesExceptionCorrectly() {
        SearchService.Exception preparedException = new SearchService.Exception("who cares", new RuntimeException());
        searchService.setupIndexCallException(preparedException);
        RuntimeException expected = null;
        try {
            monitor.componentAdded(new MockComponent(null));
        } catch (RuntimeException e) {
            expected = e;
        }
        assertSame(preparedException,expected.getCause());
    }
}
