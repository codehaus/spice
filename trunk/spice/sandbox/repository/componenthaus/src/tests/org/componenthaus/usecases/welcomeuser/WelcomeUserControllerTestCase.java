package org.componenthaus.usecases.welcomeuser;

import junit.framework.TestCase;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import java.io.IOException;

public class WelcomeUserControllerTestCase extends TestCase {

    public void testReturnsCorrectView() throws IOException, ServletException {
        WelcomeUserController controller = new WelcomeUserController();
        final ModelAndView modelAndView = controller.handleRequest(null,null);
        assertEquals("welcomeView", modelAndView.getViewName());
    }
}
