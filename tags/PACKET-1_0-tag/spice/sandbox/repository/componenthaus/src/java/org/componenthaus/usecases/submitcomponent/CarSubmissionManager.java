package org.componenthaus.usecases.submitcomponent;

import java.io.File;
import java.io.IOException;

/**
 * Given a CAR file on the serverside that contains a component submission,
 * crack open that file and process the contents.
 */
public interface CarSubmissionManager {
    String METADATA_FILE_JAR_ENTRY_NAME = "metadata.xml";
    String DISTRIBUTION_DIRECTORY_JAR_ENTRY_NAME = "dist/";
    String JAR_REPOSITORY_DIRECTORY = "repository";

    public void submit(File jarFile) throws Exception;
}
