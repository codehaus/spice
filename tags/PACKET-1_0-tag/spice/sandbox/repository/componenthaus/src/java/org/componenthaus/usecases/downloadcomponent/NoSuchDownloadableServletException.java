package org.componenthaus.usecases.downloadcomponent;

import javax.servlet.ServletException;

public class NoSuchDownloadableServletException extends ServletException {
    private final String downloadableId;

    public NoSuchDownloadableServletException(String parameterName) {
        super();
        this.downloadableId = parameterName;
    }

    public String getDownloadableId() {
        return downloadableId;
    }
}
