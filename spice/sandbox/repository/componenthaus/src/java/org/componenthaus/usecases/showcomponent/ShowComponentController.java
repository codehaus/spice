package org.componenthaus.usecases.showcomponent;

import org.componenthaus.repository.api.ComponentRepository;
import org.componenthaus.repository.api.Component;
import org.prevayler.Prevayler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ShowComponentController extends AbstractController {
    private Prevayler prevayler = null;

    public void setPrevayler(Prevayler prevayler) {
        this.prevayler = prevayler;
    }

    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final ComponentRepository repository = (ComponentRepository) prevayler.system();
        final Component component = repository.getComponent(request.getParameter("id"));
        System.out.println("Displaying component details with full desc " + component.getFullDescription());
        return new ModelAndView("showComponentView","component",component);
    }
}
