package org.componenthaus.usecases.submitcomponent;

import java.io.File;
import java.io.IOException;

/**
 * Given a jar file on the serverside that contains a component submission,
 * crack open that jar and process the contents.
 */
public interface SubmissionManager {
    String METADATA_FILE_JAR_ENTRY_NAME = "metadata.xml";

    public void submit(File jarFile) throws Exception;
}
