package org.componenthaus.usecases.submitcomponent;

import javax.servlet.ServletException;

public class RequiredMultipartFieldMissingServletException extends ServletException {
    private final String fieldName;

    public RequiredMultipartFieldMissingServletException(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
