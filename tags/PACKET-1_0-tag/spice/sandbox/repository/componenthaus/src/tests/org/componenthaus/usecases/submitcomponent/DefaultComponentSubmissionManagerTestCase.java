package org.componenthaus.usecases.submitcomponent;

import junit.framework.TestCase;
import org.componenthaus.ant.metadata.ComponentMetadata;
import org.componenthaus.ant.metadata.InterfaceMetadata;
import org.componenthaus.repository.impl.ComponentFactory;
import org.componenthaus.repository.impl.ComponentImpl;
import org.componenthaus.tests.MockClassAbbreviator;
import org.componenthaus.tests.MockPrevayler;
import org.componenthaus.tests.MockMetadataConverterImpl;

import java.util.Collections;
import java.util.Collection;
import java.util.ArrayList;

public class DefaultComponentSubmissionManagerTestCase extends TestCase {
    private ComponentSubmissionManager submissionManager = null;
    private MockCommandRegistry mockCommandRegistry = null;
    private MockMetadataConverterImpl metadataConverter = null;

    protected void setUp() throws Exception {
        mockCommandRegistry = new MockCommandRegistry();
        metadataConverter = new MockMetadataConverterImpl();
        submissionManager = new DefaultComponentSubmissionManager(metadataConverter, new MockPrevayler(),mockCommandRegistry);
    }

    public void testThrowsIllegalArgumentExceptionWhenGivenNull() {
        try {
            submissionManager.submit(null);
            fail("Did not get expected exception");
        } catch (IllegalArgumentException e) {
        } catch (ComponentSubmissionException e) {
            e.printStackTrace();
        }
    }

    public void testThrowsExceptionIfComponentContainsNoInterfaces() {
        ComponentMetadata componentMetadata = new ComponentMetadata();
        try {
            submissionManager.submit(componentMetadata);
            fail("Did not get expected exception");
        } catch (IllegalArgumentException e) {
        } catch (ComponentSubmissionException e) {
            e.printStackTrace();
        }
    }

    public void testSubmitsComponentsToRepository() throws ComponentSubmissionException {
        final ComponentImpl component1 = new ComponentImpl("a.b","1.0",Collections.EMPTY_LIST,"c","c","d");
        final ComponentImpl component2 = new ComponentImpl("e.f","1.0",Collections.EMPTY_LIST,"g","g","h");
        final Collection newComponents = new ArrayList();
        newComponents.add(component1);
        newComponents.add(component2);
        metadataConverter.setSetupResult(newComponents);
        mockCommandRegistry.setupExpectedAddComponent(component1);
        mockCommandRegistry.setupExpectedAddComponent(component2);
        mockCommandRegistry.setupExpectedSubmitComponentCalls(2);
        ComponentMetadata componentMetadata = new ComponentMetadata();
        componentMetadata.addInterface(new InterfaceMetadata("a","b","c","d",false));
        componentMetadata.addInterface(new InterfaceMetadata("e","f","g","h",true));
        Collection components = submissionManager.submit(componentMetadata);
        assertEquals(2,components.size());
        mockCommandRegistry.verify();
    }
}
