package org.componenthaus.usecases.downloadcomponent;

import javax.servlet.ServletException;

public class MissingRequestParameterServletException extends ServletException {
    private final String parameterName;

    public MissingRequestParameterServletException(String parameterName) {
        super();
        this.parameterName = parameterName;
    }

    public String getParameterName() {
        return parameterName;
    }
}
