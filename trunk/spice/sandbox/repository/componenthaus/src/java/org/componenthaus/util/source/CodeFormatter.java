package org.componenthaus.util.source;

public interface CodeFormatter {
    String format(final String in) throws Exception;

    public static final class Exception extends java.lang.Exception {
        public Exception(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
