package org.componenthaus.usecases.submitcomponent;

import java.io.File;
import java.net.URL;

public class TestFiles {
    public static final String emptyFilename = "empty.jar";
    public static final String validComponentSubmissionFilename = "validComponentSubmission.jar";
    public static final String justMetadataFilename = "justMetadata.jar";

    public static File emptyFile() {
        return new File(getFilePath(emptyFilename));
    }

    private static String getFilePath(String filename) {
        URL resource = TestFiles.class.getResource(filename);
        if ( resource == null ) {
            resource = TestFiles.class.getResource("/" + filename);
        }
        return resource.getFile();
    }

    public static File validComponentSubmission() {
        return new File(getFilePath(validComponentSubmissionFilename));
    }

    public static File justMetadataFile() {
        return new File(getFilePath(justMetadataFilename));
    }
}
