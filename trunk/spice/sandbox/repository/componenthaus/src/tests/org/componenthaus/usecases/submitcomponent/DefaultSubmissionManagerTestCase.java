package org.componenthaus.usecases.submitcomponent;

import junit.framework.TestCase;
import org.componenthaus.repository.impl.ComponentFactory;
import org.componenthaus.tests.MockPrevalentSystem;

public class DefaultSubmissionManagerTestCase extends TestCase {
    private MockCommandRegistry mockCommandRegistry = null;

    protected void setUp() throws Exception {
        mockCommandRegistry = new MockCommandRegistry();
    }

    public void testThrowsExceptionWhenSubmissionLacksMetadataFile() throws Exception {
        final DefaultSubmissionManager manager = new DefaultSubmissionManager(mockCommandRegistry,null,null);
        MissingJarEntryException expected = null;
        try {
            manager.submit(TestFiles.emptyFile());
            fail("Did not get expected exception");
        } catch (MissingJarEntryException e) {
            expected = e;
        }
        assertEquals(SubmissionManager.METADATA_FILE_JAR_ENTRY_NAME,expected.getJarEntryName());
    }

    public void testHandlesMetadataFileCorrectly() throws Exception {
        mockCommandRegistry.setupExpectedSubmitComponentCalls(1);
        final DefaultSubmissionManager manager = new DefaultSubmissionManager(mockCommandRegistry,new ComponentFactory(), new MockPrevalentSystem());
        manager.submit(TestFiles.validComponentSubmission());
        mockCommandRegistry.verify();
    }
}
