package org.componenthaus.search;

import java.util.List;

public interface SearchService {
    void index(String componentId, String componentDescription) throws Exception;

    /**
     * Perform a query using the supplied query.  Return the results in the
     * supplied collector.  The results must be Strings containing the component
     * id of each match.  Only collection matches beginning at beginIndex and ending
     * at endIndex.
     *
     * @return the total number of matches for the query (not the number returned in collector).
     */
    int search(String query, int beginIndex, int endIndex, List collector) throws Exception;

    public static final class Exception extends java.lang.Exception {
        public Exception(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
