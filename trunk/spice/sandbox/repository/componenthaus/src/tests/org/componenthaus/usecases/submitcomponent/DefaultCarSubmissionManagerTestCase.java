package org.componenthaus.usecases.submitcomponent;

import junit.framework.TestCase;
import org.componenthaus.repository.api.Component;
import org.componenthaus.repository.impl.ComponentImpl;
import org.componenthaus.tests.MockMetadataConverterImpl;
import org.componenthaus.tests.MockPrevayler;
import org.componenthaus.util.file.FileManagerImpl;

import java.io.File;
import java.util.Collections;

public class DefaultCarSubmissionManagerTestCase extends TestCase {
    private static final MockSubmissionMonitor monitor = new MockSubmissionMonitor();
    private MockCommandRegistry mockCommandRegistry = null;

    protected void setUp() throws Exception {
        mockCommandRegistry = new MockCommandRegistry();
    }

    public void testThrowsExceptionWhenSubmissionLacksMetadataFile() throws Exception {
        final DefaultCarSubmissionManager manager = new DefaultCarSubmissionManager(mockCommandRegistry,null,monitor,null,null);
        MissingJarEntryException expected = null;
        try {
            manager.submit(TestFiles.emptyFile());
            fail("Did not get expected exception");
        } catch (MissingJarEntryException e) {
            expected = e;
        }
        assertEquals(CarSubmissionManager.METADATA_FILE_JAR_ENTRY_NAME,expected.getJarEntryName());
    }

    public void testThrowsExceptionWhenSubmissionLacksDistribution() throws Exception {
        final DefaultCarSubmissionManager manager = new DefaultCarSubmissionManager(mockCommandRegistry,null,monitor,null,null);
        MissingJarEntryException expected = null;
        try {
            manager.submit(TestFiles.justMetadataFile());
            fail("Did not get expected exception");
        } catch (MissingJarEntryException e) {
            expected = e;
        }
        assertEquals(CarSubmissionManager.DISTRIBUTION_DIRECTORY_JAR_ENTRY_NAME,expected.getJarEntryName());
    }

    public void testHandlesSubmissionCorrectly() throws Exception {
        mockCommandRegistry.setupExpectedSubmitComponentCalls(0); //mocked out - ComponentSubmissionManager does it
        mockCommandRegistry.setupExpectedRegisterDownloadableCalls(1);
        final MockMetadataConverterImpl mockMetadataConverter = new MockMetadataConverterImpl();
        mockMetadataConverter.setSetupResult(Collections.singleton(new ComponentImpl()));
        final DefaultCarSubmissionManager manager = new DefaultCarSubmissionManager(mockCommandRegistry, new FileManagerImpl(),monitor,new MockPrevayler(), mockMetadataConverter);
        manager.submit(TestFiles.validComponentSubmission());
        mockCommandRegistry.verify();
    }

    private static final class MockSubmissionMonitor implements DefaultCarSubmissionManager.SubmissionMonitor {
        public void componentSubmitted(Component component, File distribution) {
        }
    }
}
