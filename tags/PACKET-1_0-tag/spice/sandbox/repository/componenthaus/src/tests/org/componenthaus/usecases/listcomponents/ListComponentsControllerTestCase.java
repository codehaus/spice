package org.componenthaus.usecases.listcomponents;

import junit.framework.TestCase;
import org.componenthaus.tests.MockComponentRepository;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import java.io.IOException;

public class ListComponentsControllerTestCase extends TestCase {

    public void testCallsRepositoryAndReturnsModel() throws ServletException, IOException {
        MockComponentRepository repository = new MockComponentRepository();
        repository.setupExpectedListComponentsCalls(1);
        ListComponentsController controller = new ListComponentsController(repository);
        ModelAndView modelAndView = controller.handleRequestInternal(null,null);
        assertNotNull(modelAndView.getViewName());
        assertEquals(1,modelAndView.getModel().size());
        assertNotNull(modelAndView.getModel().get(ListComponentsController.MODEL_NAME));
        repository.verify();
    }
}
