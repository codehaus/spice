package org.componenthaus.usecases.searchcomponents;

import javax.servlet.ServletException;

public class SearchServiceFailedServletException extends ServletException {
    public SearchServiceFailedServletException(String message, Throwable cause) {
        super(message,cause);
    }
}
