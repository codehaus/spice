package org.componenthaus.usecases.showcomponent;

import junit.framework.TestCase;
import org.componenthaus.tests.MockComponentRepository;
import org.componenthaus.tests.MockComponent;
import org.springframework.web.servlet.ModelAndView;
import com.mockobjects.servlet.MockHttpServletRequest;

import javax.servlet.ServletException;
import java.io.IOException;

public class ShowComponentControllerTestCase extends TestCase {

    public void testCallsRepositoryAndMakesModelAvailable() throws IOException, ServletException {
        MockComponentRepository repository = new MockComponentRepository();
        ShowComponentController controller = new ShowComponentController(repository);
        String componentId = "jjhjh";
        MockComponent component = new MockComponent(componentId);
        repository.setupComponent(component);
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.setupAddParameter("id",componentId);
        ModelAndView modelAndView = controller.handleRequestInternal(request,null);
        assertNotNull(modelAndView.getViewName());
        assertEquals(1,modelAndView.getModel().size());
        assertSame(component,modelAndView.getModel().get(ShowComponentController.MODEL_NAME));
        repository.verify();
    }
}
