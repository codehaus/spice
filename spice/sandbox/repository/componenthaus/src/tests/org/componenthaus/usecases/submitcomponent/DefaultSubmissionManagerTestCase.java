package org.componenthaus.usecases.submitcomponent;

import junit.framework.TestCase;
import org.componenthaus.repository.api.Component;
import org.componenthaus.repository.impl.ComponentFactory;
import org.componenthaus.tests.MockPrevayler;
import org.componenthaus.util.file.FileManagerImpl;

import java.io.File;

public class DefaultSubmissionManagerTestCase extends TestCase {
    private static final MockSubmissionMonitor monitor = new MockSubmissionMonitor();
    private MockCommandRegistry mockCommandRegistry = null;

    protected void setUp() throws Exception {
        mockCommandRegistry = new MockCommandRegistry();
    }

    public void testThrowsExceptionWhenSubmissionLacksMetadataFile() throws Exception {
        final DefaultSubmissionManager manager = new DefaultSubmissionManager(mockCommandRegistry,null,null,monitor,null);
        MissingJarEntryException expected = null;
        try {
            manager.submit(TestFiles.emptyFile());
            fail("Did not get expected exception");
        } catch (MissingJarEntryException e) {
            expected = e;
        }
        assertEquals(SubmissionManager.METADATA_FILE_JAR_ENTRY_NAME,expected.getJarEntryName());
    }

    public void testThrowsExceptionWhenSubmissionLacksDistribution() throws Exception {
        final DefaultSubmissionManager manager = new DefaultSubmissionManager(mockCommandRegistry,null,null,monitor,null);
        MissingJarEntryException expected = null;
        try {
            manager.submit(TestFiles.justMetadataFile());
            fail("Did not get expected exception");
        } catch (MissingJarEntryException e) {
            expected = e;
        }
        assertEquals(SubmissionManager.DISTRIBUTION_DIRECTORY_JAR_ENTRY_NAME,expected.getJarEntryName());
    }

    public void testHandlesSubmissionCorrectly() throws Exception {
        mockCommandRegistry.setupExpectedSubmitComponentCalls(1);
        mockCommandRegistry.setupExpectedRegisterDownloadableCalls(1);
        final DefaultSubmissionManager manager = new DefaultSubmissionManager(mockCommandRegistry,new ComponentFactory(), new FileManagerImpl(),monitor,new MockPrevayler());
        manager.submit(TestFiles.validComponentSubmission());
        mockCommandRegistry.verify();
    }

    private static final class MockSubmissionMonitor implements DefaultSubmissionManager.SubmissionMonitor {
        public void componentSubmitted(Component component, File distribution) {
            System.out.println("Submitted component " + component + ", jar file is " + distribution.getAbsolutePath());
        }
    }
}
