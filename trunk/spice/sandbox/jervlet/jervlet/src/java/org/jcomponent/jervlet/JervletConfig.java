package org.jcomponent.jervlet;

public interface JervletConfig {

    String getHostName();

    int getPort();

    int getMinThreads();

    int getMaxThreads();

    boolean getExtractWarFile();

}
