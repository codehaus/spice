package org.componenthaus.tests;

import org.componenthaus.search.SearchService;

import java.util.List;

import junit.framework.Assert;

public class MockSearchService implements SearchService {
    private SearchService.Exception toBeThrown = null;
    private List mockResults = null;
    private String expectedQuery;
    private int expectedBeginIndex;
    private int expectedEndIndex;
    private String actualQuery;
    private int actualBeginIndex;
    private int actualEndIndex;
    private String expectedIndexComponentId;
    private String expectedIndexFullDescription;
    private String actualIndexComponentId;
    private String actualIndexFullDescription;
    private SearchService.Exception preparedIndexException;

    public void setToBeThrown(SearchService.Exception toBeThrown) {
        this.toBeThrown = toBeThrown;
    }

    public void index(String componentId, String componentDescription) throws SearchService.Exception {
        if ( preparedIndexException != null ) {
            throw preparedIndexException;
        }
        this.actualIndexComponentId = componentId;
        this.actualIndexFullDescription = componentDescription;
    }

    public int search(String query, int beginIndex, int endIndex, List collector) throws SearchService.Exception {
        if ( toBeThrown != null ) {
            throw toBeThrown;
        }
        collector.addAll(mockResults);
        actualBeginIndex = beginIndex;
        actualEndIndex = endIndex;
        actualQuery = query;
        return mockResults.size();
    }

    public void setupResults(List list) {
        this.mockResults = list;
    }

    public void setupExpectedQueryParams(String query, int beginIndex, int endIndex) {
        expectedQuery = query;
        expectedBeginIndex = beginIndex;
        expectedEndIndex = endIndex;
    }

    public void verify() {
        Assert.assertEquals("Did not get expected query",expectedQuery,actualQuery);
        Assert.assertEquals("Did not get expected beginIndex",expectedBeginIndex,actualBeginIndex);
        Assert.assertEquals("Did not get expected endIndex",expectedEndIndex,actualEndIndex);

        Assert.assertEquals("Did not get expected component id during index",expectedIndexComponentId,actualIndexComponentId);
        Assert.assertEquals("Did not get expected component desc during index",expectedIndexFullDescription,actualIndexFullDescription);

    }

    public void setupExpectedIndexCall(String componentId, String fullDescription) {
        this.expectedIndexComponentId = componentId;
        this.expectedIndexFullDescription = fullDescription;
    }

    public void setupIndexCallException(SearchService.Exception preparedException) {
        preparedIndexException = preparedException;
    }
}
