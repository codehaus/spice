package org.componenthaus.repository.services;

import junit.framework.TestCase;
import org.componenthaus.tests.MockComponent;
import org.componenthaus.tests.MockComponentRepository;

public class SubmitComponentCommandTestCase extends TestCase {

    public void testCallsRepositoryProperly() throws Exception {
        MockComponent component = new MockComponent("23432");
        SubmitComponentCommand command = new SubmitComponentCommand(component);
        MockComponentRepository repository = new MockComponentRepository();
        String preparedResult = "ajdklj";
        repository.setupExpectedAddComponent(component, preparedResult);
        Object result = command.execute(repository);
        assertSame(preparedResult,result);
        repository.verify();
    }
}
