package org.componenthaus.repository.services;

import junit.framework.TestCase;

import java.io.File;

import org.componenthaus.tests.MockComponentRepository;

public class RegisterDownloadableComponentCommandTestCase extends TestCase {

    public void testCallsRepositoryProperly() throws Exception {
        String componentId = "424";
        File file = new File("");
        RegisterDownloadableComponentCommand command = new RegisterDownloadableComponentCommand(componentId, file);
        MockComponentRepository mockRepository = new MockComponentRepository();
        mockRepository.setupExpectedRegisterDownloadable(componentId, file);
        Object result = command.execute(mockRepository);
        assertNull(result);
        mockRepository.verify();
    }
}
