package org.componenthaus.search;

import java.util.Collection;

public interface SearchService {
    void index(String componentId, String componentDescription) throws Exception;
    Collection search(String query) throws Exception;

    public static final class Exception extends java.lang.Exception {
        public Exception(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
