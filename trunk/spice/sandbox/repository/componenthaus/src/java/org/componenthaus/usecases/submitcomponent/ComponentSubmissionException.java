package org.componenthaus.usecases.submitcomponent;

public class ComponentSubmissionException extends Exception {
    public ComponentSubmissionException(String message, Exception e) {
        super(message,e);
    }
}
